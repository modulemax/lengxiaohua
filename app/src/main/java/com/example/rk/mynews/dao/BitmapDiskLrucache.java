package com.example.rk.mynews.dao;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.example.rk.mynews.utils.DiskLruCache;
import com.example.rk.mynews.utils.MD5Util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**文件的put和get都是流操作，可能会耗时，需在异步任务执行
 * Created by RK on 2015/8/9.
 */
public class BitmapDiskLrucache {
    private static BitmapDiskLrucache bitmapDiskLrucache;
    /**
     * 图片硬盘缓存核心类。
     */
    private static DiskLruCache mDiskLruCache;
    private static final int DISK_CACHE_SIZE = 1024 * 1024 * 10; // 10MB
    private static final String DISK_CACHE_SUBDIR = "thumbnails";
    public static BitmapDiskLrucache getInstance(Context context){

        if (mDiskLruCache==null){
            bitmapDiskLrucache=new BitmapDiskLrucache(context);
        }
        return bitmapDiskLrucache;
    }
    private BitmapDiskLrucache(Context context){
        try {
            // 获取图片缓存路径
            File cacheDir = getDiskCacheDir(context, DISK_CACHE_SUBDIR);
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            // 创建DiskLruCache实例，初始化缓存数据
            mDiskLruCache = DiskLruCache
                    .open(cacheDir, getAppVersion(context), 1, DISK_CACHE_SIZE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param path
     * @param data
     */
    public void putDataToDiskLrucachebybyte(String path,byte[] data){
        DiskLruCache.Editor editor;
        try {
            editor= mDiskLruCache.edit(MD5Util.MD5(path));
            BufferedOutputStream bufferedOutputStream=new BufferedOutputStream(editor.newOutputStream(0));
            bufferedOutputStream.write(data);
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
            editor.commit();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    /**
     * 返回DiskLruCache.Editor
     * @param path
     * @return
     */
    public DiskLruCache.Editor putDataToDiskLrucache(String path){
        try {
            return mDiskLruCache.edit(MD5Util.MD5(path));

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 返回文件的输出流
     * @param path
     * @return
     */
    public InputStream getDataFromDiskLrucache(String path){
        try {
            DiskLruCache.Snapshot snapshot=mDiskLruCache.get(MD5Util.MD5(path));
            if (snapshot!=null){
                return snapshot.getInputStream(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 将缓存记录同步到journal文件中。
     */
    public void fluchCache() {
        if (mDiskLruCache != null) {
            try {
                mDiskLruCache.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 根据传入的uniqueName获取硬盘缓存的路径地址。
     */
    public static File getDiskCacheDir(Context context, String uniqueName) {
//        String cachePath;
//        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
//                || !Environment.isExternalStorageRemovable()) {
//            cachePath = context.getExternalCacheDir().getPath();
//        } else {
//            cachePath = context.getCacheDir().getPath();
//        }
//        return new File(cachePath + File.separator + uniqueName);
        return context.getFilesDir();
    }
    /**
     * 获取当前应用程序的版本号。
     */
    public static int getAppVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(),
                    0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }
}
