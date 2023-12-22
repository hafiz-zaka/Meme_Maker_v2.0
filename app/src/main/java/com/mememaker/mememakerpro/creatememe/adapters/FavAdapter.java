package com.mememaker.mememakerpro.creatememe.adapters;



import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mememaker.mememakerpro.creatememe.R;
import com.mememaker.mememakerpro.creatememe.databinding.ShowFavItemBinding;
import com.mememaker.mememakerpro.creatememe.databinding.ShowNewItemBinding;
import com.mememaker.mememakerpro.creatememe.model.Fav;
import com.mememaker.mememakerpro.creatememe.model.PopularModel;
import com.mememaker.mememakerpro.creatememe.myinterface.MainItemClick;
import com.mememaker.mememakerpro.creatememe.myinterface.MainItemClick2;

import java.util.ArrayList;

/*
 *   Developer Name : Rafaqat Mehmood
 *   Whatsapp Number : 0310-1025532
 *   Designation : Sr.Android Developer
 */
public class FavAdapter extends RecyclerView.Adapter<FavAdapter.NewHolder> {
    Context context;
    ArrayList<Uri> list;
    MainItemClick2 mainItemClick;
    public FavAdapter(Context context, ArrayList<Uri> list,MainItemClick2 mainItemClick) {
        this.context = context;
        this.list = list;
        this.mainItemClick=mainItemClick;

    }
    @Override
    public NewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ShowFavItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.show_fav_item, parent, false);
        return new NewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull NewHolder holder, int position) {

        Glide.with(context)
                .load(list.get(position))
                .placeholder(R.drawable.loading_img)
                .into(holder.itemBinding.selectedImage);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class NewHolder extends RecyclerView.ViewHolder {
        ShowFavItemBinding itemBinding;
        public NewHolder(ShowFavItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding=itemBinding;

            itemBinding.selectedImage.setOnClickListener(view -> {
                mainItemClick.favItemClick(list.get(getAdapterPosition()));
            });
        }
    }
}
