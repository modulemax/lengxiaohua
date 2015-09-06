package com.example.rk.mynews.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.rk.mynews.R;
import com.example.rk.mynews.core.MyApplication;
import com.example.rk.mynews.model.Joke;
import com.example.rk.mynews.core.BitmapWorkerTask;
import com.example.rk.mynews.ui.activity.ImageViewActivity;
import com.example.rk.mynews.ui.view.LoadProgress;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

public class MyArrayAdapter extends ArrayAdapter<Joke> {
    private int resourceId;
    private static final Bitmap btimap = BitmapFactory.decodeResource(MyApplication.getContext().getResources(), R.drawable.load1);
    private List<Joke.detail> list;
    Context context;

    DisplayImageOptions options = new DisplayImageOptions.Builder().cacheOnDisc(true)
            .considerExifParams(true).build();
    public MyArrayAdapter(Context context, int resource) {
        super(context, resource);
        this.context=context;
        this.resourceId=resource;
        list=new ArrayList<Joke.detail>();
    }

    /**
     *3种更新数据的方式
     * @param joke
     */
    public void add(Joke joke){
        list.addAll(0,joke.detail);
    }
    public void addToLast(Joke joke){
        list.addAll(joke.detail);
    }

    public void replace(Joke joke){
        list.clear();
        list.addAll(joke.detail);
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        final ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);

            viewHolder = new ViewHolder();
            viewHolder.author = (TextView) view.findViewById(R.id.author);
            viewHolder.content = (TextView) view.findViewById(R.id.content);
            viewHolder.network_ImageView = (ImageView) view.findViewById(R.id.network_ImageView);

            viewHolder.mProgressBar= (LoadProgress) view.findViewById(R.id.progress_img);
            view.setTag(viewHolder); //  将ViewHolder 存储在View 中
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag(); //  重新获取ViewHolder
        }
        /**
         * 将数据帮定到ui上
         */
        viewHolder.author.setText(list.get(position).author);
        viewHolder.content.setText(list.get(position).content);
        if (!TextUtils.isEmpty(list.get(position).picUrl)
                &&(!MyApplication.isONLY_DOWN_BY_WIFI()||MyApplication.getNetworkInfo()== ConnectivityManager.TYPE_WIFI)){
            Log.i("img", "该joke含图");
            final String image_url=list.get(position).picUrl;
            viewHolder.network_ImageView.setVisibility(View.VISIBLE);
            viewHolder.network_ImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, ImageViewActivity.class);
                    intent.putExtra("image_url",image_url);
                    Log.i("onClick","");
                    context.startActivity(intent);
                }
            });
            BitmapWorkerTask.loadBitmap(list.get(position).picUrl, viewHolder.network_ImageView, context.getResources(), viewHolder.mProgressBar);

        }else {
            viewHolder.network_ImageView.setVisibility(View.GONE);
            viewHolder.mProgressBar.setVisibility(View.GONE);
        }

        return view;
    }

    class ViewHolder {
        TextView author;
        TextView content;
        ImageView network_ImageView;
        LoadProgress mProgressBar;
    }

}
