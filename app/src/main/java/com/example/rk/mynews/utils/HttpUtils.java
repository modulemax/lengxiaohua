package com.example.rk.mynews.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.example.rk.mynews.dao.BitmapDiskLrucache;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Http辅助工具类
 * Created by Administrator on 2015/1/18.
 */
public class HttpUtils {

    private static final int TIMEOUT_IN_MILLIONS = 5000;

    /**
     * 用Get方法返回该链接地址的html数据
     *
     * @param urlStr    URL地址
     * @return  服务器返回的数据
     */
    public static String doGet1(String urlStr) throws Exception
    {
        if (urlStr == ""){
            return null;
        }
        URL url;
        HttpURLConnection conn = null;
        InputStream is = null;
        ByteArrayOutputStream baos = null;
        try
        {
            url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(TIMEOUT_IN_MILLIONS);
            conn.setConnectTimeout(TIMEOUT_IN_MILLIONS);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            if (conn.getResponseCode() == 200)
            {
                is = conn.getInputStream();
                baos = new ByteArrayOutputStream();
                int len = -1;
                byte[] buf = new byte[128];

                while ((len = is.read(buf)) != -1)
                {
                    baos.write(buf, 0, len);
                }
                baos.flush();
                return baos.toString();
            } else
            {
                throw new RuntimeException(" responseCode is not 200 ... ");
            }

        } catch (Exception e)
        {
            e.printStackTrace();

        } finally
        {
            try
            {
                if (is != null)
                    is.close();
            } catch (IOException e)
            {
            }
            try
            {
                if (baos != null)
                    baos.close();
            } catch (IOException e)
            {
            }
            conn.disconnect();
        }

        return null ;
    }
    /**
     * 用Get方法返回该链接地址的byte[]数据
     *
     * @param url    URL地址
     * @return  服务器返回的数据
     */
    public static byte[] doGet(String url,BitmapDiskLrucache diskLrucache){
        if (url==null)return null;
        URL url1=null;
        HttpURLConnection connection=null;
        BufferedInputStream is=null;
        ByteArrayOutputStream os=null;
        OutputStream editorStream;
        try {
            connection= (HttpURLConnection) new URL(url).openConnection();
            connection.setReadTimeout(TIMEOUT_IN_MILLIONS);
            connection.setConnectTimeout(TIMEOUT_IN_MILLIONS);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setReadTimeout(20*1000);
            connection.setConnectTimeout(5*1000);
            if (connection.getResponseCode()==200){
                is=new BufferedInputStream(connection.getInputStream());
                os=new ByteArrayOutputStream();
                DiskLruCache.Editor editor=diskLrucache.putDataToDiskLrucache(url);
                editorStream=editor.newOutputStream(0);
                int len=-1;
                byte[] buf=new byte[1024];
                int i=0;
                while ((len=is.read(buf))!=-1){
                    os.write(buf,0,len);
                    editorStream.write(buf,0,len);
                    Log.i("tt",""+i++);
                }
                        /*将图片放入disk*/

                os.flush();
                editor.commit();
                return os.toByteArray();
            }else {
                throw new RuntimeException("responsecode is not 200");
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

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url
     *            发送请求的 URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     * @throws Exception
     */
    public static String doPost(String url, String param)
    {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try
        {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            HttpURLConnection conn = (HttpURLConnection) realUrl
                    .openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            conn.setRequestProperty("charset", "utf-8");
            conn.setUseCaches(false);
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setReadTimeout(TIMEOUT_IN_MILLIONS);
            conn.setConnectTimeout(TIMEOUT_IN_MILLIONS);

            if (param != null && !param.trim().equals(""))
            {
                // 获取URLConnection对象对应的输出流
                out = new PrintWriter(conn.getOutputStream());
                // 发送请求参数
                out.print(param);
                // flush输出流的缓冲
                out.flush();
            }
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null)
            {
                result += line;
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        // 使用finally块来关闭输出流、输入流
        finally
        {
            try
            {
                if (out != null)
                {
                    out.close();
                }
                if (in != null)
                {
                    in.close();
                }
            } catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 当前是否有网络连接
     * @return
     */
    public static boolean IsNetAvailable(Context context){
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        return (info != null && info.isConnected());

    }

}
