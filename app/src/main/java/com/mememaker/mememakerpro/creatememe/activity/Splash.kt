package com.mememaker.mememakerpro.creatememe.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mememaker.mememakerpro.creatememe.BuildConfig
import com.mememaker.mememakerpro.creatememe.Constant
import com.mememaker.mememakerpro.creatememe.Constant.SPLASH_ACT
import com.mememaker.mememakerpro.creatememe.Constant.SPLASH_ACT_2ndTime
import com.mememaker.mememakerpro.creatememe.R
import com.mememaker.mememakerpro.creatememe.adsManager.AdId
import com.mememaker.mememakerpro.creatememe.adsManager.AppOpenManager
import com.mememaker.mememakerpro.creatememe.databinding.ActivitySplashBinding
import com.mememaker.mememakerpro.creatememe.model.AdModel
import java.io.IOException
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/*
 *   Developer Name : Rafaqat Mehmood
 *   Whatsapp Number : 0310-1025532
 *   Designation : Sr.Android Developer
 */
class Splash : AppCompatActivity() {
    private var appOpenManager: AppOpenManager? = null
    private var countDownTimer: CountDownTimer? = null
    private lateinit var binding: ActivitySplashBinding
//    var facebookInterstitialAd: com.facebook.ads.InterstitialAd? = null
    var admobInterstitialAd: com.google.android.gms.ads.interstitial.InterstitialAd? = null

    private var ranBefore = false
    private var packageCheck = false
    private var ref: DatabaseReference? = null
    private val COUNTER_TIME = 2L
    private val COUNTER_TIME_FB = 6L
    private val COUNTER_TIME_AD = 4L

    lateinit var sharePref :SharedPreferences
    var checkLoad=false
    lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        preferences = getSharedPreferences("db", Context.MODE_PRIVATE)
        ranBefore = preferences.getBoolean("RunBefore", false)

        sharePref=getSharedPreferences("your_prefs", MODE_PRIVATE);
        sharePref.edit().putInt("resume_check",0).apply()

        ref = FirebaseDatabase.getInstance().reference.child(Constant.ADMOB_TEST_AD)

        countDownTimer = object : CountDownTimer(COUNTER_TIME * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val ex: ExecutorService = Executors.newSingleThreadScheduledExecutor()
                ex.execute { packageCheck = isInternetAvailable(this@Splash) }
            }

