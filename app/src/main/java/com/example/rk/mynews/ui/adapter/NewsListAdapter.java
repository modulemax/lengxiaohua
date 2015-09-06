package com.example.rk.mynews.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.rk.mynews.R;
import com.example.rk.mynews.model.News;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by RK on 2015/8/12.
 */
public class NewsListAdapter extends ArrayAdapter<News> {
    private int resourceId;
    public List<News.detail> list;
    Context context;
    SimpleDateFormat format=new SimpleDateFormat("E kk点mm分");

    public NewsListAdapter(Context context, int resource) {
        super(context, resource);
        this.context=context;
        this.resourceId=resource;
        list=new ArrayList<News.detail>();
    }
    public void addtoLast(News news){
        list.addAll(news.detail);

    }
    public void replace(News news){
        list.clear();
        list.addAll(news.detail);
    }


    @Override
    public int getCount() {
        return list.size();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);

            viewHolder = new ViewHolder();
            viewHolder.source = (TextView) view.findViewById(R.id.source);
            viewHolder.behot_time = (TextView) view.findViewById(R.id.behot_time);
            viewHolder.title = (TextView) view.findViewById(R.id.title);

            view.setTag(viewHolder); //  将ViewHolder 存储在View 中
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag(); //  重新获取ViewHolder
        }

        String t=format.format(new Date(list.get(position).behot_time));
        viewHolder.source.setText(list.get(position).source+"");
        viewHolder.behot_time.setText(t);
        viewHolder.title.setText(list.get(position).title+"");

        return view;
    }

    class ViewHolder {
        TextView source;
        TextView behot_time;
        TextView title;

    }

}
