package com.mememaker.mememakerpro.creatememe.adapters;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mememaker.mememakerpro.creatememe.R;
import com.mememaker.mememakerpro.creatememe.databinding.ShowNewItem2Binding;
import com.mememaker.mememakerpro.creatememe.databinding.ShowNewItemBinding;
import com.mememaker.mememakerpro.creatememe.model.MainModel;
import com.mememaker.mememakerpro.creatememe.model.PopularModel;
import com.mememaker.mememakerpro.creatememe.myinterface.MainItemClick;
import com.mememaker.mememakerpro.creatememe.myinterface.MainItemClick2;

import java.util.ArrayList;

/*
 *   Developer Name : Rafaqat Mehmood
 *   Whatsapp Number : 0310-1025532
 *   Designation : Sr.Android Developer
 */
public class NewAdapter2 extends RecyclerView.Adapter<NewAdapter2.NewHolder2>  {
    Context context;
     ArrayList<PopularModel> list;
     MainItemClick2 mainItemClick;

    public NewAdapter2(Context context, ArrayList<PopularModel> list,MainItemClick2 mainItemClick) {
        this.context = context;
        this.list = list;
        this.mainItemClick=mainItemClick;


    }
    @NonNull
    @Override
    public NewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ShowNewItem2Binding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.show_new_item2, parent, false);
        return new NewHolder2(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull NewHolder2 holder, int position) {

        PopularModel model=list.get(position);
        holder.itemBinding.fav.setVisibility(model.getFav());
        holder.itemBinding.unFav.setVisibility(model.getUnFav());

            Glide.with(context)
                    .load(model.getImageUri())
                    .placeholder(R.drawable.loading_img)
                    .into(holder.itemBinding.selectedImage2);


            holder.itemBinding.setItem(model);

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public  class NewHolder2 extends RecyclerView.ViewHolder {
        public ShowNewItem2Binding itemBinding;
        public NewHolder2(ShowNewItem2Binding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding=itemBinding;

            itemBinding.selectedImage2.setOnClickListener(view -> {
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
