package com.example.rk.mynews.core;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Message;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.rk.mynews.dao.MyNewsDB;
import com.example.rk.mynews.model.TYPE;
import com.example.rk.mynews.ui.activity.MainActivity;
import com.example.rk.mynews.ui.adapter.MyArrayAdapter;
import com.example.rk.mynews.model.Joke;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * Created by RK on 2015/7/29.
 */
public class JokeBiz {
    private static int refrash;

    /**
     * 将字符解析成JOke
     *
     * @param s
     * @return
     */
    public static Joke gsonJoke(String s) {
        Joke joke = null;
        Gson gson = new Gson();
        java.lang.reflect.Type type = new TypeToken<Joke>() {}.getType();
        joke = gson.fromJson(s, type);
        return joke;
    }
    //根据refrash的值，选择更新数据的方式
    public static void getJoke(final MyArrayAdapter arrayAdapter,final int refrash1, int size, int page) {
        if (MyApplication.getNetworkInfo()==-1)return;
        refrash=refrash1;
        if (refrash==TYPE.FROM_DB_REPLACE){
            loadFromDB(arrayAdapter,size,page);
        }else if (refrash==TYPE.FROM_NET_REPLACE){
            loadFromNet(arrayAdapter,size,page);
        }else if (refrash==TYPE.FROM_NET_ADD){
            loadFromNet(arrayAdapter,size,page);
        }
    }

    /**
     * 从网络加载数据
     * @param arrayAdapter
     * @param size
     * @param page
     */
    public static void loadFromNet( final MyArrayAdapter arrayAdapter, final int size, final int page){
        String path = "http://api.1-blog.com/biz/bizserver/xiaohua/list.do?size=" + size + "&page=" + page;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, path,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        /*得到数据，解析数据，发送网络成功消息，更新arrayAdapter*/
                        Log.i("onResponse", "  " + s);
                        final Joke joke=gsonJoke(s);
                        Message msg=new Message();
                        msg.arg1=joke.detail.size();
                        msg.what=TYPE.SUCCESS;
                        MainActivity.mHandler.sendMessage(msg);
                        if (refrash==TYPE.FROM_NET_REPLACE){
                            arrayAdapter.replace(joke);
                        }else if (refrash==TYPE.FROM_NET_ADD){
                            if (joke.detail.size()==0){
                                Log.i("joke.detail.size()==0","重新加载");
                                Random random = new Random();
                                int num = random.nextInt(26000);
                                loadFromNet(arrayAdapter, size, num);
                            }
                            arrayAdapter.addToLast(joke);
                        }
                        arrayAdapter.notifyDataSetChanged();
                        new Thread(){
                            @Override
                            public void run() {
                                super.run();
                                MyNewsDB.getInstance(MyApplication.getContext()).saveJoke(joke);//保存到数据库中
                            }
                        }.start();
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
     * @param page
     */
    public static void loadFromDB(MyArrayAdapter arrayAdapter, int size, int page){
        Joke joke=MyNewsDB.getInstance(MyApplication.getContext()).loadJoke(size);
        if (joke!=null&&joke.detail.size()!=0){
            arrayAdapter.replace(joke);
            arrayAdapter.notifyDataSetChanged();
        }
        else {
            refrash=TYPE.FROM_NET_REPLACE;
            loadFromNet(arrayAdapter,size,page);
        }
    }

    public static int bulkInsert(Joke joke) {
        ArrayList<ContentValues> contentValues = new ArrayList<ContentValues>();
        List<Joke.detail> list=joke.detail;
        if (list!=null){
            for (Joke.detail j:list){
                ContentValues contentValues1=new ContentValues();
                contentValues1.put("xhid",j.xhid);
                contentValues1.put("author",j.author);
                contentValues1.put("content",j.content);
                contentValues1.put("picUrl",j.picUrl);
                contentValues.add(contentValues1);
            }
        }
        ContentValues[] valueArray = new ContentValues[contentValues.size()];
        return MyApplication.getContext().getContentResolver().bulkInsert(
                Uri.parse("content://com.example.rk.mynews.provider/joke"),
                contentValues.toArray(valueArray));
    }


}
