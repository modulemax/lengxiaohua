package com.example.rk.mynews.utils;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.develop.utils.InnerContacts;
import android.develop.utils.log.LogUtils;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;

/**
 * 网络 工具类<Br>
 * 内部已经封装了打印功能,只需要把DEBUG参数改为true即可<br>
 * 如果需要更换tag可以直接更改,默认为KEZHUANG
 * 
 * @author KEZHUANG
 *
 */
public class NetWorkUtils {
	/**
	 * Log的开关<br>
	 * true为开启<br>
	 * false为关闭<br>
	 */
	public static boolean DEBUG = InnerContacts.DEFAULT_DEBUG;

	/**
	 * Log 输出标签
	 */
	public static String TAG = InnerContacts.DEFAULT_TAG;

	/**
	 * 接受网络状态的广播Action
	 */
	public static final String NET_BROADCAST_ACTION = "com.network.state.action";
	
	public static final String NET_STATE_NAME = "network_state";
	
	/**
	 * 实时更新网络状态<br>
	 * -1为网络无连接<br>
	 * 1为WIFI<br>
	 * 2为移动网络<br>
	 */
	public static int CURRENT_NETWORK_STATE = -1;
	
	/**
	 * 判断网络是否连接
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isConnected(Context context) {

		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (null != connectivity) {

			NetworkInfo info = connectivity.getActiveNetworkInfo();
			if (null != info && info.isConnected()) {
				if (info.getState() == NetworkInfo.State.CONNECTED) {
					LogUtils.inner_i("当前网络可用", DEBUG);
					return true;
				}
			}
		}
		LogUtils.inner_i("当前网络不可用", DEBUG);
		return false;
	}
	
	/**
	 * 判断是否是wifi连接
	 */
	public static boolean isWifi(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (cm == null) {
			LogUtils.inner_i("当前网络----->不可用", DEBUG);
			return false;
		}
		boolean isWifi = cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;
		if (isWifi)
			LogUtils.inner_i("当前网络----->WIFI环境", DEBUG);
		else
			LogUtils.inner_i("当前网络----->非WIFI环境", DEBUG);

		return isWifi;

	}

	/**
	 * 打开网络设置界面
	 */
	public static void openSetting(Activity activity) {
		Intent intent = new Intent("/");
		ComponentName cm = new ComponentName("com.android.settings",
				"com.android.settings.WirelessSettings");
		intent.setComponent(cm);
		intent.setAction("android.intent.action.VIEW");
		activity.startActivityForResult(intent, 0);
	}
	
	/**
	 * 开启服务,实时监听网络变化
	 * 需要自己在清单文件中配置服务<br>
	 * 然后把对应的Action传入<br>
	 * 服务类:android.develop.utils.net.NetService
	 */
	public static void startNetService(Context context,String action){
		//注册广播
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(NET_BROADCAST_ACTION);
		context.registerReceiver(mReceiver, intentFilter);
		//开启服务
		Intent intent = new Intent();
		LogUtils.inner_d("开启网络监听服务", DEBUG);
		intent.setAction(action);
		intent.setPackage(context.getPackageName());
		context.bindService(intent, new ServiceConnection() {

			@Override
			public void onServiceDisconnected(ComponentName name) {

			}

			@Override
			public void onServiceConnected(ComponentName name, IBinder service) {

			}
		}, Context.BIND_AUTO_CREATE);
	}
	//接受服务上发过来的广播
	private static BroadcastReceiver mReceiver = new BroadcastReceiver(){

		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent!=null){
				CURRENT_NETWORK_STATE = (Integer) intent.getExtras().get(NET_STATE_NAME);
				switch (CURRENT_NETWORK_STATE) {
				case -1:
					LogUtils.inner_d("网络更改为 无网络  CURRENT_NETWORK_STATE ="+CURRENT_NETWORK_STATE  , DEBUG);
					break;
				case 1:
					LogUtils.inner_d("网络更改为 WIFI网络  CURRENT_NETWORK_STATE="+CURRENT_NETWORK_STATE, DEBUG);
					break;
				case 2:
					LogUtils.inner_d("网络更改为 移动网络  CURRENT_NETWORK_STATE ="+CURRENT_NETWORK_STATE, DEBUG);
					break;

				default:
					break;
				}
			}
		}
		
	};
}
