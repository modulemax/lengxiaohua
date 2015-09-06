package com.example.rk.mynews.ui.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rk.mynews.R;


/**
 * Created by RK on 2015/8/23.
 */
public class FeedsAdapter extends CursorAdapter {
    private LayoutInflater mLayoutInflater;
    public FeedsAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
        mLayoutInflater = LayoutInflater.from(context);

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return mLayoutInflater.inflate(R.layout.joke_item_list,parent,false);
    }

    @Override
    public Object getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = getViewHolder(view);
        holder.author.setText(cursor.getString(cursor.getColumnIndex("author")));
        holder.content.setText(cursor.getString(cursor.getColumnIndex("content")));
    }
    private ViewHolder getViewHolder(final View view){
        ViewHolder viewHolder= (ViewHolder) view.getTag();
        if (viewHolder==null){
            viewHolder=new ViewHolder(view);
            view.setTag(viewHolder);
        }
        return viewHolder;
    }


    public static class ViewHolder  {
        TextView author;
        TextView content;
        ImageView network_ImageView;

        // each data item is just a string in this case
        public ViewHolder(View view) {
            author = (TextView) view.findViewById(R.id.author);
            content = (TextView) view.findViewById(R.id.content);
            network_ImageView = (ImageView) view.findViewById(R.id.network_ImageView);

        }
    }
}
