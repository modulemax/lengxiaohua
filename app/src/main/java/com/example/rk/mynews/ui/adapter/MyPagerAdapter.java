package com.example.rk.mynews.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;


import java.util.List;


/**
 * Created by RK on 2015/7/26.
 */
public class MyPagerAdapter extends FragmentPagerAdapter {
    List<Fragment> items;
    FragmentManager fm;
    String[] title=new String[]{"9GAG","JOKE","NEWS"};
    public MyPagerAdapter(FragmentManager fm,List<Fragment> items) {
        super(fm);
        this.items=items;
        this.fm=fm;
    }



    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        return super.instantiateItem(container, position);
    }


    @Override
    public Fragment getItem(int position) {

        return items.get(position);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];
    }
}
