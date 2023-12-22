package com.mememaker.mememakerpro.creatememe.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.mememaker.mememakerpro.creatememe.R;
import com.mememaker.mememakerpro.creatememe.databinding.CategoriesItemBinding;
import com.mememaker.mememakerpro.creatememe.databinding.DisplayCategoriesItemBinding;
import com.mememaker.mememakerpro.creatememe.model.MenuItem;
import com.mememaker.mememakerpro.creatememe.myinterface.MenuItemClick;

import java.util.ArrayList;

/*
 *   Developer Name : Rafaqat Mehmood
 *   Whatsapp Number : 0310-1025532
 *   Designation : Sr.Android Developer
 */
public class CategoriesDisplayAdapter extends RecyclerView.Adapter<CategoriesDisplayAdapter.MyHolder> {

    ArrayList<MenuItem> list;
    Context context;
    MenuItemClick menuItemClick;
    private Integer selectedItemPosition = -1;

    public CategoriesDisplayAdapter(ArrayList<MenuItem> list, Context context, MenuItemClick menuItemClick) {
        this.list = list;
        this.context = context;
        this.menuItemClick = menuItemClick;
    }


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DisplayCategoriesItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.display_categories_item, parent, false);

        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        MenuItem model = list.get(position);

        holder.displayCategoriesItemBinding.setItem(model);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        DisplayCategoriesItemBinding displayCategoriesItemBinding;

        public MyHolder(DisplayCategoriesItemBinding binding) {
            super(binding.getRoot());
            this.displayCategoriesItemBinding = binding;

            binding.card.setOnClickListener(view -> {
                try {
                    menuItemClick.itemPos(list.get(getAdapterPosition()), getAdapterPosition());
                }
                catch (Exception e)
                {
                    Log.i("rafaqat", "MyHolder: "+e.getMessage());
                }

            });
        }
    }
}
