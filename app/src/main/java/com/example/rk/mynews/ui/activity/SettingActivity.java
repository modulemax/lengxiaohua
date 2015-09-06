package com.example.rk.mynews.ui.activity;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.rk.mynews.R;
import com.example.rk.mynews.core.MyApplication;
import com.example.rk.mynews.dao.BitmapDiskLrucache;
import com.example.rk.mynews.ui.fragment.SettingsFragment;

public class SettingActivity extends ActionBarActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    private SettingsFragment mSettingsFragment;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitle("设置");
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back_holo_light));
        setSupportActionBar(toolbar);
        mSettingsFragment = new SettingsFragment();
        getFragmentManager().beginTransaction().replace(R.id.settings_activity, mSettingsFragment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        String clearcache=getResources().getString(R.string.setting_key_clear_cache);
        String only_wifi=getResources().getString(R.string.setting_key_only_wifi);
        String check_update=getResources().getString(R.string.setting_key_check_update);

        if (key==only_wifi){
            MyApplication.setONLY_DOWN_BY_WIFI(sharedPreferences.getBoolean(key, false));
            Log.i("key","true");
        }else if (key==check_update){

        }else if (key==clearcache){
            Toast.makeText(SettingActivity.this,"缓存清除成功",Toast.LENGTH_SHORT).show();

        }
    }
}
