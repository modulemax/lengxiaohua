package com.example.rk.mynews.model;

import java.util.List;

/**
 * Created by RK on 2015/8/12.
 */
public class News {
    public String status;
    public String desc;
    public List<detail> detail;
    public static class detail{
        public String title;
        public String source;
        public String article_url;
        public int publish_time;
        public long behot_time;
        public int create_time;
        public int digg_count;
        public int bury_count;
        public int repin_count;
        public String group_id;
    }
}
