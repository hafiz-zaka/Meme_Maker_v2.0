package com.mememaker.mememakerpro.creatememe.activity

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaScannerConnection
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.appbrain.InterstitialBuilder
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.mememaker.mememakerpro.creatememe.BuildConfig
import com.mememaker.mememakerpro.creatememe.Common.load_IA
import com.mememaker.mememakerpro.creatememe.Common.show_IA
import com.mememaker.mememakerpro.creatememe.Constant
import com.mememaker.mememakerpro.creatememe.R
import com.mememaker.mememakerpro.creatememe.SavedImages
import com.mememaker.mememakerpro.creatememe.SharePrefConfig
import com.mememaker.mememakerpro.creatememe.adapters.CategoriesDisplayAdapter
import com.mememaker.mememakerpro.creatememe.adapters.MyAdapter.list
import com.mememaker.mememakerpro.creatememe.adapters.NewAdapter
import com.mememaker.mememakerpro.creatememe.adapters.NewAdapter2
import com.mememaker.mememakerpro.creatememe.adsManager.AdId
import com.mememaker.mememakerpro.creatememe.adsManager.RafaqatAdMobManager
import com.mememaker.mememakerpro.creatememe.databinding.ActivityCategorieDisplayBinding
import com.mememaker.mememakerpro.creatememe.model.AdModel
import com.mememaker.mememakerpro.creatememe.model.MainModel
import com.mememaker.mememakerpro.creatememe.model.MenuItem
import com.mememaker.mememakerpro.creatememe.myinterface.MainItemClick
import com.mememaker.mememakerpro.creatememe.myinterface.MainItemClick2
import com.mememaker.mememakerpro.creatememe.myinterface.MenuItemClick
import dmax.dialog.SpotsDialog
import java.io.File
import java.io.IOException
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class  CategoriesDisplayAct : AppCompatActivity(), MenuItemClick {

    private lateinit var binding: ActivityCategorieDisplayBinding
    private var actionBarDrawerToggle: ActionBarDrawerToggle? = null
    private var activity = this@CategoriesDisplayAct;
    private val TAG = "RafaqatMehmood"
    private var catAdapter: CategoriesDisplayAdapter? = null
    private var interstitialBuilder: InterstitialBuilder? = null


    private val storage_permissions = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA,
    )
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private val storage_permissions_33 = arrayOf(
        Manifest.permission.READ_MEDIA_IMAGES
    )
    private var permissionRequestCode=1

    fun permissions(): Array<String> {
        val p: Array<String> = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            storage_permissions_33
        } else {
            storage_permissions
        }
        return p
    }

    private var progressDialog: SpotsDialog? = null

    private val COUNTER_TIME_AD = 5L
    private var packageCheck = false
    private var ref: DatabaseReference? = null

    companion object
    {
        @JvmField
        var categoriesDisplayAct=0
        var mInterstitialAd: InterstitialAd? = null
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_categorie_display)
        interstitialBuilder = InterstitialBuilder.create()
            .setAdId(com.appbrain.AdId.LEVEL_COMPLETE)
            .setOnDoneCallback { // Preload again, so we can use interstitialBuilder again.
                interstitialBuilder!!.preload(this@CategoriesDisplayAct)
            }
            .preload(this@CategoriesDisplayAct)
        ref = FirebaseDatabase.getInstance().reference.child(Constant.ADMOB_TEST_AD)

        progressDialog = SpotsDialog(this@CategoriesDisplayAct, R.style.Custom)
        progressDialog!!.setCanceledOnTouchOutside(false)

        try {
            ref!!.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (dataSnapshot in snapshot.children) {
                            val key = dataSnapshot.key
                            if (key == Constant.CATEGORIES_ACT) {
                                for (s in dataSnapshot.children) {
                                    if (s.key == Constant.CATEGORIES_ACT_NATIVE) {
                                        val model: AdModel? = s.getValue(AdModel::class.java)
                                        var adId: String
                                        if (model!!.status == "true") {
                                            adId = if (BuildConfig.DEBUG) {
                                                AdId.AD_MOB_NATIVE_TEST
                                            } else {
                                                model.adId
                                            }
                                            //Load and Show AdMob Native
                                            RafaqatAdMobManager.getInstance().loadNative(
                                                this@CategoriesDisplayAct,
                                                binding.nativeTemplate,
                                                adId
                                            )
                                            binding.advertisingArea.visibility = View.INVISIBLE
                                            binding.nativeTemplate.visibility = View.VISIBLE
                                        } else {
                                            // binding.advertisingArea.visibility = View.VISIBLE
                                            binding.nativeTemplate.visibility = View.INVISIBLE

                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        }catch (e:Exception){}

        binding.apply {

            actionBarDrawerToggle =
                ActionBarDrawerToggle(activity, drawer, R.string.nav_open, R.string.nav_close)

            // pass the Open and Close toggle for the drawer layout listener
            // to toggle the button
            drawer.addDrawerListener(actionBarDrawerToggle!!)
            actionBarDrawerToggle!!.syncState()


            mainDashboardToolbar.navMenu.setOnClickListener {
                drawer.openDrawer(GravityCompat.START)
            }

            //Navigation Function
            navigation.setNavigationItemSelectedListener { item ->
                val id = item.itemId
                if (id == R.id.nav_home) {
                    drawer.close()
                } else if (id == R.id.nav_custom) {

                        runTimePermission()
                    drawer.close()
                } else if (id == R.id.nav_save) {
                    val intent = Intent(
                        application,
                        SavedImages::class.java
                    )
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    application.startActivity(intent)
                    drawer.close()
                } else if (id == R.id.nav_more) {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("market://search?q=pub:Torcia+Technologies")
                        )
                    )
                    drawer.close()
                } else if (id == R.id.nav_rateUs) {
                    val appPackageName =
                        packageName // getPackageName() from Context or Activity object
                    try {
                        startActivity(
                            Intent(
                                Intent.ACTION_VIEW, Uri.parse(
                                    "market://details?id=$appPackageName"
                                )
                            )
                        )
                    } catch (anfe: ActivityNotFoundException) {
                        startActivity(
                            Intent(
                                Intent.ACTION_VIEW, Uri.parse(
                                    "https://play.google.com/store/apps/details?id=$appPackageName"
                                )
                            )
                        )
                    }
                    drawer.close()
                } else if (id == R.id.nav_privacy) {
                    drawer.close()
                    val alert = AlertDialog.Builder(activity)
                    alert.setTitle("Privacy Policy")
                    val wv = WebView(activity)
                    wv.settings.javaScriptEnabled = true
                    wv.loadUrl("https://docs.google.com/document/d/1SEbdpQ3DibjFk6RTY72Gh-Tee9zuHAkPA2sECaORWjI/edit") //Your Privacy Policy Url Here
                    wv.webViewClient = object : WebViewClient() {
                        override fun shouldOverrideUrlLoading(
                            view: WebView,
                            url: String
                        ): Boolean {
                            view.settings.javaScriptEnabled = true
                            view.loadUrl(url)
                            return true
                        }
                    }
                    alert.setView(wv)
                    alert.setNegativeButton(
                        "Close"
                    ) { dialog, id -> dialog.dismiss() }
                    alert.show()
                }
                false
            }


            binding.catRV.layoutManager = LinearLayoutManager(this@CategoriesDisplayAct)
            catAdapter = CategoriesDisplayAdapter(
                catData(),
                this@CategoriesDisplayAct,
                this@CategoriesDisplayAct
            )
            binding.catRV.adapter = catAdapter
        }

    }

    private fun catData(): ArrayList<MenuItem> {
        val dataModelList = ArrayList<MenuItem>()
        dataModelList.add(MenuItem("Popular", 0))
        dataModelList.add(MenuItem("New", 0))
        dataModelList.add(MenuItem("Decent", 0))
        dataModelList.add(MenuItem("Religious", 0))
        dataModelList.add(MenuItem("University", 0))
        dataModelList.add(MenuItem("Dank", 0))
//        dataModelList.add(MenuItem("More", 0))
        return dataModelList
    }

    private fun buttonListener(check: String) {
        try {
            if (packageCheck) {
                if (ref!=null) {
                    ref!!.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                for (dataSnapshot in snapshot.children) {
                                    val key = dataSnapshot.key
                                    if (key == Constant.CATEGORIES_ACT) {
                                        for (s in dataSnapshot.children) {
                                            if (s.key == Constant.SPLASH_ACT_1stTime) {
                                                for (ss in s.children) {
                                                    val model: AdModel? =
                                                        ss.getValue(AdModel::class.java)
                                                    var adId: String
                                                    if (ss.key == check) {
                                                        if (model?.status == "true") {
                                                            adId = if (BuildConfig.DEBUG) {
                                                                AdId.AD_MOB_INTERSTITIAL_TEST
                                                            } else {
                                                                model.adId
                                                            }
                                                            if (categoriesDisplayAct > 0) {
                                                                progressDialog!!.dismiss()
                                                                if (categoriesDisplayAct==3){
                                                                    interstitialBuilder?.show(this@CategoriesDisplayAct)
                                                                    interstitialBuilder =
                                                                        InterstitialBuilder.create()
                                                                            .setAdId(com.appbrain.AdId.EXIT)
                                                                            .setFinishOnExit(
                                                                                this@CategoriesDisplayAct
                                                                            ).preload(this@CategoriesDisplayAct)
                                                                            }
                                                                itemClickFun(check)
                                                                categoriesDisplayAct--
                                                            } else {
                                                                progressDialog!!.show()
                                                                load_IA(
                                                                    this@CategoriesDisplayAct,
                                                                    adId,
                                                                    check
                                                                )
                                                            }
                                                        } else {
                                                            itemClickFun(check)

                                                        }
                                                    }
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
                else
                {
                    Toast.makeText(
                        this@CategoriesDisplayAct,
                        "Data Not Present",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                progressDialog!!.dismiss()
                itemClickFun(check)
            }
        }catch (e:Exception)
        {
            Log.i(TAG, "Error: "+e.message)
        }

    }

    private fun itemClickFun(check: String) {
        try {
            val intent = Intent(this, MainDashboard::class.java)
            intent.putExtra("selectionCheck", check)
            startActivity(intent)
            Log.i(TAG, "itemClickFun: "+check)
        }catch (e: Exception){
            e.printStackTrace()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && data != null) {
            var s = data.data?.encodedPath

            val intent = Intent(this, EditActivity::class.java)
            intent.putExtra("main", s)
            startActivity(intent)

        }
    }

    override fun onBackPressed() {
        val factory = LayoutInflater.from(this)
        val exitDialog = AlertDialog.Builder(this).create()
        val deleteDialogView = factory.inflate(R.layout.exit_dialog, null)
        exitDialog.setView(deleteDialogView)


        deleteDialogView.findViewById<View>(R.id.exitApp).setOnClickListener {
            exitDialog.dismiss()
            finishAffinity()
        }
        deleteDialogView.findViewById<View>(R.id.cancel).setOnClickListener { exitDialog.dismiss() }

        exitDialog.show()
    }

    override fun itemPos(model: MenuItem?, newName: Int) {
        buttonListener(model!!.title)
    }


    private fun runTimePermission() {
        try {
            ActivityCompat.requestPermissions(this@CategoriesDisplayAct,
                permissions(),
                permissionRequestCode);
        }catch (e:Exception){
            e.printStackTrace()
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode==permissionRequestCode) {
            try {
                if (grantResults.isNotEmpty())
                {
                    Log.i("rafaqat", "Granted: ")
                    ImagePicker.with(this@CategoriesDisplayAct)
                        .crop()                    //Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(
                            1080,
                            1080
                        )    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start()
                }
                else
                {
                    Log.i("rafaqat", "Not: ")
                    runTimePermission()

                }
            }catch (e:Exception ){
               e.printStackTrace()
            }


        }
    }

    private fun load_IA(context: Activity?, adID: String?, check: String) {
        val str2: String?
        if (mInterstitialAd != null) {
            mInterstitialAd = null
        }
        val adRequest = AdRequest.Builder().build()
        str2 = if (BuildConfig.DEBUG) {
            adID
        } else {
            adID
        }
        InterstitialAd.load(context!!, str2!!, adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                super.onAdLoaded(interstitialAd)
                mInterstitialAd = interstitialAd
                Log.e(
                    com.mememaker.mememakerpro.creatememe.constant.Constant.MY_TAG,
                    "onInterstitial_Ad_Loaded:"
                )
                Handler().postDelayed({
                    progressDialog!!.dismiss()
                    show_IA(context, check)
                }, COUNTER_TIME_AD)
            }

            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                super.onAdFailedToLoad(loadAdError)
                progressDialog!!.dismiss()
                show_IA(context, check)

                Log.i(TAG, "onAdFailedToLoad: " + loadAdError.message)
                mInterstitialAd = null
            }
        })
    }

    private fun show_IA(context: Activity?, check: String) {
        if (mInterstitialAd != null) {
            mInterstitialAd!!.show(context!!)
            mInterstitialAd!!.fullScreenContentCallback =
                object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        mInterstitialAd = null
                        itemClickFun(check)
                        categoriesDisplayAct = 6
                    }

                    override fun onAdShowedFullScreenContent() {
                        super.onAdShowedFullScreenContent()
                        mInterstitialAd = null
                    }
                }
        }else{
            interstitialBuilder?.show(this@CategoriesDisplayAct)
            interstitialBuilder =
                InterstitialBuilder.create()
                    .setAdId(com.appbrain.AdId.EXIT)
                    .setFinishOnExit(
                        this@CategoriesDisplayAct
                    ).preload(this@CategoriesDisplayAct)
            itemClickFun(check)
            categoriesDisplayAct = 6
        }
    }

    override fun onStart() {
        super.onStart()
        valueChecker()
    }

    private fun valueChecker() {
        val ex: ExecutorService = Executors.newSingleThreadScheduledExecutor()
        ex.execute { packageCheck = isInternetAvailable(this@CategoriesDisplayAct) }
    }

    private val CMD_PING_GOOGLE = "ping -c 1 google.com"
    private fun isInternetAvailable(context: Context): Boolean {
        return isConnected(context) && checkInternetPingGoogle()
    }

    private fun checkInternetPingGoogle(): Boolean {
        try {
            val a = Runtime.getRuntime().exec(CMD_PING_GOOGLE).waitFor()
            return a == 0x0
        } catch (_: IOException) {
        } catch (_: InterruptedException) {
        }
        return false
    }

    private fun isConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return run {
            val activeNetwork = cm.activeNetworkInfo
            activeNetwork != null && activeNetwork.isConnectedOrConnecting
        }
    }
}