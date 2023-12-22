package com.mememaker.mememakerpro.creatememe.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.mememaker.mememakerpro.creatememe.R;
import com.mememaker.mememakerpro.creatememe.databinding.MenuItemBinding;
import com.mememaker.mememakerpro.creatememe.model.MenuItem;
import com.mememaker.mememakerpro.creatememe.myinterface.MenuItemClick;

import java.util.ArrayList;
/*
 *   Developer Name : Rafaqat Mehmood
 *   Whatsapp Number : 0310-1025532
 *   Designation : Sr.Android Developer
 */
public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MyHolder> {

    ArrayList<MenuItem> list;
    Context context;
    MenuItemClick menuItemClick;

    public MenuAdapter(ArrayList<MenuItem> list, Context context,MenuItemClick menuItemClick) {
        this.list = list;
        this.context = context;
        this.menuItemClick=menuItemClick;
    }



    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MenuItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.menu_item, parent, false);

        return new MyHolder(binding);
    }
    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        MenuItem model=list.get(position);


//        String color= "#CCCCCC";
//        if (position % 2==1)
//        {
//            color= "#EEEEEE";
//        }
//        holder.menuItemBinding.card.setBackgroundColor(Color.parseColor(color));

        holder.menuItemBinding.setItem(model);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        MenuItemBinding menuItemBinding;
        public MyHolder(MenuItemBinding binding) {
            super(binding.getRoot());
            this.menuItemBinding=binding;

            binding.card.setOnClickListener(view -> {
                menuItemClick.itemPos(list.get(getAdapterPosition()),0);
            });
        }
    }
}
