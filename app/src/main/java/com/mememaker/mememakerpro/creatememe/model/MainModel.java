package com.mememaker.mememakerpro.creatememe.model;

import android.view.View;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;
/*
 *   Developer Name : Rafaqat Mehmood
 *   Whatsapp Number : 0310-1025532
 *   Designation : Sr.Android Developer
 */
public class MainModel {
    private String title;

    public MainModel() {
    }




    public int getFav() {
        return fav;
    }

    public void setFav(int fav) {
        this.fav = fav;
    }

    private int fav= View.GONE;

    public MainModel(String title, int template) {
        this.title = title;
        this.template = template;

    }
    public MainModel(int template) {
        this.template = template;


    }
    private int template;

    public int getUnFav() {
        return unFav;
    }

    public void setUnFav(int unFav) {
        this.unFav = unFav;
    }

    private int unFav= View.GONE;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTemplate() {
        return template;
    }

    public void setTemplate(int template) {
        this.template = template;
    }



    @BindingAdapter({"android:src2"})
    public static void setImageViewResource(ImageView imageView, int resource) {
        imageView.setImageResource(resource);
    }
}
