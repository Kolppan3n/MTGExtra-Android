package com.example.co1200679.mtgextra;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


public class GridAdapter extends BaseAdapter {

    private Context context;
    private List<Integer> thumbnails;
    private static final int MAX_WIDTH = 1024;
    private static final int MAX_HEIGHT = 768;
    private int mode;
    private int height = 129;
    private int width = 91;
    private int maxSize;
    private float density;


    public GridAdapter(Context context, int mode) {
        this.context = context;
        this.mode = mode;
        getSizes();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView tempView;
        if (convertView == null) {
            tempView = new ImageView(context);
            tempView.setLayoutParams(new GridView.LayoutParams(width, height));
        } else {
            tempView = (ImageView) convertView;
        }
        int id = thumbnails.get(position);
        Picasso.with(context)
                .load(id)
                .transform(new BitmapTransform(MAX_WIDTH, MAX_HEIGHT))
                .skipMemoryCache()
                .resize(maxSize, maxSize)
                .centerInside()
                .into(tempView);
        return tempView;
    }

    @Override
    public int getCount() {
        loadThumbnails();
        return thumbnails.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private void loadThumbnails() {
        thumbnails = new ArrayList<Integer>();
        Field[] ID_Fields = R.drawable.class.getFields();
        for (Field f : ID_Fields) {
            try {
                if (f.getName().contains("token_")) {
                    thumbnails.add(context.getResources().getIdentifier("@drawable/" + f.getName().toString(), "drawable", context.getPackageName()));
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }

    private void getSizes() {
        switch (mode) {
            case 1: {
                width = 286;
                height = 200;
                break;
            }
            case 2: {
                width = 286;
                height = 410;
                break;
            }
            case 3: {
                width = 91;
                height = 129;
                break;
            }
            default:
                break;
        }

        maxSize = (int) Math.ceil(Math.sqrt(MAX_WIDTH * MAX_HEIGHT));
        density = context.getResources().getDisplayMetrics().density;
        height *= density;
        width *= density;
    }
}
