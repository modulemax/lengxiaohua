package com.example.rk.mynews.dao;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader;
import com.example.rk.mynews.utils.MD5Util;

/**
 * Created by RK on 2015/7/30.
 */
public class BitmapLrucache implements ImageLoader.ImageCache {
    /**该cache是单例*/
    private static BitmapLrucache cache;
    /**内存缓存*/
    private LruCache<String, Bitmap> mMemoryCache;
    /*获取到可用内存的最大值，使用内存超出这个值会引起OutOfMemory异常*/
    final static int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
    // Use 1/8th of the available memory for this memory cache.
    final static int cacheSize = maxMemory / 8;
    /**
     * 获取单例
     * @param
     * @return
     */
    public static BitmapLrucache getInstance(){
        if (cache==null){
            cache=new BitmapLrucache();
        }
        return cache;
    }
    private  BitmapLrucache(){
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };
    }
    @Override
    public Bitmap getBitmap(String s) {
        return mMemoryCache.get(MD5Util.MD5(s));
    }
    @Override
    public void putBitmap(String s, Bitmap bitmap) {
        //图片放入缓存中
        mMemoryCache.put(MD5Util.MD5(s), bitmap);
    }

}
