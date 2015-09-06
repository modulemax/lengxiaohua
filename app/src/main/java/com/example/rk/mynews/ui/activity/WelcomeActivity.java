package com.example.rk.mynews.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.develop.utils.app.AppUtils;
import android.net.ConnectivityManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rk.mynews.R;
import com.example.rk.mynews.core.MyApplication;
import com.example.rk.mynews.utils.NetWorkUtils;

import java.util.Random;

public class WelcomeActivity extends ActionBarActivity {

    ImageView mBackgroundImage;
    TextView mTitleText;
    TextView mVersionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        View decorView = getWindow().getDecorView();
        int uiOptions = decorView.getSystemUiVisibility();
        int newUiOptions = uiOptions;
        //隐藏导航栏
        newUiOptions |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(newUiOptions);

        mBackgroundImage = (ImageView) findViewById(R.id.image_background);
        Random random = new Random();
        int num = random.nextInt(4);


        mBackgroundImage.animate().alpha(0f).setDuration(1000).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                //动画结束时打开首页
                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));

                finish();
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }
        }).start();

        if (NetWorkUtils.isConnected(this)){
            MyApplication.setNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        }else {
            MyApplication.setNetworkInfo(-1);
            Toast.makeText(this,"无网络",Toast.LENGTH_SHORT).show();

        }
        MyApplication.setONLY_DOWN_BY_WIFI(getSharedPreferences(this.getPackageName() + "_preferences",MODE_WORLD_READABLE)
                .getBoolean(getResources().getString(R.string.setting_key_only_wifi),false));

        mTitleText = (TextView) findViewById(R.id.title_text);
        mTitleText.setText(AppUtils.getAppName(WelcomeActivity.this));
        mVersionText = (TextView) findViewById(R.id.version_text);
        mVersionText.setText(AppUtils.getAppName(WelcomeActivity.this));
    }

    @Override
    public void finish() {
        mBackgroundImage.destroyDrawingCache();
        super.finish();
    }
}
