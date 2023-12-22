package com.mememaker.mememakerpro.creatememe.adapters;

import static com.mememaker.mememakerpro.creatememe.adapters.Font_Adapter.fontClick;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.mememaker.mememakerpro.creatememe.R;


public class Font_ViewHolder extends RecyclerView.ViewHolder {
   public TextView textName;
   public CardView cardView;
   public ImageView imageView;

    public Font_ViewHolder(@NonNull View itemView) {
        super(itemView);

        textName=itemView.findViewById(R.id.pdf_textName);
        cardView=itemView.findViewById(R.id.pdf_cardView);
        imageView=itemView.findViewById(R.id.pdf_imageView);


        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fontClick.font_click(getAdapterPosition());
            }
        });
    }
}
