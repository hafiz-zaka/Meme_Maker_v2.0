package com.mememaker.mememakerpro.creatememe;

import static com.mememaker.mememakerpro.creatememe.Constant.SPLASH_ACT;
import static com.mememaker.mememakerpro.creatememe.Constant.SPLASH_ACT_2ndTime;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mememaker.mememakerpro.creatememe.adsManager.AdId;
import com.mememaker.mememakerpro.creatememe.adsManager.AppOpenManager;
import com.mememaker.mememakerpro.creatememe.model.AdModel;

import java.util.Date;

public class App extends Application implements LifecycleObserver, Application.ActivityLifecycleCallbacks {
    boolean check =true;
    //private InterstitialBuilder interstitialBuilder;
    SharedPreferences sp;
    int resume_check;
    SharedPreferences.Editor editor;
    int myIntValue = 0;
    boolean check_admob;
    DatabaseReference ref;
    int limit_value=1;
    @Override
    public void onCreate() {
        super.onCreate();
        sp = getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
        registerActivityLifecycleCallbacks(this);
        ref = FirebaseDatabase.getInstance().getReference().child(Constant.ADMOB_TEST_AD);
        fetchAd();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    void onResume() {
        resume_check = sp.getInt("resume_check", 0);

        myIntValue = sp.getInt("ad_value", 5);
        check_admob = myIntValue != 10;
        limit_value = sp.getInt("limit_value1", 0);
        //Toast.makeText(AppDetailActivity.this, "Sorry we"+limit_value, Toast.LENGTH_SHORT).show();

        if(resume_check==1){
            showAdIfAvailable();
            //  Toast.makeText(this,"* Necessário Acesso a Internet *"+resume_check,Toast.LENGTH_LONG).show();

        }


        editor.putInt("limit_value1",limit_value+1);
        editor.apply();




        Log.d("MyApp", "App in foreground");
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void onDestroy() {
        editor = sp.edit();
        editor.putInt("resume_check", 0);
        editor.apply();
        // Toast.makeText(this,"* Necessário Acesso a Internet *",Toast.LENGTH_LONG).show();
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void oncreate() {
        editor = sp.edit();
        editor.putInt("resume_check", 0);
        editor.apply();
//        Toast.makeText(this,"* Necessário Acesso a Internet *",Toast.LENGTH_LONG).show();
        //Toast.makeText(getApplicationContext(), "* Necessário Acesso a Internet *", Toast.LENGTH_LONG).show();
    }
    private static final String LOG_TAG = "AppOpenManager";

    private AppOpenAd appOpenAdmain = null;
    private long loadTime = 0;
    private AppOpenAd.AppOpenAdLoadCallback loadCallback;

    private static boolean isShowingAd = false;
    private Activity currentActivity;
/** Constructor */


/** LifecycleObserver methods */
//        @OnLifecycleEvent(Lifecycle.Event.ON_START)
//        public void onStart() {
//            // if( isAdAvailable())
//            //  {
//            showAdIfAvailable();
////        }
////        else{
////      //  interstitialBuilder.show(this);
////            Toast.makeText(getApplicationContext(),"* Necessário Acesso a Internet *",Toast.LENGTH_LONG).show();
////            Log.d(LOG_TAG, "onStart");
////        }
//
//
//        }

    /** Request an ad */
    public void fetchAd() {

        // Have unused ad, no need to fetch another.
        if (isAdAvailable()) {
            return;
        }


        AdRequest request = getAdRequest();

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Log.d("MyApp","exist");

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                            if (dataSnapshot.getKey()!=null&&dataSnapshot.getKey().equals(SPLASH_ACT)) {
                                Log.d("MyApp","here");
                                for (DataSnapshot s1:dataSnapshot.getChildren()){
                                    if (s1.getKey()!=null&&s1.getKey().equals(SPLASH_ACT_2ndTime)){

                                        AdModel model = s1.getValue(AdModel.class);
                                        String key = dataSnapshot.getKey();
                                        String status;
                                        String adId;
                                        if (model != null && key != null) {
                                            status = model.getStatus();
                                            if ("true".equals(status)) {
                                                if (BuildConfig.DEBUG){
                                                    adId= AdId.AD_MOB_OPEN_AD_TEST;
                                                }else {
                                                    adId=model.getAdId();
                                                }
                                                loadCallback=new AppOpenAd.AppOpenAdLoadCallback() {
                                                    @Override
                                                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                                        super.onAdFailedToLoad(loadAdError);
                                                        appOpenAdmain=null;
                                                    }

                                                    @Override
                                                    public void onAdLoaded(@NonNull AppOpenAd appOpenAd) {
                                                        appOpenAdmain= appOpenAd ;
                                                        loadTime = (new Date()).getTime();
                                                        super.onAdLoaded(appOpenAd);
                                                    }
                                                };
                                                try {
                                                    AppOpenAd.load(currentActivity,adId,request,loadCallback);
                                                }catch (Exception e){
                                                    e.printStackTrace();
                                                }



                                            }

                                        }
                                    }

                                }




                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });











    }
    /** Shows the ad if one isn't already showing. */
    public void showAdIfAvailable() {
        // Only show ad if there is not already an app open ad currently showing
        // and an ad is available.
        if (!isShowingAd && isAdAvailable()) {
            Log.d(LOG_TAG, "Will show ad.");

            FullScreenContentCallback fullScreenContentCallback =
                    new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            // Set the reference to null so isAdAvailable() returns false.
                            appOpenAdmain = null;
                            isShowingAd = false;
                            fetchAd();
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(AdError adError) {}

                        @Override
                        public void onAdShowedFullScreenContent() {
                            isShowingAd = true;
                        }
                    };
            appOpenAdmain.setFullScreenContentCallback(fullScreenContentCallback);

            appOpenAdmain.show(currentActivity);

        } else {
            Log.d(LOG_TAG, "Can not show ad.");
//                if (!isShowingAd){
//                    interstitialBuilder.show(this);
//                }


            fetchAd();
        }
    }
    /** Creates and returns ad request. */

    private AdRequest getAdRequest() {
        return new AdRequest.Builder().build();
    }
    /** Utility method to check if ad was loaded more than n hours ago. */
    private boolean wasLoadTimeLessThanNHoursAgo(long numHours) {
        long dateDifference = (new Date()).getTime() - this.loadTime;
        long numMilliSecondsPerHour = 3600000;
        return (dateDifference < (numMilliSecondsPerHour * numHours));
    }

    /** Utility method that checks if ad exists and can be shown. */
    public boolean isAdAvailable() {
        return appOpenAdmain != null && wasLoadTimeLessThanNHoursAgo(1);
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {

    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        currentActivity = activity;
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        currentActivity = activity;

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
}
