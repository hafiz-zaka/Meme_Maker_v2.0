package com.mememaker.mememakerpro.creatememe.adsManager;



import static com.mememaker.mememakerpro.creatememe.constant.Constant.MY_TAG;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.mememaker.mememakerpro.creatememe.BuildConfig;
import com.mememaker.mememakerpro.creatememe.R;

import java.util.Objects;


/**
 * Developer Name : Rafaqat Mehmood
 * Whatsapp       : 0310-1025532
 * Designation    : Sr.Android Developer
 */
public class RafaqatAdMobManager {
    private static RafaqatAdMobManager INSTANCE;
    private InterstitialAd mInterstitialAd;
    private NativeAd mNativeAd;
    private AdView adView;
    int loadAdCounter = 0;
    int showAdCounter = 0;


    public static RafaqatAdMobManager getInstance()
    {
        if (INSTANCE==null)
        {
            synchronized (RafaqatAdMobManager.class)
            {
                if (INSTANCE==null)
                {
                    INSTANCE=new RafaqatAdMobManager();
                }
            }
        }

        return INSTANCE;
    }

    public void initializeAds(Context context) {
        MobileAds.initialize(context, initializationStatus -> {
            Log.i(MY_TAG, "initializeAds: ");
        });
    }


    public void loadBanner(Activity activity, LinearLayout linearLayout,String id) {
        adView = new AdView(activity);
        if (BuildConfig.DEBUG) {
            adView.setAdUnitId(id);
        } else {
            adView.setAdUnitId(id);

        }

        linearLayout.removeAllViews();
        linearLayout.addView(adView);
        AdSize adSize = getAdSize(activity, linearLayout);
        adView.setAdSize(adSize);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                Log.e("AdsManager", "Failed Banner: " + loadAdError);
            }
        });


    }
    private AdSize getAdSize(Activity activity, LinearLayout linearLayout) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        float desnsity = displayMetrics.density;
        float adWidthPixels = linearLayout.getWidth();
        if (adWidthPixels == 0) {
            adWidthPixels = displayMetrics.widthPixels;
        }
        int adWidth = (int) (adWidthPixels / desnsity);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adWidth);
    }


//    public void populateNativeAdView(NativeAd nativeAd, NativeAdView adView) {
//        // Set the media view.
//        adView.setMediaView(adView.findViewById(R.id.ad_media));
//
//        // Set other ad assets.
//        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
//        adView.setBodyView(adView.findViewById(R.id.ad_body));
//        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
//        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
//        adView.setPriceView(adView.findViewById(R.id.ad_price));
//        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
//        adView.setStoreView(adView.findViewById(R.id.ad_store));
//        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));
//
//        // The headline and mediaContent are guaranteed to be in every NativeAd.
//        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
//        adView.getMediaView().setMediaContent(nativeAd.getMediaContent());
//
//        // These assets aren't guaranteed to be in every NativeAd, so it's important to
//        // check before trying to display them.
//        if (nativeAd.getBody() == null) {
//            adView.getBodyView().setVisibility(View.INVISIBLE);
//        } else {
//            adView.getBodyView().setVisibility(View.VISIBLE);
//            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
//        }
//
//        if (nativeAd.getCallToAction() == null) {
//            adView.getCallToActionView().setVisibility(View.INVISIBLE);
//        } else {
//            adView.getCallToActionView().setVisibility(View.VISIBLE);
//            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
//        }
//
//        if (nativeAd.getIcon() == null) {
//            adView.getIconView().setVisibility(View.GONE);
//        } else {
//            ((ImageView) adView.getIconView()).setImageDrawable(
//                    nativeAd.getIcon().getDrawable());
//            adView.getIconView().setVisibility(View.VISIBLE);
//        }
//
//        if (nativeAd.getPrice() == null) {
//            adView.getPriceView().setVisibility(View.INVISIBLE);
//        } else {
//            adView.getPriceView().setVisibility(View.VISIBLE);
//            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
//        }
//
//        if (nativeAd.getStore() == null) {
//            adView.getStoreView().setVisibility(View.INVISIBLE);
//        } else {
//            adView.getStoreView().setVisibility(View.VISIBLE);
//            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
//        }
//
//        if (nativeAd.getStarRating() == null) {
//            adView.getStarRatingView().setVisibility(View.INVISIBLE);
//        } else {
//            ((RatingBar) adView.getStarRatingView())
//                    .setRating(nativeAd.getStarRating().floatValue());
//            adView.getStarRatingView().setVisibility(View.VISIBLE);
//        }
//
//        if (nativeAd.getAdvertiser() == null) {
//            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
//        } else {
//            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
//            adView.getAdvertiserView().setVisibility(View.VISIBLE);
//        }
//
//        // This method tells the Google Mobile Ads SDK that you have finished populating your
//        // native ad view with this native ad.
//        adView.setNativeAd(nativeAd);
//    }
//
//    //Load And Show Start Screen
//    public void loadAndShowNative(Activity activity,FrameLayout frameLayout, TextView startButton, LottieAnimationView animationView) {
//        String id;
//        if (BuildConfig.DEBUG) {
//            id =AdId.AD_MOB_NATIVE_TEST;
//        } else {
//            id =AdId.AD_MOB_NATIVE_REAL;
//        }
//        AdLoader adLoader = new AdLoader.Builder(activity,id).forNativeAd(nativeAd ->
//                        mNativeAd = nativeAd).
//                withAdListener(new AdListener() {
//                    @Override
//                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
//                        super.onAdFailedToLoad(loadAdError);
//                        Log.e(MY_TAG, "onAdFailedToLoad: native" + loadAdError.getMessage());
//                        startButton.setVisibility(View.VISIBLE);
//                        animationView.setVisibility(View.INVISIBLE);
//                    }
//
//                    @Override
//                    public void onAdLoaded() {
//                        super.onAdLoaded();
//                        Log.e(MY_TAG, "onAdLoaded: native");
//                        startButton.setVisibility(View.VISIBLE);
//                        animationView.setVisibility(View.INVISIBLE);
//                        showNativeAd(activity, frameLayout);
//                        //  showNativeAd(context, activity, frameLayout, nativeAdLayout);
//                    }
//                }).build();
//        adLoader.loadAd(new AdRequest.Builder().build());
//    }
//
//    //Load and Show Main Activity
//    public void loadAndShowNative(Activity activity,FrameLayout frameLayout,String adId) {
//        String id;
//        if (BuildConfig.DEBUG) {
//            id =adId;
//        } else {
//            id =adId;
//        }
//        AdLoader adLoader = new AdLoader.Builder(activity,id).forNativeAd(nativeAd ->
//                        mNativeAd = nativeAd).
//                withAdListener(new AdListener() {
//                    @Override
//                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
//                        super.onAdFailedToLoad(loadAdError);
//                        Log.e(MY_TAG, "onAdFailedToLoad: native" + loadAdError.getMessage());
//                    }
//
//                    @Override
//                    public void onAdLoaded() {
//                        super.onAdLoaded();
//                        Log.e(MY_TAG, "onAdLoaded: native");
//
//                        showNativeAd(activity,frameLayout);
//                        //  showNativeAd(context, activity, frameLayout, nativeAdLayout);
//                    }
//                }).build();
//        adLoader.loadAd(new AdRequest.Builder().build());
//    }

