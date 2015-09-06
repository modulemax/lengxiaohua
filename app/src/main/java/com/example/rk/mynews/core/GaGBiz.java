package com.example.rk.mynews.core;

import android.os.Message;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.rk.mynews.dao.MyNewsDB;
import com.example.rk.mynews.model.GaG;
import com.example.rk.mynews.model.News;
import com.example.rk.mynews.model.TYPE;
import com.example.rk.mynews.ui.activity.MainActivity;
import com.example.rk.mynews.ui.adapter.GagAdapter;
import com.example.rk.mynews.ui.adapter.NewsListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by RK on 2015/8/26.
 */
public class GaGBiz {
    /**
     * ���ַ���������News
     * @param s
     * @return
     */
    public static GaG gsonGaG(String s){
        GaG gag=new GaG();
        gag.data=new ArrayList<GaG.data>();
        gag.paging=new GaG.Paging();
        try {
            JSONObject object=new JSONObject(s);
            JSONArray array=object.getJSONArray("data");
            String paging1=object.getJSONObject("paging").getString("next");

            gag.paging.next=paging1;
            for (int i=0;i<array.length();i++){
                JSONObject jsonObject=array.getJSONObject(i);
                String caption=jsonObject.getString("caption");
                JSONObject images=jsonObject.getJSONObject("images");
                String normal=images.getString("normal");
                String large=images.getString("large");
                GaG.data data=new GaG.data();
                data.caption=caption;
                GaG.images images1=new GaG.images();
                images1.normal=normal;
                images1.large=large;
                data.images=images1;
                gag.data.add(data);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return gag;
    }

    /**
     * ѡ�����ݵļ��غ͸��·�ʽ
     */
    private static int way;
    private static String mPage;

    public static void getNews(GagAdapter adapter,int wayq,String mPageq){
        way=wayq;
        mPage=mPageq;
        if (way== TYPE.FROM_DB_REPLACE){
            loadNewsFromDB(adapter);
        }else if (way==TYPE.FROM_NET_REPLACE){
            getNewsFromNet(adapter);
        }else if (way==TYPE.FROM_NET_ADD){
            getNewsFromNet(adapter);
        }
    }

    /**
     * �������������
     * @param adapter
     */
    private static void getNewsFromNet(final GagAdapter adapter){
        String path = "http://infinigag-us.aws.af.cm/hot/"+mPage;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, path,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        /*�õ����ݣ��������ݣ���������ɹ���Ϣ������arrayAdapter*/
                        Log.i("onResponse", "  " + s);
                        GaG gag=gsonGaG(s);
                        MyApplication.mPage=gag.paging.next;
                        //**�ж����ݵĸ��·�ʽ
                        if (way==TYPE.FROM_NET_REPLACE||way==TYPE.FROM_DB_REPLACE){
                            adapter.replace(gag);
                            adapter.notifyDataSetChanged();
                        }else if (way==TYPE.FROM_NET_ADD){
                            if (gag.data.size()==0){
                                //loadNewsFromDB(adapter, size, time);
                            }
                            adapter.add(gag);
                            adapter.notifyDataSetChanged();
                        }


                        Message msg=new Message();
                        msg.what=TYPE.SUCCESS;
                        msg.arg1=gag.data.size();
                        MainActivity.mHandler.sendMessage(msg);
                        MyNewsDB.getInstance(MyApplication.getContext()).saveGaG(gag);//���浽���ݿ���

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
         *IMAGE_TIMEOUT_MS,  //  ��ʱʱ��
         *IMAGE_MAX_RETRIES,  //  ��ʱ��retry����
         *IMAGE_BACKOFF_MULT));// ��ʱ����
         */
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(2000,1,1f));
        MyApplication.getRequestQueue().add(stringRequest);
    }
    /**
     * �����ݿ��ж�ȡ����
     * @param arrayAdapter
     */
    private static void loadNewsFromDB(GagAdapter arrayAdapter){
        GaG gaG=MyNewsDB.getInstance(MyApplication.getContext()).loadGaG();
        if (gaG!=null) {
            arrayAdapter.replace(gaG);
            arrayAdapter.notifyDataSetChanged();
        }
        else {
            getNewsFromNet(arrayAdapter);
        }
    }
}
