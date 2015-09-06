package com.example.rk.mynews.core;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.rk.mynews.dao.BitmapDiskLrucache;
import com.example.rk.mynews.dao.BitmapLrucache;
import com.example.rk.mynews.ui.activity.MainActivity;
import com.example.rk.mynews.ui.view.LoadProgress;
import com.example.rk.mynews.utils.BitmapTool;
import com.example.rk.mynews.utils.DiskLruCache;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.NumberFormat;


/**
 * 实现图片的内存，本地2级缓存，下载进度跟新
 * Created by RK on 2015/8/8.
 */
public class BitmapWorkerTask extends AsyncTask<String, Double, Bitmap> {

    private SoftReference<ImageView> imageSoftReference;
    private SoftReference<LoadProgress> progressSoftReference;
    private String data = "";
    private LoadProgress progressText;
    public static int PROGRESS_TEXT=76588;

    public BitmapWorkerTask(ImageView imageView,View view) {
        imageSoftReference = new SoftReference<ImageView>(imageView);
        progressText= (LoadProgress) view;
        progressText.setTag(this);
        progressText.setVisibility(View.VISIBLE);
        progressSoftReference=new SoftReference<LoadProgress>(progressText);
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        // TODO Auto-generated method stub

        ImageView imageView = imageSoftReference.get();
        int w=imageView.getMeasuredWidth();
        int h=imageView.getHeight();
        Bitmap bitmap;
        BitmapDiskLrucache diskLrucache=BitmapDiskLrucache.getInstance(MyApplication.getContext());
        InputStream inputStream;
        data = params[0];
        if ((inputStream=diskLrucache.getDataFromDiskLrucache(data))!=null) {
            bitmap=BitmapFactory.decodeStream(
                    inputStream,
                    new Rect(),
                    BitmapTool.getOptions(w,h) );
            try {
                if (inputStream!=null)
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }
        /*从网络获得图片*/
        byte[] result = doGet(data, diskLrucache,getBitmapWorkerTask(imageSoftReference.get()));
        if (result==null){return null;}
        //对位图解码，保证图片不会 太大，导致oom
        bitmap = BitmapTool.decodeBitmapFromByteArray(result, w, h);
        /*将图片放入内存*/
        BitmapLrucache.getInstance().putBitmap(data, bitmap);
        return bitmap;
    }

    @Override
    protected void onProgressUpdate(Double... data) {
        super.onProgressUpdate(data);
        progressSoftReference.get().setSweepAngle((int) (360*data[0]));
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        // TODO Auto-generated method stub
        super.onPostExecute(bitmap);
        if (isCancelled()) {
            bitmap = null;
        }
        if (imageSoftReference != null && bitmap != null) {
            final ImageView imageView = imageSoftReference.get();
            final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
            if (this == bitmapWorkerTask) {
                progressSoftReference.get().setVisibility(View.GONE);
                imageView.setImageBitmap(bitmap);
            }
        }
    }

    /**
     * 被调用的静态方法
     *
     * @param data
     * @param imageView
     * @param mResources
     * @param bitmap
     */
    public static void loadBitmap(String data, ImageView imageView, Resources mResources, View bitmap) {
        Bitmap bitmap1 = null;
        if ((bitmap1 = BitmapLrucache.getInstance().getBitmap(data)) != null) {
            imageView.setImageBitmap(bitmap1);
            return;
        }
        if (cancelPotntialWork(data, imageView)) {
            final BitmapWorkerTask task = new BitmapWorkerTask(imageView,bitmap);
            final AsyncDrawable asyncDrawable = new AsyncDrawable(mResources, bitmap,
                    task);
            imageView.setImageDrawable(asyncDrawable);
            task.execute(data);
        }
    }

    /**
     * 判断imageview 是否有绑定异步任务，如果有判断任务下载的url,如果和将要执行的URL相同
     * 则返回false，否则true。
     *
     * @param data
     * @param imageView
     * @return
     */
    public static boolean cancelPotntialWork(String data, ImageView imageView) {
        final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
        if (bitmapWorkerTask != null) {
            final String bitmapData = bitmapWorkerTask.data;
            if (bitmapData != data) {
                bitmapWorkerTask.cancel(true);
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * 帮助方法，
     *
     * @param imageView
     * @return
     */
    private static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
        if (imageView != null) {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                return asyncDrawable.getBitmapWorkerTask();
            }
        }
        return null;
    }
    private void s(View view){}

    //AsyncDrawable 被用来作为占位图片
    public static class AsyncDrawable extends BitmapDrawable {
        private final SoftReference<BitmapWorkerTask> bitmapWorkerTaskReference;

        public AsyncDrawable(Resources res, View bitmap,
                             BitmapWorkerTask bitmapWorkerTask) {
            super(res);
            bitmapWorkerTaskReference =
                    new SoftReference<BitmapWorkerTask>(bitmapWorkerTask);

        }

        public BitmapWorkerTask getBitmapWorkerTask() {
            return bitmapWorkerTaskReference.get();
        }
    }

    /**
     * 用Get方法返回该链接地址的byte[]数据
     *
     * @param url    URL地址
     * @return  服务器返回的数据
     */

    public  byte[] doGet(String url,BitmapDiskLrucache diskLrucache, final BitmapWorkerTask task){
        if (url==null)return null;
        HttpURLConnection connection=null;
        BufferedInputStream is=null;
        ByteArrayOutputStream os=null;
        OutputStream editorStream;
        DiskLruCache.Editor editor = null;
        int fileSize =0; //文件大小
        int hasRead=0;   //已经下载大小
        try {
            connection= (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            initHttpURLConnection(connection);
            if (connection.getResponseCode()==200){
                fileSize=connection.getContentLength();
                is=new BufferedInputStream(connection.getInputStream());
                os=new ByteArrayOutputStream();
                editor=diskLrucache.putDataToDiskLrucache(url);
                if (editor==null){
                    editor=diskLrucache.putDataToDiskLrucache(url);
                    if (editor==null)return null;
                }
                editorStream=editor.newOutputStream(0);
                int len=-1;
                byte[] buf=new byte[10240];
                NumberFormat format=NumberFormat.getNumberInstance();
                format.setMaximumFractionDigits(2);
                double percent=0;
                while ((len=is.read(buf))!=-1){
                    hasRead+=len;
                    os.write(buf, 0, len);
                    editorStream.write(buf, 0, len);

                    percent= Double.parseDouble((format.format(hasRead*1.0/fileSize)));
                    if (task!=null){
                        task.publishProgress(percent);

                    }
                }
                os.flush();
                editor.commit();
                return os.toByteArray();
            }else {
                return null;
            }
        } catch (IOException e) {

            e.printStackTrace();
        }finally {
            try{
                if (is!=null)is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (os!=null) try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            assert connection != null;
            connection.disconnect();
        }
        return null;
    }

    public static void initHttpURLConnection(HttpURLConnection conn){
        conn.setRequestProperty(
                "Accept",
                "image/gif, image/jpeg, image/pjpeg, image/pjpeg, "
                        + "application/x-shockwave-flash, application/xaml+xml, "
                        + "application/vnd.ms-xpsdocument, application/x-ms-xbap, "
                        + "application/x-ms-application, application/vnd.ms-excel, "
                        + "application/vnd.ms-powerpoint, application/msword, */*");
        conn.setRequestProperty("Accept-Language", "zh-CN");
        conn.setRequestProperty("Charset", "UTF-8");
    }


}
