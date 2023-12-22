package com.mememaker.mememakerpro.creatememe;

import static android.content.Context.CONNECTIVITY_SERVICE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.ResponseInfo;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.mememaker.mememakerpro.creatememe.activity.MainDashboard;
import com.mememaker.mememakerpro.creatememe.adsManager.AdId;
import com.mememaker.mememakerpro.creatememe.constant.Constant;

import java.io.IOException;
import java.util.Locale;

public class Common {
    public static InterstitialAd mInterstitialAd = null;
    public static int load=0;
    public static void load_IA(Activity context,String adID) {
        String str2;
        if (mInterstitialAd != null) {
            mInterstitialAd = null;
        }
        AdRequest adRequest = new AdRequest.Builder().build();

        if (BuildConfig.DEBUG) {
//            str2= AdId.AD_MOB_INTERSTITIAL_TEST;
            str2= adID;
        } else {
//            str2=AdId.AD_MOB_INTERSTITIAL_REAL;
            str2=adID;
        }

        InterstitialAd.load(context, str2, adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                super.onAdLoaded(interstitialAd);
                mInterstitialAd = interstitialAd;
                Log.e(Constant.MY_TAG, "onInterstitial_Ad_Loaded:");

            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                mInterstitialAd = null;
            }
        });



    }
    public static void show_IA(Activity context, Class cl, int i,String check){
        if (mInterstitialAd != null) {
                    mInterstitialAd.show(context);
                    mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                                mInterstitialAd = null;
                                if (check.equals("MainDashboard")) {
                                    startAct(context, cl, i);
                                }
                                else if (check.equals("EditActivity"))
                                {

                                }
                            load = 2;
                            }

                        @Override
                        public void onAdShowedFullScreenContent() {
                            super.onAdShowedFullScreenContent();
                            mInterstitialAd = null;
                        }
                    });
                }

    }
    public static void startAct(Context context,Class cl, int i) {
        Intent intent = new Intent(context, cl);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("image", i);
        context.startActivity(intent);
    }



}
