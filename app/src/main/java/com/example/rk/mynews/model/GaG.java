package com.example.rk.mynews.model;

import java.util.List;

/**
 * Created by RK on 2015/8/26.
 */
public class GaG {
    public List<data> data;
    public Paging paging;
    public static class data{
        public String caption;
        public images images;
    }
    public static class images{
        public String small;
        public String normal;
        public String large;
    }
    public static class Paging{
        public String next;
    }


}
