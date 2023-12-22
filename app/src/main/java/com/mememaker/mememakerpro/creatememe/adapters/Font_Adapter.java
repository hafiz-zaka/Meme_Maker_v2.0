package com.mememaker.mememakerpro.creatememe.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.mememaker.mememakerpro.creatememe.R;
import com.mememaker.mememakerpro.creatememe.model.Font;
import com.mememaker.mememakerpro.creatememe.myinterface.FontClick;

import java.util.ArrayList;

public class Font_Adapter extends RecyclerView.Adapter<Font_ViewHolder> {


    ArrayList<Font> list;
    Context context;
   public static FontClick fontClick;

    public Font_Adapter(ArrayList<Font> list, Context context, FontClick fontClick) {
        this.list = list;
        this.context = context;
        this.fontClick=fontClick;
    }

    @NonNull
    @Override
    public Font_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
              return new Font_ViewHolder(LayoutInflater.from(context).inflate(R.layout.font_item,parent,false)) ;

    }

    @Override
    public void onBindViewHolder(@NonNull Font_ViewHolder holder, int position) {
        Font model=list.get(position);
        holder.imageView.setImageResource(model.getColorImage());
        holder.textName.setText(model.getName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }




}
