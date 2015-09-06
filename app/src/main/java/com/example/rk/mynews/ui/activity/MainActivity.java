package com.example.rk.mynews.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Toast;

import com.example.rk.mynews.R;
import com.example.rk.mynews.dao.BitmapDiskLrucache;
import com.example.rk.mynews.model.TYPE;
import com.example.rk.mynews.ui.adapter.MyPagerAdapter;
import com.example.rk.mynews.ui.fragment.JokeListFragment;
import com.example.rk.mynews.ui.fragment.NewsListFragment;
import com.example.rk.mynews.ui.fragment.gagFrament;
import com.example.rk.mynews.ui.view.PagerSlidingTabStrip;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements
        JokeListFragment.OnFragmentInteractionListener,
        NewsListFragment.OnFragmentInteractionListener,
        gagFrament.OnFragmentInteractionListener{

    List<Fragment> items;
    List<SwipeRefreshLayout> mSwipeRefreshLayoutList = new ArrayList<SwipeRefreshLayout>();
    public static Handler mHandler;
    public DrawerLayout drawerLayout;
    private Toolbar toolbar;
    //标识是否点击过一次back退出
    private boolean mIsExit = false;
    final static int TIME_TO_EXIT=2000;
    PagerSlidingTabStrip tabs;
    View content;
    public ViewPager pager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        content=findViewById(R.id.draw_fragment);
        initToolbar();
        initFragment();
        initHandler();
        /*为HttpURLConnection增加了一个response cache(这是一个很好的减少http请求次数的机制*/
        //enableHttpResponseCache();
    }

    /**
     * 监听Jokelist 网络刷新信息
     */
    private void initHandler(){
        mHandler=new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case TYPE.SUCCESS:
                        for (SwipeRefreshLayout swipeRefreshLayout : mSwipeRefreshLayoutList)
                            swipeRefreshLayout.setRefreshing(false);
                        if (msg.arg1!=0)
                        //Toast.makeText(MainActivity.this, "为你更新了" + msg.arg1, Toast.LENGTH_SHORT).show();
                        break;
                    case TYPE.ERROR:
                        //sendBroadcast(new Intent("android.net.conn.CONNECTIVITY_CHANGE"));

                        //Toast.makeText(MainActivity.this, "刷新失败", Toast.LENGTH_SHORT).show();
                        for (SwipeRefreshLayout swipeRefreshLayout : mSwipeRefreshLayoutList)
                            swipeRefreshLayout.setRefreshing(false);
                        break;
                    case 222:Toast.makeText(MainActivity.this, "加载更多", Toast.LENGTH_SHORT).show();
                        break;
                    case 789:mIsExit=false;
                        break;
                }
            }
        };
    }

    public void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitle(R.string.app_name);
        drawerLayout = (DrawerLayout) findViewById(R.id.draw_layout);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, toolbar, R.string.open, R.string.close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                super.onDrawerStateChanged(newState);
            }
        };
        actionBarDrawerToggle.syncState();
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

    }


    public void initFragment() {

        items = new ArrayList<Fragment>();
        items.add(gagFrament.newInstance(2));
        items.add(JokeListFragment.newInstance(TYPE.JOKE));
        items.add(NewsListFragment.newInstance(1));

        pager = (ViewPager) findViewById(R.id.pager);
        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager(), items);
        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(1);
        // Bind the tabs to the ViewPager
        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        //如果只有一页，就不显示导航
        if (items.size()==1)tabs.setVisibility(View.GONE);
        tabs.setViewPager(pager);



    }
    /**
     * 根据滑动方向设置ToolBar的显隐
     * @param scrollState   滑动方向
     */
    private boolean onanimate=false;
    private boolean istabShow=true;
    private int tabBarHeiget=30;
    public void onUpOrCancelMotionEvent(AbsListView view, int scrollState) {
        //上 0
//        if (scrollState ==1&&!onanimate&&!istabShow){
//            Log.i("ok","ppppppppppppppppp"+scrollState);
//                tabs.animate().translationY(0).setDuration(300).setListener(new AnimatorListenerAdapter() {
//                    @Override
//                    public void onAnimationStart(Animator animation) {
//                        onanimate=true;
//                        super.onAnimationStart(animation);
//                    }
//
//                    @Override
//                    public void onAnimationEnd(Animator animation) {
//                        super.onAnimationEnd(animation);
//                        onanimate=false;
//                        istabShow=true;
//                        //content.requestLayout();
//                        tabs.setVisibility(View.VISIBLE);
//
//
//                    }
//                });
//
//        } else if (scrollState == 0&&!onanimate&&istabShow){
//            Log.i("ok","ppppppppppppppppp"+scrollState);
//            tabs.animate().translationY(-tabBarHeiget).setDuration(300).setListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationStart(Animator animation) {
//                    onanimate=true;
//                    super.onAnimationStart(animation);
//                    tabs.setVisibility(View.GONE);
//                }
//
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    super.onAnimationEnd(animation);
//                    onanimate=false;
//                    istabShow=false;
//                    //content.requestLayout();
//                }
//            });
//
//        }
    }

    /**
     * 为HttpURLConnection增加了一个response cache(这是一个很好的减少http请求次数的机制
     */
    private void enableHttpResponseCache() {
        try {
            long httpCacheSize = 10 * 1024 * 1024; // 10 MiB
            File httpCacheDir = new File(getCacheDir(), "http");
            Class.forName("android.net.http.HttpResponseCache")
                    .getMethod("install", File.class, long.class)
                    .invoke(null, httpCacheDir, httpCacheSize);
        } catch (Exception httpResponseCacheNotAvailable) {
            Log.d("TAG", "HTTP response cache is unavailable.");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        switch (id){
            case R.id.action_settings:Intent intent=new Intent(MainActivity.this,SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.action_fresh:
                for (SwipeRefreshLayout swipeRefreshLayout : mSwipeRefreshLayoutList)
                    swipeRefreshLayout.setRefreshing(true);
                JokeListFragment jokeListFragment= (JokeListFragment) items.get(0);
                jokeListFragment.onRefresh();
                break;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(View view) {
        mSwipeRefreshLayoutList.add((SwipeRefreshLayout) view);
    }
    @Override
    public void onFragmentInteraction2(View view) {
        mSwipeRefreshLayoutList.add((SwipeRefreshLayout) view);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
    private void exit(){
        if (mIsExit){
            finish();
            System.exit(0);
        }
        else {
            mIsExit=true;
            Toast.makeText(MainActivity.this,"再按一次退出程序",Toast.LENGTH_SHORT).show();
            BitmapDiskLrucache.getInstance(this).fluchCache();
            mHandler.sendEmptyMessageDelayed(789, TIME_TO_EXIT);
        }
    }

    public ViewPager getViewPager(){
        return pager;
    }
    public DrawerLayout getDrawerLayout(){
        return drawerLayout;
    }
}
