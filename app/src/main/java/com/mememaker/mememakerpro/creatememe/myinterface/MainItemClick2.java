package com.mememaker.mememakerpro.creatememe.myinterface;

import android.net.Uri;
import android.widget.ImageView;

import com.mememaker.mememakerpro.creatememe.model.MainModel;
import com.mememaker.mememakerpro.creatememe.model.PopularModel;

/*
 *   Developer Name : Rafaqat Mehmood
 *   Whatsapp Number : 0310-1025532
 *   Designation : Sr.Android Developer
 */
public interface MainItemClick2 {

    void main_item_image_click(PopularModel model);

    void itemFav(PopularModel model, ImageView fav,ImageView unFav,int position);
    void favItemClick(Uri model);


}
