package com.example.rk.mynews.ui.receive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.example.rk.mynews.core.MyApplication;

/**
 * Created by RK on 2015/8/10.
 */
public class NetworkReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info=connectivityManager.getActiveNetworkInfo();
        if (info!=null&&info.isConnected()){
            //Toast.makeText(context,info.getTypeName(),Toast.LENGTH_SHORT).show();
            MyApplication.setNetworkInfo(info.getType());
            }
        else {
            MyApplication.setNetworkInfo(-1);
            Toast.makeText(context,"无网络",Toast.LENGTH_SHORT).show();
        }
    }
}
