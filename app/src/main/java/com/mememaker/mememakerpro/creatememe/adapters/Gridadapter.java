package com.mememaker.mememakerpro.creatememe.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mememaker.mememakerpro.creatememe.R;
import com.mememaker.mememakerpro.creatememe.ShowImage;

import java.io.File;
import java.util.List;

public class Gridadapter extends BaseAdapter {
    Context context;
    List logos;
    LayoutInflater inflter;

    public Gridadapter(Context applicationContext, List logos) {
        this.context = applicationContext;
        this.logos = logos;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return logos.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.imagegrid, null); // inflate the layout
        ImageView imageView = (ImageView) view.findViewById(R.id.image); // get the reference of ImageView
        //Collections.reverse(logos);
        Glide.with(context)
                .load(logos.get(i))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imageView);
        Log.e("Image", logos.get(i) + "");
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open_image(logos.get(i) + "");
                //new MainActivity().Toast(context, "Image Clicked");
            }
        });
        return view;
    }

    public void open_image(String path) {
        File file = new File(path);
        if (file.exists()) {
            Intent intent = new Intent(context, ShowImage.class);
            intent.putExtra("imagepath", path);
            context.startActivity(intent);
        }
    }
}