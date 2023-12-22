package com.mememaker.mememakerpro.creatememe.adapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mememaker.mememakerpro.creatememe.R;
import com.mememaker.mememakerpro.creatememe.model.ImageModel;
import com.mememaker.mememakerpro.creatememe.myinterface.MyViewListener;

import java.util.ArrayList;

/*
 *   Developer Name : Rafaqat Mehmood
 *   Whatsapp Number : 0310-1025532
 *   Designation : Sr.Android Developer
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private Context context;
    public static ArrayList<ImageModel> list;
    MyViewListener listener;



    public MyAdapter(Context context, ArrayList<ImageModel> list,MyViewListener listener) {
        this.context = context;
        this.list = list;
        this.listener=listener;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.mp3_item_show,parent,false)) ;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.textName.setText(list.get(position).getTitle());
        holder.imageSize.setText(getImgSize(list.get(position).getSize()));
        Glide.with(context)
                .load(list.get(position).getPath())
                .into(holder.imageView);





    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textName,imageSize,view;
        public CardView cardView;
        public ImageView imageView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView=itemView.findViewById(R.id.pdf_imageView);
            imageSize=itemView.findViewById(R.id.imageSize);
            view=itemView.findViewById(R.id.viewImage);
            textName=itemView.findViewById(R.id.pdf_textName);
//            cardView=itemView.findViewById(R.id.pdf_cardView);





//            view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    listener.viewImg(list.get(getAdapterPosition()).getPath());
//                }
//            });
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.viewImg(list.get(getAdapterPosition()));

                }
            });



        }
    }

public static String getImgSize(Double d)
{
    double value = 0.0;
    if (d==null)
    {
        return "0.0 B";
    }
    if (d>=(1000.0 * 1000.0 * 1000.0 * 1000.0))
    {
        value = d / (1000.0 * 1000.0 * 1000.0 * 1000.0);
        return String.format("%.0f", d) + " TB";
    }
    if (d >= (1000.0 * 1000.0 * 1000.0)) {
        value = d  / (1000.0 * 1000.0 * 1000.0);
        return String.format("%.0f", d) + " GB";
    }
    if (d >= (1000.0 * 1000.0)) {
        value = d  / (1000.0 * 1000.0);
        return String.format("%.0f", d) + " MB";
    }
    if (d >= 1000.0) {
        value = d / 1000.0;
        return String.format("%.0f", d) + " KB";
    }
    return String.format("%.0f", d) + " B";

}
}
