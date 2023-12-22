package com.mememaker.mememakerpro.creatememe.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.mememaker.mememakerpro.creatememe.R;
import com.mememaker.mememakerpro.creatememe.model.Font;

import java.util.ArrayList;

public class Spinner_Adapter extends BaseAdapter {
    ArrayList<Font> list;
    Context context;

    public Spinner_Adapter(Context context, ArrayList<Font> list) {
        this.list = list;
        this.context = context;

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView heading;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.spinner_speed_item, parent, false);

            heading = convertView.findViewById(R.id.heading);
            heading.setText(list.get(position).getName());

        }
        return convertView;
    }
}
