package com.example.rk.mynews.core;

import android.app.Application;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LRULimitedMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

/**
 * Created by RK on 2015/7/29.
 */
public class MyApplication extends Application {
    private static Context context;
    private static RequestQueue mRequestQueue;
    private static int Network_Info;
    public static String mPage="0";


    private static boolean ONLY_DOWN_BY_WIFI;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        initImageLoader(getApplicationContext());
    }

    public static Context getContext() {
        return context;
    }

    public static RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(context);
            return mRequestQueue;
        } else {
            return mRequestQueue;
        }
    }

    //
    // ≈‰÷√universal-image-loader-1.9.1.jar
    private void initImageLoader(Context context) {
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration
                .Builder(context).threadPoolSize(2).threadPriority(Thread.NORM_PRIORITY - 2)
                .memoryCache(new LRULimitedMemoryCache(2 * 1024 * 1024)).discCacheSize(15 * 1024 * 1024)
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.FIFO)
                .imageDownloader(new BaseImageDownloader(context, 4 * 1000, 20 * 1000))
                .build();
        ImageLoader.getInstance().init(configuration);
    }

    public static Integer getNetworkInfo() {
        return Network_Info;
    }

    public static void setNetworkInfo(Integer isDownImage) {
        Network_Info = isDownImage;
    }

    public static boolean isONLY_DOWN_BY_WIFI() {
        return ONLY_DOWN_BY_WIFI;
    }

    public static void setONLY_DOWN_BY_WIFI(boolean ONLY_DOWN_BY_WIFI) {
        MyApplication.ONLY_DOWN_BY_WIFI = ONLY_DOWN_BY_WIFI;
    }
}
