package com.example.rk.mynews.model;

import android.graphics.Bitmap;

import java.util.List;

/**
 * Created by RK on 2015/7/28.
 */
public class Joke {
    public String status;
    public String desc;
    public List<detail> detail;
    public static class detail{
        public String id;
        public String xhid;
        public String author;
        public String content;
        public String picUrl;
        public String status;
        public Bitmap bitmap;


    }
}
