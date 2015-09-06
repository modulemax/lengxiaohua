package com.example.rk.mynews.core;

import android.os.Message;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.rk.mynews.dao.MyNewsDB;
import com.example.rk.mynews.model.Joke;
import com.example.rk.mynews.model.News;
import com.example.rk.mynews.model.TYPE;
import com.example.rk.mynews.ui.activity.MainActivity;
import com.example.rk.mynews.ui.adapter.MyArrayAdapter;
import com.example.rk.mynews.ui.adapter.NewsListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RK on 2015/8/12.
 */
public class NewsBiz {
    /**
     * 将字符串解析成News
     * @param s
     * @return
     */
    public static News gsonNews(String s){
        News news=new News();
        news.detail=new ArrayList<News.detail>();
        try {
            JSONObject object=new JSONObject(s);
            JSONArray array=object.getJSONArray("detail");

            for (int i=0;i<array.length();i++){
                JSONObject jsonObject=array.getJSONObject(i);
                String title=jsonObject.getString("title");
                String source=jsonObject.getString("source");
                String article_url=jsonObject.getString("article_url");
                int publish_time=jsonObject.getInt("publish_time");
                long behot_time=jsonObject.getLong("behot_time");
                int create_time=jsonObject.getInt("create_time");
                int digg_count=jsonObject.getInt("digg_count");
                int bury_count=jsonObject.getInt("bury_count");
                int repin_count=jsonObject.getInt("repin_count");

                News.detail detail=new News.detail();

                detail.title=title;
                detail.source=source;
                detail.article_url=article_url;
                detail.publish_time=publish_time;
                detail.behot_time=behot_time;
                detail.create_time=create_time;
                detail.digg_count=digg_count;
                detail.bury_count=bury_count;
                detail.repin_count=repin_count;

                news.detail.add(detail);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return news;
    }

    /**
     * 选择数据的加载和更新方式
     */
    private static int way;
    public static void getNews(NewsListAdapter adapter,int way1,int size,long time){
        way=way1;
        if (way==TYPE.FROM_DB_REPLACE){
            loadNewsFromDB(adapter, size, time);
        }else if (way==TYPE.FROM_NET_REPLACE){
            getNewsFromNet(adapter, size, time);
        }else if (way==TYPE.FROM_NET_ADD){
            getNewsFromNet(adapter, size, time);
        }
    }
    /**
     * 从网络加载数据
     * @param adapter
     * @param size
     * @param time
     */
    private static void getNewsFromNet(final NewsListAdapter adapter, final int size, final long time){
        String path = "http://api.1-blog.com/biz/bizserver/news/list.do?size="
                + size+"&max_behot_time="+time;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, path,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        /*得到数据，解析数据，发送网络成功消息，更新arrayAdapter*/
                        Log.i("onResponse", "  " + s);
                        News news=gsonNews(s);
                        //**判断数据的跟新方式
                        if (way==TYPE.FROM_NET_REPLACE){
                            adapter.replace(news);
                        }else if (way==TYPE.FROM_NET_ADD){
                            adapter.addtoLast(news);
                        }
                        adapter.notifyDataSetChanged();
                        Message msg=new Message();
                        msg.what=TYPE.SUCCESS;
                        msg.arg1=news.detail.size();
                        MainActivity.mHandler.sendMessage(msg);
                        MyNewsDB.getInstance(MyApplication.getContext()).saveNews(news);//保存到数据库中

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Message msg=new Message();
                        msg.what=TYPE.ERROR;
                        MainActivity.mHandler.sendMessage(msg);
                    }
                });
        /**
         * setRetryPolicy(new DefaultRetryPolicy(
         *IMAGE_TIMEOUT_MS,  //  超时时间
         *IMAGE_MAX_RETRIES,  //  超时后retry次数
         *IMAGE_BACKOFF_MULT));// 超时因子
         */
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(2000,1,1f));
        MyApplication.getRequestQueue().add(stringRequest);
    }
    /**
     * 从数据库中读取数据
     * @param arrayAdapter
     * @param size
     * @param time
     */
    private static void loadNewsFromDB(NewsListAdapter arrayAdapter, int size, long time){
        News news=MyNewsDB.getInstance(MyApplication.getContext()).loadNews(size);
        if (news!=null){
            arrayAdapter.replace(news);
            arrayAdapter.notifyDataSetChanged();
        }
        else {
            getNewsFromNet(arrayAdapter, size, time);
        }
    }
}
