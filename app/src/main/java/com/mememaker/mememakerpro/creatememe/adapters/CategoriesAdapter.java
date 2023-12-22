package com.mememaker.mememakerpro.creatememe.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.mememaker.mememakerpro.creatememe.R;
import com.mememaker.mememakerpro.creatememe.databinding.CategoriesItemBinding;
import com.mememaker.mememakerpro.creatememe.databinding.MenuItemBinding;
import com.mememaker.mememakerpro.creatememe.model.MenuItem;
import com.mememaker.mememakerpro.creatememe.myinterface.MenuItemClick;

import java.util.ArrayList;

/*
 *   Developer Name : Rafaqat Mehmood
 *   Whatsapp Number : 0310-1025532
 *   Designation : Sr.Android Developer
 */
public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.MyHolder> {

    ArrayList<MenuItem> list;
    Context context;
    MenuItemClick menuItemClick;
    private Integer selectedItemPosition = -1;

    public CategoriesAdapter(ArrayList<MenuItem> list, Context context, MenuItemClick menuItemClick) {
        this.list = list;
        this.context = context;
        this.menuItemClick = menuItemClick;
    }


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CategoriesItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.categories_item, parent, false);

        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        MenuItem model = list.get(position);


//        String color= "#CCCCCC";
//        if (position % 2==1)
//        {
//            color= "#EEEEEE";
//        }
//        holder.menuItemBinding.card.setBackgroundColor(Color.parseColor(color));

        if (selectedItemPosition == position)
            holder.categoriesItemBinding.newName.setBackgroundResource(R.drawable.cat_item_selected_click_bg);
        else
            holder.categoriesItemBinding.newName.setBackgroundResource(R.drawable.cat_item_click_bg);
        holder.categoriesItemBinding.setItem(model);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        CategoriesItemBinding categoriesItemBinding;

        public MyHolder(CategoriesItemBinding binding) {
            super(binding.getRoot());
            this.categoriesItemBinding = binding;


            binding.card.setOnClickListener(view -> {
                try {
                    menuItemClick.itemPos(list.get(getAdapterPosition()), getAdapterPosition());
                    selectedItemPosition = getAdapterPosition();
                    notifyDataSetChanged();
                }
                catch (Exception e)
                {
                    Log.i("rafaqat", "MyHolder: "+e.getMessage());
                }

            });
        }
    }
}
