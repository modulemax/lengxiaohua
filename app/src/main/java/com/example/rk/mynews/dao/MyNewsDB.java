package com.example.rk.mynews.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.rk.mynews.model.GaG;
import com.example.rk.mynews.model.Joke;
import com.example.rk.mynews.model.News;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RK on 2015/7/29.
 */
public class MyNewsDB {
    /**
     * 数据库名称
     */
    public static final String DB_NAME="Mynews";
    /**
     * 版本号
     */
    public static final int VERSION=1;

    public static MyNewsDB myNewsDB;

    private SQLiteDatabase db;

    /**
     * 私有化构造方法
     */
    private MyNewsDB(Context context){
        MySQliteOpenHelper newsdb=new MySQliteOpenHelper(context,DB_NAME,null,VERSION);
        db=newsdb.getWritableDatabase();
    }
    /**
     * ͨ提供单例CoolWeatherDB对象
     */
    public synchronized static MyNewsDB getInstance(Context context){
        if (myNewsDB==null){
            myNewsDB=new MyNewsDB(context);
        }
        return myNewsDB;
    }
    /**
     * 保存Joke
     */
    public void saveJoke(Joke joke){
        db.delete("joke",null,null);
        Log.i("saveJoke","");
        List<Joke.detail> list=joke.detail;
        if (list!=null){

            for (Joke.detail j:list){
                ContentValues contentValues=new ContentValues();
                contentValues.put("xhid",j.xhid);
                contentValues.put("author",j.author);
                contentValues.put("content",j.content);
                contentValues.put("picUrl",j.picUrl);

                db.insert("joke",null,contentValues);
            }
        }
    }

    /**
     * load数据
     * size条数
     */
    public Joke loadJoke(int size){
        List<Joke.detail> list=new ArrayList<Joke.detail>();
        Cursor cursor=db.query("joke",null,null,null,null,null,null);

        if (cursor.moveToLast()) {
            do {
                Joke.detail detail = new Joke.detail();
                detail.xhid = cursor.getString(cursor.getColumnIndex("xhid"));
                detail.author = cursor.getString(cursor.getColumnIndex("author"));
                detail.content = cursor.getString(cursor.getColumnIndex("content"));
                detail.picUrl = cursor.getString(cursor.getColumnIndex("picUrl"));
                list.add(detail);
            } while (cursor.moveToPrevious()&&(--size)>0);
            Joke joke = new Joke();
            joke.detail = list;
            return joke;
        }
        else {
            Log.e("数据库内Joke为空","");
            return null;
        }

    }


    /**
     * 保存news
     */
    public void saveNews(News news){
        db.delete("News",null,null);
        Log.i("savenews","");
        List<News.detail> list=news.detail;
        if (list!=null){

            for (News.detail detail:list){
                ContentValues contentValues=new ContentValues();
                contentValues.put("title",detail.title);
                contentValues.put("source",detail.source);
                contentValues.put("article_url",detail.article_url);
                contentValues.put("publish_time",detail.publish_time);
                contentValues.put("behot_time",detail.behot_time);
                contentValues.put("create_time",detail.create_time);
                contentValues.put("digg_count",detail.digg_count);
                contentValues.put("bury_count",detail.bury_count);
                contentValues.put("repin_count",detail.repin_count);

                db.insert("news",null,contentValues);
            }
        }
    }

    /**
     * load数据
     * size条数
     */
    public News loadNews(int size){
        List<News.detail> list=new ArrayList<News.detail>();
        Cursor cursor=db.query("news",null,null,null,null,null,null);

        if (cursor.moveToFirst()) {
            do {
                News.detail detail = new News.detail();
                detail.title = cursor.getString(cursor.getColumnIndex("title"));
                detail.source = cursor.getString(cursor.getColumnIndex("source"));
                detail.article_url = cursor.getString(cursor.getColumnIndex("article_url"));
                detail.publish_time = cursor.getInt(cursor.getColumnIndex("publish_time"));
                detail.behot_time = cursor.getInt(cursor.getColumnIndex("behot_time"));
                detail.digg_count = cursor.getInt(cursor.getColumnIndex("digg_count"));
                detail.bury_count = cursor.getInt(cursor.getColumnIndex("bury_count"));
                detail.repin_count = cursor.getInt(cursor.getColumnIndex("repin_count"));

                list.add(detail);
            } while (cursor.moveToNext()&&(--size)>0);
            News news = new News();
            news.detail = list;
            return news;
        }
        else {
            Log.e("数据库内News为空","");
            return null;
        }

    }

    /**
     * 保存Joke
     */
    public void saveGaG(GaG gag){
        db.delete("gag", null, null);
        if (gag==null)Log.e("saveJoke==null","");
        List<GaG.data> list=gag.data;
        if (list!=null){

            for (GaG.data j:list){
                ContentValues contentValues=new ContentValues();
                contentValues.put("caption",j.caption);
                contentValues.put("normal",j.images.normal);
                contentValues.put("large",j.images.large);

                db.insert("gag",null,contentValues);
            }
        }
    }

    /**
     * load数据
     * size条数
     */
    public GaG loadGaG(){
        List<GaG.data> list=new ArrayList<GaG.data>();
        Cursor cursor=db.query("gag",null,null,null,null,null,null);

        if (cursor.moveToLast()) {
            do {
                GaG.data data=new GaG.data();
                GaG.images images=new GaG.images();
                data.caption = cursor.getString(cursor.getColumnIndex("caption"));
                images.normal = cursor.getString(cursor.getColumnIndex("normal"));
                images.large = cursor.getString(cursor.getColumnIndex("large"));
                data.images=images;
                list.add(data);
            } while (cursor.moveToPrevious());
            GaG gaG=new GaG();
            gaG.data=list;
            return gaG;
        }
        else {
            Log.e("数据库内Joke为空","");
            return null;
        }

    }



}
