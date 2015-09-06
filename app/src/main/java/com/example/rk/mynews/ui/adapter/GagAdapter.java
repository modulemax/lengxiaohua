package com.example.rk.mynews.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rk.mynews.R;
import com.example.rk.mynews.core.BitmapWorkerTask;
import com.example.rk.mynews.model.GaG;
import com.example.rk.mynews.ui.activity.ImageViewActivity;
import com.example.rk.mynews.ui.view.LoadProgress;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RK on 2015/8/12.
 */
public class GagAdapter extends ArrayAdapter<GaG> {
    private int resourceId;
    public List<GaG.data> list;
    Context context;
    public GagAdapter(Context context, int resource) {
        super(context, resource);
        this.context=context;
        this.resourceId=resource;
        list=new ArrayList<GaG.data>();
    }
    public void add(GaG gag){
        list.addAll(gag.data);
    }
    public void replace(GaG gag){
        list.clear();
        list.addAll(gag.data);
    }


    @Override
    public int getCount() {
        return list.size();
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);

            viewHolder = new ViewHolder();
            viewHolder.caption = (TextView) view.findViewById(R.id.caption);
            viewHolder.news_img=(ImageView)view.findViewById(R.id.network_ImageView);
            viewHolder.progress_img= (LoadProgress) view.findViewById(R.id.progress_img);

            view.setTag(viewHolder); //  将ViewHolder 存储在View 中
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag(); //  重新获取ViewHolder
        }

        viewHolder.caption.setText(list.get(position).caption);

        viewHolder.news_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, ImageViewActivity.class);
                intent.putExtra("image_url",list.get(position).images.large);
                Log.i("onClick", "");
                context.startActivity(intent);
            }
        });
        BitmapWorkerTask.loadBitmap(list.get(position).images.normal, viewHolder.news_img, context.getResources(), viewHolder.progress_img);



        return view;
    }

    class ViewHolder {
        TextView caption;
        LoadProgress progress_img;
        ImageView news_img;

    }

}