            override fun onFinish() {
                if (packageCheck) {
                    ref!!.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                for (dataSnapshot in snapshot.children) {

                                    for (s in dataSnapshot.children) {
                                        if (s.key == SPLASH_ACT_2ndTime) {
                                            val model: AdModel? = s.getValue(AdModel::class.java)
                                            val key = dataSnapshot.key
                                            var status: String
                                            var adId: String

                                            if (model != null && key != null) {
                                                status = model.status
                                                appOpenManager = AppOpenManager(this@Splash)

                                                if (status == "true") {

                                                    when (model.facebookID) {
                                                        "0" -> {
                                                            adId = if (BuildConfig.DEBUG) {
                                                                AdId.AD_MOB_OPEN_AD_TEST
                                                            } else {
                                                                model.adId
                                                            }
                                                            AppOpenManager.fetchAd(adId, binding.adText,binding.animationView)

                                                            Handler().postDelayed(Runnable {
                                                                if (AppOpenManager.adLoaded) {


                                                                    if (!AppOpenManager.isShowingAd) {
                                                                        AppOpenManager.showAdIfAvailable(binding.adText,binding.animationView)
                                                                        binding.animationView.visibility = View.GONE
                                                                        checkLoad=true

                                                                    }
                                                                }
                                                                else{
                                                                    activityFun()
                                                                }

                                                            },6000)
                                                        }
                                                        "1" -> {
                                                            ref!!.addListenerForSingleValueEvent(
                                                                object : ValueEventListener {
                                                                    override fun onDataChange(
                                                                        snapshot: DataSnapshot
                                                                    ) {
                                                                        if (snapshot.exists()) {
                                                                            for (dataSnapshot in snapshot.children) {

                                                                                if (dataSnapshot.key == SPLASH_ACT) {
                                                                                    for (s in dataSnapshot.children) {
                                                                                        if (s.key == Constant.SPLASH_ACT_1stTime) {
                                                                                            val model: AdModel? = s.getValue(AdModel::class.java)
                                                                                            val key = dataSnapshot.key
                                                                                            var status: String
                                                                                            var adId: String
                                                                                            var facebookID: String
                                                                                            if (model != null && key != null) {
                                                                                                status = model.status
                                                                                                facebookID = model.facebookID
                                                                                                adId = if (BuildConfig.DEBUG) {
                                                                                                    AdId.AD_MOB_INTERSTITIAL_TEST
                                                                                                } else {
                                                                                                    model.adId
                                                                                                }
                                                                                                //One Fun then Handle Admob + Facebook Ads
                                                                                                btnClickAdShow(status, adId, facebookID)

                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }


                                                                            }
                                                                        }
                                                                    }

                                                                    override fun onCancelled(error: DatabaseError) {}
                                                                })
                                                        }
                                                        else -> {
                                                            adId = if (BuildConfig.DEBUG) {
                                                                AdId.AD_MOB_OPEN_AD_TEST
                                                            } else {
                                                                model.adId
                                                            }
                                                            AppOpenManager.fetchAd(adId, binding.adText,binding.animationView)

                                                            Handler().postDelayed(Runnable {
                                                                if (AppOpenManager.adLoaded) {


                                                                    if (!AppOpenManager.isShowingAd) {
                                                                        AppOpenManager.showAdIfAvailable(binding.adText,binding.animationView)
                                                                        binding.animationView.visibility = View.GONE
                                                                        checkLoad=true

                                                                    }


                                                                } else  {
                                                                    Log.i("rafaqat", "1st Time--> Open Not Load: ")
                                                                    ref!!.addListenerForSingleValueEvent(
                                                                        object : ValueEventListener {
                                                                            override fun onDataChange(
                                                                                snapshot: DataSnapshot
                                                                            ) {
                                                                                if (snapshot.exists()) {
                                                                                    for (dataSnapshot in snapshot.children) {

                                                                                        if (dataSnapshot.key == SPLASH_ACT) {
                                                                                            for (s in dataSnapshot.children) {
                                                                                                if (s.key == Constant.SPLASH_ACT_1stTime) {
                                                                                                    val model: AdModel? =
                                                                                                        s.getValue(
                                                                                                            AdModel::class.java
                                                                                                        )
                                                                                                    val key =
                                                                                                        dataSnapshot.key
                                                                                                    var status: String
                                                                                                    var adId: String
                                                                                                    var facebookID: String
                                                                                                    if (model != null && key != null) {
                                                                                                        status =
                                                                                                            model.status

                                                                                                        facebookID = model.facebookID
                                                                                                        adId = if (BuildConfig.DEBUG) {
                                                                                                            AdId.AD_MOB_INTERSTITIAL_TEST
                                                                                                        } else {
                                                                                                            model.adId
                                                                                                        }
                                                                                                        //One Fun then Handle Admob + Facebook Ads
                                                                                                        btnClickAdShow(
                                                                                                            status,
                                                                                                            adId,
                                                                                                            facebookID
                                                                                                        )

                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                        }


                                                                                    }
                                                                                }
                                                                            }

                                                                            override fun onCancelled(error: DatabaseError) {}
                                                                        })

                                                                }
                                                            },6000)
                                                        }
                                                    }

                                                }
                                                else
                                                {
                                                    binding.animationView.visibility = View.GONE
                                                    activityFun()
                                                }


                                            } else {
                                                binding.animationView.visibility = View.GONE
                                                activityFun()
                                            }

                                        }
                                    }
                                }

                            }
                        }


                        override fun onCancelled(error: DatabaseError) {}
                    })
                } else
                {
                    binding.animationView.visibility = View.GONE
                    activityFun()
                }
            }
        }.start()


    }


    fun activityFun() {
        if (!ranBefore) {
            val editor = preferences.edit()
            editor.putBoolean("RunBefore", true)
            editor.apply()
            val intent = Intent(this, StartActivity::class.java)
            startActivity(intent)
            finish()

        } else {
            sharePref.edit().putInt("resume_check",1).apply()

            startActivity(Intent(this@Splash, MainDashboard::class.java))
            finish()
        }
    }

    private fun isInternetAvailable(context: Context): Boolean {
        return isConnected(context) && checkInternetPingGoogle()
    }

    private fun checkInternetPingGoogle(): Boolean {
        try {
            val CMD_PING_GOOGLE = "ping -c 1 google.com"
            val a = Runtime.getRuntime().exec(CMD_PING_GOOGLE).waitFor()
            return a == 0x0
        } catch (ignored: IOException) {
        } catch (ignored: InterruptedException) {
        }
        return false
    }

    private fun isConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return if (cm != null) {
            val activeNetwork = cm.activeNetworkInfo
            activeNetwork != null && activeNetwork.isConnectedOrConnecting
        } else {
            false
        }
    }

