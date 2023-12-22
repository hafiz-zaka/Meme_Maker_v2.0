package com.mememaker.mememakerpro.creatememe.model;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;

import java.io.Serializable;

public class PopularModel{
    private String title;

    public PopularModel() {
    }




    public int getFav() {
        return fav;
    }

    public void setFav(int fav) {
        this.fav = fav;
    }

    private int fav= View.GONE;

    public PopularModel(String title, Uri imageUri) {
        this.title = title;
        this.imageUri = imageUri;

    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    private Uri imageUri;

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




    @BindingAdapter({"android:src2"})
    public static void setImageViewResource(ImageView imageView, Uri resource) {
        imageView.setImageURI(resource);
    }
}
