package com.mememaker.mememakerpro.creatememe.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.mememaker.mememakerpro.creatememe.R;
import com.mememaker.mememakerpro.creatememe.SharePrefConfig;
import com.mememaker.mememakerpro.creatememe.activity.MainDashboard;
import com.mememaker.mememakerpro.creatememe.databinding.ShowNewItemBinding;
import com.mememaker.mememakerpro.creatememe.model.MainModel;
import com.mememaker.mememakerpro.creatememe.myinterface.MainItemClick;

import java.util.ArrayList;
/*
 *   Developer Name : Rafaqat Mehmood
 *   Whatsapp Number : 0310-1025532
 *   Designation : Sr.Android Developer
 */
public class NewAdapter extends RecyclerView.Adapter<NewAdapter.NewHolder> {
    Context context;
     ArrayList<MainModel> list;
     MainItemClick mainItemClick;

    public NewAdapter(Context context, ArrayList<MainModel> list,MainItemClick mainItemClick) {
        this.context = context;
        this.list = list;
        this.mainItemClick=mainItemClick;

    }
    @NonNull
    @Override
    public NewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ShowNewItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.show_new_item, parent, false);
        return new NewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull NewHolder holder, int position) {

        MainModel model=list.get(position);

        holder.itemBinding.fav.setVisibility(model.getFav());
        holder.itemBinding.unFav.setVisibility(model.getUnFav());
        holder.itemBinding.setItem(model);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public  class NewHolder extends RecyclerView.ViewHolder {
        public ShowNewItemBinding itemBinding;
        public NewHolder(ShowNewItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding=itemBinding;

            itemBinding.selectedImage.setOnClickListener(view -> {
                mainItemClick.main_item_image_click(list.get(getAdapterPosition()));
            });

            itemBinding.fav.setOnClickListener(view -> {
                mainItemClick.itemFav(list.get(getAdapterPosition()),itemBinding.fav,itemBinding.unFav,getAdapterPosition());
            });
            itemBinding.unFav.setOnClickListener(view -> {
                mainItemClick.itemFav(list.get(getAdapterPosition()),itemBinding.fav,itemBinding.unFav,getAdapterPosition());
            });
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

}
