package com.example.rk.mynews.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**提供了数据库初始化，建表语句
 * 升级，打开数据库的方法
 * Created by RK on 2015/7/29.
 */
public class MySQliteOpenHelper extends SQLiteOpenHelper {
    /**
     * joke表
     */
    public static final String CREATE_JOKE ="create table joke ("
            +"_id integer primary key autoincrement,"
            +"xhid,"
            +"content,"
            +"author,"
            +"picUrl)";

    /**
     * joke表
     */
    public static final String CREATE_GAG ="create table gag ("
            +"_id integer primary key autoincrement,"
            +"caption,"
            +"normal,"
            +"large)";
    /**
     * news表
     */
    public static final String CREATE_NEWS ="create table news ("
            +"_id integer primary key autoincrement,"
            +"title,"
            +"source,"
            +"article_url,"
            +"behot_time,"
            +"create_time,"
            +"publish_time,"
            +"bury_count,"
            +"repin_count,"
            +"digg_count)";

    public MySQliteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_JOKE);
        db.execSQL(CREATE_NEWS);
        db.execSQL(CREATE_GAG);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
