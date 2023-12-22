package com.mememaker.mememakerpro.creatememe.myinterface;

import android.app.Activity;
import android.widget.ImageView;

import com.mememaker.mememakerpro.creatememe.activity.MainDashboard;
import com.mememaker.mememakerpro.creatememe.model.Fav;
import com.mememaker.mememakerpro.creatememe.model.MainModel;

/*
 *   Developer Name : Rafaqat Mehmood
 *   Whatsapp Number : 0310-1025532
 *   Designation : Sr.Android Developer
 */
public interface MainItemClick {

    void main_item_image_click(MainModel model);
    void favItemClick(Integer model);
    void itemFav(MainModel model, ImageView fav,ImageView unFav,int position);
}
