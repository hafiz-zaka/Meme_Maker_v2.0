package com.mememaker.mememakerpro.creatememe.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.mememaker.mememakerpro.creatememe.R;
import com.mememaker.mememakerpro.creatememe.databinding.ActivityAdvanceFeatureBinding;

public class AdvanceFeature extends AppCompatActivity {

    int retrieveImage;
    ActivityAdvanceFeatureBinding advanceFeatureBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        advanceFeatureBinding= DataBindingUtil.setContentView(this,R.layout.activity_advance_feature);


        retrieveImage=getIntent().getIntExtra("image",0);

        advanceFeatureBinding.temp.setImageResource(retrieveImage);

    }
}