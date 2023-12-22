package com.mememaker.mememakerpro.creatememe.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.mememaker.mememakerpro.creatememe.R;
import com.mememaker.mememakerpro.creatememe.databinding.ShowNewItemBinding;
import com.mememaker.mememakerpro.creatememe.databinding.StickerItemBinding;
import com.mememaker.mememakerpro.creatememe.model.StickerItem;
import com.mememaker.mememakerpro.creatememe.myinterface.StickerItemClick;

import java.util.ArrayList;
/*
 *   Developer Name : Rafaqat Mehmood
 *   Whatsapp Number : 0310-1025532
 *   Designation : Sr.Android Developer
 */
public class StickerAdapter extends RecyclerView.Adapter<StickerAdapter.NewHolder> {
    Context context;
    ArrayList<StickerItem> list;
    StickerItemClick stickerItemClick;

    public StickerAdapter(Context context, ArrayList<StickerItem> list,StickerItemClick stickerItemClick) {
        this.context = context;
        this.list = list;
        this.stickerItemClick=stickerItemClick;


    }




    @Override
    public NewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        StickerItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.sticker_item, parent, false);

        return new NewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull NewHolder holder, int position) {

        StickerItem model=list.get(position);
        holder.itemBinding.setItem(model);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class NewHolder extends RecyclerView.ViewHolder {
        StickerItemBinding itemBinding;
        public NewHolder(StickerItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding=itemBinding;

            itemBinding.selectedImage.setOnClickListener(view -> {
                stickerItemClick.itemPos(list.get(getAdapterPosition()));
            });



        }
    }
}