//    fun loadFbInterstitialAd(context: Context?, adId: String) {
//        val id: String
//        if (facebookInterstitialAd != null) {
//            facebookInterstitialAd = null
//        }
//        id = if (BuildConfig.DEBUG) {
//            adId
//        } else {
//            adId
//        }
//
//        facebookInterstitialAd = com.facebook.ads.InterstitialAd(context, id)
//        val interstitialAdListener: InterstitialAdListener = object : InterstitialAdListener {
//            override fun onInterstitialDisplayed(ad: Ad) {
//                // Interstitial ad displayed callback
//            }
//
//            override fun onInterstitialDismissed(ad: Ad) {
//                // Interstitial dismissed callback
//                if (facebookInterstitialAd != null) {
//                    activityFun()
//                    facebookInterstitialAd = null
//                }
//
//
//            }
//
//            override fun onError(ad: Ad, adError: AdError) {
//                // Ad error callback
//            }
//
//            override fun onAdLoaded(ad: Ad) {
//                // Interstitial ad is loaded and ready to be displayed
//                // Show the ad
//
//            }
//
//            override fun onAdClicked(ad: Ad) {
//                // Ad clicked callback
//            }
//
//            override fun onLoggingImpression(ad: Ad) {
//                // Ad impression logged callback
//            }
//        }
//
//        // For auto play video ads, it's recommended to load the ad
//        // at least 30 seconds before it is shown
//        facebookInterstitialAd!!.loadAd(
//            facebookInterstitialAd!!.buildLoadAdConfig()
//                .withAdListener(interstitialAdListener)
//                .build()
//        )
//    }
//
//    fun showFbInterstitial() {
//        if (facebookInterstitialAd != null && facebookInterstitialAd!!.isAdLoaded) {
//            facebookInterstitialAd!!.show()
//        } else {
//            activityFun()
//        }
//    }

    fun loadAdmobInterstitialAds(context: Activity?, id: String) {
        val str: String
        if (admobInterstitialAd != null) {
            admobInterstitialAd = null
        }
        val adRequest = AdRequest.Builder().build()
        str = if (BuildConfig.DEBUG) {
            id
        } else {
            id
        }

        com.google.android.gms.ads.interstitial.InterstitialAd.load(
            context!!,
            str,
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(interstitialAd: com.google.android.gms.ads.interstitial.InterstitialAd) {
                    super.onAdLoaded(interstitialAd)
                    admobInterstitialAd = interstitialAd

                    Handler().postDelayed(Runnable {
                        binding!!.adText.text = "Ready for Display"
                        showAdmobIA()
                    }, 4000)


                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    super.onAdFailedToLoad(loadAdError)
                    admobInterstitialAd = null
                    Log.e("AdsManager", "Failed Interstitial: " + loadAdError.message)
                    activityFun()
                }
            })
    }

    fun showAdmobIA() {
        if (admobInterstitialAd != null) {
            admobInterstitialAd!!.show(this@Splash)
            admobInterstitialAd!!.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {

                    if (admobInterstitialAd != null) {
                        admobInterstitialAd = null
                        activityFun()
                    }


                }
            }
        }


    }

    fun btnClickAdShow(status: String, adId: String, facebookID: String) {
        if (status == "true") {

            binding!!.adText.text = "Ad is Loading"
            loadAdmobInterstitialAds(this@Splash, adId)


        }
//        else if (status == "false") {
//            binding!!.adText.text = "Ad is Loading"
//            loadFbInterstitialAd(this@Splash, facebookID)
//
//            countDownTimer = object : CountDownTimer(COUNTER_TIME_FB * 1000, 1000) {
//                override fun onTick(millisUntilFinished: Long) {
//
//                }
//
//                override fun onFinish() {
//                    binding!!.adText.text = "Ready for Display"
//                    showFbInterstitial()
//
//                }
//            }.start()
//
//
//        }
        else {

            activityFun()
        }
    }


    override fun onStart() {
        super.onStart()

        if (checkLoad) {
            if (AppOpenManager.adLoaded) {
                if (!AppOpenManager.isShowingAd) {
                    Log.i("rafaqat", "onResume--> Open Ad is Loaded But not  Display: ")
                    AppOpenManager.showAdIfAvailable(binding.adText,binding.animationView)
                } else {
                    Log.i("rafaqat", "onResume--> Open Ad is Loaded But Display: ")
                }
            } else {
                Log.i("rafaqat", "onResume--> Open Ad not Loaded")
            }
        }
        else
        {
            if (admobInterstitialAd!=null)
            {
                showAdmobIA()
            }
        }
    }

}