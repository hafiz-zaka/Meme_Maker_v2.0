package com.mememaker.mememakerpro.creatememe.adsManager;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.mememaker.mememakerpro.creatememe.BuildConfig;
import com.mememaker.mememakerpro.creatememe.activity.Splash;


/**
 * Developer Name : Rafaqat Mehmood
 * Whatsapp       : 0310-1025532
 * Designation    : Sr.Android Developer
 */

public class AppOpenManager implements Application.ActivityLifecycleCallbacks {
    private static final String LOG_TAG = "RAFAQAT";
    public static String AD_UNIT_ID;
    private static AppOpenAd appOpenAd = null;
    public static boolean isShowingAd = false,adLoaded = false;

    Activity mActivity;
    private static AppOpenAd.AppOpenAdLoadCallback loadCallback;

    private static  Splash myApplication;

    /** Constructor */
    public AppOpenManager(Splash myApplication) {
        this.myApplication = myApplication;
    }



    /** Request an ad */
    public static void fetchAd(String id, TextView textView, LottieAnimationView animation) {
        if (BuildConfig.DEBUG) {
            AD_UNIT_ID=id;
        } else {
            AD_UNIT_ID=id;
        }
        isShowingAd = false;
        if (isAdAvailable()) {
            return;
        }

        loadCallback = new AppOpenAd.AppOpenAdLoadCallback() {

            @Override
            public void onAdLoaded(AppOpenAd ad) {
                appOpenAd = ad;
                adLoaded = true;
                textView.setText("Ad is Loading");
                Log.d("RAFAQAT","Load Success");
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                // Handle the error.
                Log.d("RAFAQAT","Load fail");
            }

        };
        AdRequest request = getAdRequest();
        try {
            AppOpenAd.load(myApplication, AD_UNIT_ID, request,
                    AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, loadCallback);
        }catch (Exception e){
            e.printStackTrace();
        }


    }


    public static void showAdIfAvailable(TextView textView,LottieAnimationView animationView) {
        // Only show ad if there is not already an app open ad currently showing
        // and an ad is available.
        if (!isShowingAd && isAdAvailable()) {
            textView.setText("Ready for Display");
            FullScreenContentCallback fullScreenContentCallback =
                    new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            // Set the reference to null so isAdAvailable() returns false.
                            appOpenAd = null;
                            isShowingAd = true;
                            adLoaded = false;
                            ((Splash)myApplication).activityFun();
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(AdError adError) {
                            Log.i("rafaqat", "onAdFailedToShowFullScreenContent: "+adError);

                        }

                        @Override
                        public void onAdShowedFullScreenContent() {
                            isShowingAd = true;
                        }
                    };

            appOpenAd.setFullScreenContentCallback(fullScreenContentCallback);
            appOpenAd.show(myApplication);

        } else {
            Log.d(LOG_TAG, "Can not show ad.");
            fetchAd(AD_UNIT_ID,textView,animationView);
        }
    }


    /** Creates and returns ad request. */
    private static AdRequest getAdRequest() {
        return new AdRequest.Builder().build();
    }


    public static boolean isAdAvailable() {
        return appOpenAd != null;
    }


    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {


    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {

    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {


    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {

    }

    public static boolean adsisLoaded(){
        return adLoaded;
    }

    public static boolean adsisShowed(){
        return isShowingAd;
    }

}