//    public void showNativeAd(Activity activity, FrameLayout frameLayout) {
//        if (mNativeAd != null) {
//            frameLayout.setVisibility(View.VISIBLE);
//            NativeAdView adView =
//                    (NativeAdView) activity.getLayoutInflater().inflate(R.layout.native_layout, null);
//            populateNativeAdView(mNativeAd, adView);
//            frameLayout.removeAllViews();
//            frameLayout.addView(adView);
//        }
//    }



    public void destroyAllAds() {
        if (adView != null) {
            adView.destroy();
        }
    }

    public void loadNative(Activity context,FrameLayout frameLayout,String id) {
        String adId=id;
        if (BuildConfig.DEBUG) {
            adId=id;
        } else {
            adId = id;
        }
        mNativeAd = null;
        AdLoader adLoader = new AdLoader.Builder(context, adId).forNativeAd(nativeAd ->
                        mNativeAd = nativeAd).
                withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);
                        Log.e(MY_TAG, "Customize Native onAdFailedToLoad: " + loadAdError.getMessage());

                    }

                    @Override
                    public void onAdLoaded() {
                        super.onAdLoaded();
                        showNativeAdsCustomize(context,frameLayout);
                        Log.e(MY_TAG, "Customize Native onAdLoaded: ");
                    }
                }).build();
        adLoader.loadAd(new AdRequest.Builder().build());
    }
    public void showNativeAdsCustomize(Activity mContext, FrameLayout fLayout) {
        if (mNativeAd == null) {
            return;
        }
        @SuppressLint("InflateParams") NativeAdView adVw = ((NativeAdView) LayoutInflater.from(mContext).inflate(R.layout.custom_native, null));
        MediaView mediaView = adVw.findViewById(R.id.ad_media);
        mediaView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
        adVw.setMediaView(mediaView);

        adVw.setHeadlineView(adVw.findViewById(R.id.tv_title));
        adVw.setCallToActionView(adVw.findViewById(R.id.btn_click));
        adVw.setIconView(adVw.findViewById(R.id.icon_ad));
        if (mNativeAd != null) {

            ((TextView) Objects.requireNonNull(adVw.getHeadlineView())).setText(mNativeAd.getHeadline());

            if (mNativeAd.getCallToAction() == null) {
                Objects.requireNonNull(adVw.getCallToActionView()).setVisibility(View.INVISIBLE);
            } else {
                Objects.requireNonNull(adVw.getCallToActionView()).setVisibility(View.VISIBLE);
                ((TextView) adVw.getCallToActionView()).setText(mNativeAd.getCallToAction());
            }

            if (mNativeAd.getIcon() == null) {
                Objects.requireNonNull(adVw.getIconView()).setVisibility(View.GONE);
            } else {
                Glide.with(mContext.getApplicationContext()).load(mNativeAd.getIcon().getDrawable()).circleCrop().into((ImageView) Objects.requireNonNull(adVw.getIconView()));
                adVw.getIconView().setVisibility(View.VISIBLE);
            }

            adVw.setNativeAd(mNativeAd);

            VideoController vc = Objects.requireNonNull(mNativeAd.getMediaContent()).getVideoController();

            if (vc.hasVideoContent()) {

                vc.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
                    @Override
                    public void onVideoEnd() {
                        super.onVideoEnd();
                    }
                });
            }

            fLayout.removeAllViews();
            fLayout.addView(adVw);
            //loadNative(mContext);
        }
    }


//    public static void myFun(Context context,Activity activity,String key,String value,String type)
//    {
//        Intent intent=new Intent(context,activity.getClass());
//        intent.putExtra(key,value);
//        intent.putExtra(key,value);
//        intent.putExtra(key,value);
//        intent.putExtra(key,value);
//        intent.putExtra(key,value);
//        intent.putExtra(key,value);
//        intent.putExtra(key,value);
//        intent.putExtra(key,value);
//        context.startActivity(intent);
//    }

}
