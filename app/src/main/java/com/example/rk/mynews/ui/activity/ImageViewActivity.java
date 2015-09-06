package com.example.rk.mynews.ui.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.example.rk.mynews.R;
import com.example.rk.mynews.core.BitmapWorkerTask;
import com.example.rk.mynews.core.MyApplication;
import com.example.rk.mynews.dao.BitmapDiskLrucache;
import com.example.rk.mynews.ui.view.LoadProgress;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;


/**
 * Created by storm on 14-4-15.
 */
public class ImageViewActivity extends AppCompatActivity {
    public static final String IMAGE_URL = "image_url";

    Toolbar toolbar;
    PhotoView photoView;
    LoadProgress loadProgress;

    private PhotoViewAttacher mAttacher;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imageview);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setNavigationIcon(R.drawable.ic_back_holo_light);
        setSupportActionBar(toolbar);
        loadProgress = (LoadProgress) findViewById(R.id.progress_img);
        photoView = (PhotoView) findViewById(R.id.photoView);
        mAttacher = new PhotoViewAttacher(photoView);
        mAttacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                finish();
            }
        });
        String imageUrl = getIntent().getStringExtra(IMAGE_URL);
        BitmapDiskLrucache diskLrucache = BitmapDiskLrucache.getInstance(MyApplication.getContext());
        InputStream is = diskLrucache.getDataFromDiskLrucache(imageUrl);
        if (is != null) {
            photoView.setImageBitmap(
                    BitmapFactory.decodeStream(is));
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheOnDisc(true)
                    .considerExifParams(true).build();
            ImageLoader.getInstance().displayImage(imageUrl, photoView, options, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    loadProgress.setVisibility(View.GONE);
                    mAttacher.update();
                }
            }, new ImageLoadingProgressListener() {
                @Override
                public void onProgressUpdate(String imageUri, View view, int current, int total) {
                     loadProgress.setSweepAngle(360 * current / total);
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAttacher != null) {
            mAttacher.cleanup();
        }
    }
}
