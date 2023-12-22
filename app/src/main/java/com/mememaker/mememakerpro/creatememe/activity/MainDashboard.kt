package com.mememaker.mememakerpro.creatememe.activity

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import com.mememaker.mememakerpro.creatememe.Constant
import com.mememaker.mememakerpro.creatememe.Constant.FavBtn
import com.mememaker.mememakerpro.creatememe.Constant.HomeBtn
import com.mememaker.mememakerpro.creatememe.Constant.TemplateItemClick
import com.mememaker.mememakerpro.creatememe.InAppUpdate
import com.mememaker.mememakerpro.creatememe.ItemDecoration
import com.mememaker.mememakerpro.creatememe.R
import com.mememaker.mememakerpro.creatememe.SavedImages
import com.mememaker.mememakerpro.creatememe.SharePrefConfig.Companion.getUriListFromSharedPreferences
import com.mememaker.mememakerpro.creatememe.SharePrefConfig.Companion.saveUriListToSharedPreferences
import com.mememaker.mememakerpro.creatememe.activity.CategoriesDisplayAct.Companion.categoriesDisplayAct
import com.mememaker.mememakerpro.creatememe.activity.CategoriesDisplayAct.Companion.mInterstitialAd
import com.mememaker.mememakerpro.creatememe.adapters.CategoriesAdapter
import com.mememaker.mememakerpro.creatememe.adapters.FavAdapter
import com.mememaker.mememakerpro.creatememe.adapters.NewAdapter2
import com.mememaker.mememakerpro.creatememe.adsManager.AdId
import com.mememaker.mememakerpro.creatememe.databinding.ActivityMainDashboardBinding
import com.mememaker.mememakerpro.creatememe.model.AdModel
import com.mememaker.mememakerpro.creatememe.model.MenuItem
import com.mememaker.mememakerpro.creatememe.model.PopularModel
import com.mememaker.mememakerpro.creatememe.myinterface.MainItemClick2
import com.mememaker.mememakerpro.creatememe.myinterface.MenuItemClick
import dmax.dialog.SpotsDialog
import java.io.IOException
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


/*
 *   Developer Name : Rafaqat Mehmood
 *   Whatsapp Number : 0310-1025532
 *   Designation : Sr.Android Developer
 */
class MainDashboard : AppCompatActivity(), MenuItemClick, MainItemClick2 {
    private var actionBarDrawerToggle: ActionBarDrawerToggle? = null
    private var adapter: FavAdapter? = null
    private var progressDialog: SpotsDialog? = null

    private val COUNTER_TIME_AD = 5L
    private var packageCheck = false
    private var ref: DatabaseReference? = null

    private var saveList: ArrayList<Uri>? = null
    private var index = 0
    private var categoriesAdapter: CategoriesAdapter? = null

    private lateinit var binding: ActivityMainDashboardBinding
    private val permissionCode = 3090
    lateinit var sharePref :SharedPreferences


    private var inAppUpdate: InAppUpdate? = null

    private lateinit var storageRef: StorageReference
    private var adapter2: NewAdapter2? = null

    private lateinit var popularList: ArrayList<PopularModel>
    private lateinit var newList: ArrayList<PopularModel>
    private lateinit var decentList: ArrayList<PopularModel>
    private lateinit var islamicList: ArrayList<PopularModel>
    private lateinit var universityList: ArrayList<PopularModel>
    private lateinit var dankList: ArrayList<PopularModel>
    private var interstitialBuilder: InterstitialBuilder? = null


    lateinit var meme : String

    private val storage_permissions = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA,
    )

    private val storage_permissions_33 = arrayOf(
        Manifest.permission.READ_MEDIA_IMAGES
    )
    private var permissionCheck = true

    private var imagePickerRequestCode = 2030
    private var permissionRequestCode = 1

    fun permissions(): Array<String> {
        val p: Array<String> = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            storage_permissions_33
        } else {
            storage_permissions
        }
        return p
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main_dashboard)

        meme="Popular"

        sharePref=getSharedPreferences("your_prefs", MODE_PRIVATE);
        sharePref.edit().putInt("resume_check",1).apply()

        ref = FirebaseDatabase.getInstance().reference.child(Constant.ADMOB_TEST_AD)
        popularList = ArrayList()
        newList = ArrayList()
        decentList = ArrayList()
        islamicList = ArrayList()
        universityList = ArrayList()
        dankList = ArrayList()

        saveList = ArrayList()

        inAppUpdate = InAppUpdate(this);
        inAppUpdate!!.checkForAppUpdate();
        interstitialBuilder = InterstitialBuilder.create()
            .setAdId(com.appbrain.AdId.LEVEL_COMPLETE)
            .setOnDoneCallback { // Preload again, so we can use interstitialBuilder again.
                interstitialBuilder!!.preload(this@MainDashboard)
            }
            .preload(this@MainDashboard)


        visibilityShow("Popular")


        binding.apply {
            actionBarDrawerToggle = ActionBarDrawerToggle(
                this@MainDashboard,
                drawer,
                R.string.nav_open,
                R.string.nav_close
            )
            // pass the Open and Close toggle for the drawer layout listener
            // to toggle the button
            drawer.addDrawerListener(actionBarDrawerToggle!!)
            actionBarDrawerToggle!!.syncState()
            progressDialog = SpotsDialog(this@MainDashboard, R.style.Custom)
            progressDialog!!.setCanceledOnTouchOutside(false)


            //Navigation Bar Click
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
                    val intent = Intent(application, SavedImages::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    application.startActivity(intent)
                    drawer.close()
                } else if (id == R.id.nav_more) {
                    try {
                        startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://play.google.com/store/apps/details?id=com.Satellitefinder.Satellitepointer.DishAligner.LNBsetting")
                            ) )
                    }catch (e:Exception){
                        e.message
                    }


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
                    } catch (anfe: Exception) {
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
                    val alert = AlertDialog.Builder(this@MainDashboard)
                    alert.setTitle("Privacy Policy")
                    val wv = WebView(this@MainDashboard)
                    wv.settings.javaScriptEnabled = true
                    wv.loadUrl("https://docs.google.com/document/d/1SEbdpQ3DibjFk6RTY72Gh-Tee9zuHAkPA2sECaORWjI/edit") //Your Privacy Policy Url Here
                    wv.webViewClient = object : WebViewClient() {
                        @Deprecated("Deprecated in Java")
                        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                            view.settings.javaScriptEnabled = true
                            view.loadUrl(url)
                            return true
                        }
                    }
                    alert.setView(wv)
                    alert.setNegativeButton("Close") { dialog, id -> dialog.dismiss() }
                    alert.show()
                }
                false
            }

            //        Floating Click
            customFloat.setOnClickListener {
                runTimePermission()
            }

            //Categories Initialize Item
            categoriesInitialize(this@MainDashboard, categoriesAdapter, newCatRv, categoriesData())

            when (intent.getStringExtra("selectionCheck")) {

                "Popular" -> {
                    visibilityShow("Popular")

                }

                "New" -> {
                    visibilityShow("New")

                }

                "Religious" -> {
                    visibilityShow("Religious")
                }

                "Decent" -> {
                    visibilityShow("Decent")
                }

                "University" -> {
                    visibilityShow("University")
                }

                "Dank" -> {
                    visibilityShow("Dank")
                }

                "More" -> {
                    visibilityShow("More")
                }
            }





                //Fav Button Click
                favBtn.setOnClickListener {
                    if (packageCheck) {
                        ref!!.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if (snapshot.exists()) {
                                    for (dataSnapshot in snapshot.children) {
                                        val key = dataSnapshot.key
                                        if (key == Constant.MAIN_ACT) {
                                            for (s in dataSnapshot.children) {
                                                if (s.key == Constant.INTERSTITIAL) {
                                                    for (ss in s.children) {
                                                        val model: AdModel? = ss.getValue(AdModel::class.java)
                                                        var adId: String
                                                        if (ss.key == FavBtn) {
                                                            if (model?.status == "true") {
                                                                adId = if (BuildConfig.DEBUG) {
                                                                    AdId.AD_MOB_INTERSTITIAL_TEST
                                                                } else {
                                                                    model.adId
                                                                }
                                                                if (categoriesDisplayAct > 0) {
                                                                    progressDialog!!.dismiss()

                                                                    if (categoriesDisplayAct==3){
                                                                        interstitialBuilder?.show(this@MainDashboard)
                                                                        interstitialBuilder =
                                                                            InterstitialBuilder.create()
                                                                                .setAdId(com.appbrain.AdId.EXIT)
                                                                                .setFinishOnExit(
                                                                                    this@MainDashboard
                                                                                ).preload(this@MainDashboard)
                                                                    }
                                                                    favFunctionality(
                                                                        popularRecycleView,
                                                                        newRecycleView,
                                                                        cFavRecycleView,
                                                                        cDecentRecycleView,
                                                                        cIslamicRecycleView,
                                                                        cMoreRecycleView,
                                                                        universityRV,
                                                                        cDankRV,
                                                                        favBtn
                                                                    )
                                                                    categoriesDisplayAct--
                                                                } else {

                                                                    progressDialog!!.show()
                                                                    load_IA_2(
                                                                        this@MainDashboard,
                                                                        adId,
                                                                        "FavBtn",
                                                                        popularRecycleView,
                                                                        newRecycleView,
                                                                        cFavRecycleView,
                                                                        cDecentRecycleView,
                                                                        cIslamicRecycleView,
                                                                        cMoreRecycleView,
                                                                        universityRV,
                                                                        cDankRV,
                                                                        favBtn
                                                                    )

                                                                }
                                                            } else {
                                                                favFunctionality(
                                                                    popularRecycleView,
                                                                    newRecycleView,
                                                                    cFavRecycleView,
                                                                    cDecentRecycleView,
                                                                    cIslamicRecycleView,
                                                                    cMoreRecycleView,
                                                                    universityRV,
                                                                    cDankRV,
                                                                    favBtn
                                                                )

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
                    } else {
                        progressDialog!!.dismiss()
                        favFunctionality(
                            popularRecycleView,
                            newRecycleView,
                            cFavRecycleView,
                            cDecentRecycleView,
                            cIslamicRecycleView,
                            cMoreRecycleView,
                            universityRV,
                            cDankRV,
                            favBtn
                        )
                    }

                }

                //Home Button Click
                homeBtn.setOnClickListener {
                    if (packageCheck) {
                        ref!!.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if (snapshot.exists()) {
                                    for (dataSnapshot in snapshot.children) {
                                        val key = dataSnapshot.key
                                        if (key == Constant.MAIN_ACT) {
                                            for (s in dataSnapshot.children) {
                                                if (s.key == Constant.INTERSTITIAL) {
                                                    for (ss in s.children) {
                                                        val model: AdModel? =
                                                            ss.getValue(AdModel::class.java)
                                                        var adId: String
                                                        if (ss.key == HomeBtn) {
                                                            if (model?.status == "true") {
                                                                adId = if (BuildConfig.DEBUG) {
                                                                    AdId.AD_MOB_INTERSTITIAL_TEST
                                                                } else {
                                                                    model.adId
                                                                }
                                                                if (categoriesDisplayAct > 0) {
                                                                    progressDialog!!.dismiss()
                                                                    if (categoriesDisplayAct==3){
                                                                        interstitialBuilder?.show(this@MainDashboard)
                                                                        interstitialBuilder =
                                                                            InterstitialBuilder.create()
                                                                                .setAdId(com.appbrain.AdId.EXIT)
                                                                                .setFinishOnExit(
                                                                                    this@MainDashboard
                                                                                ).preload(this@MainDashboard)
                                                                    }
                                                                    var intent = Intent(
                                                                        this@MainDashboard,
                                                                        CategoriesDisplayAct::class.java
                                                                    )
                                                                    startActivity(intent)
                                                                    categoriesDisplayAct--
                                                                } else{
                                                                    progressDialog!!.show()
                                                                    load_IA_2(
                                                                        this@MainDashboard,
                                                                        adId,
                                                                        "HomeBtn",
                                                                        popularRecycleView,
                                                                        newRecycleView,
                                                                        cFavRecycleView,
                                                                        cDecentRecycleView,
                                                                        cIslamicRecycleView,
                                                                        cMoreRecycleView,
                                                                        universityRV,
                                                                        cDankRV,
                                                                        favBtn
                                                                    )

                                                                }
                                                            } else {
                                                                var intent = Intent(
                                                                    this@MainDashboard,
                                                                    CategoriesDisplayAct::class.java
                                                                )
                                                                startActivity(intent)

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
                    } else {
                        progressDialog!!.dismiss()
                        var intent = Intent(this@MainDashboard, CategoriesDisplayAct::class.java)
                        startActivity(intent)
                    }


            }


        }
        binding.appAd.setOnClickListener{
            var uri: Uri? =null

            when (meme) {

                "Popular" -> {

                    uri= Uri.parse("https://play.google.com/store/apps/details?id=com.nextsimpledesign.wasticker.stickerislamicswhatsapp")


                }

                "New" -> {

                    uri=Uri.parse("https://play.google.com/store/apps/details?id=com.ng.collagemaker.photoeditor.photocollage")
                }
                "Decent" -> {
                    uri=Uri.parse("https://play.google.com/store/apps/details?id=com.zadeveloper.statusdownloader.whatsappstatussaver.savestatus")

                }

                "Religious" -> {
                    uri= Uri.parse("https://play.google.com/store/apps/details?id=com.nextsimpledesign.wasticker.stickerislamicswhatsapp")


                }



                "University" -> {
                    uri=Uri.parse("https://play.google.com/store/apps/details?id=com.ng.collagemaker.photoeditor.photocollage")

                }

                "Dank" -> {
                    uri=Uri.parse("https://play.google.com/store/apps/details?id=com.zadeveloper.statusdownloader.whatsappstatussaver.savestatus")

                }

            }
            try {
                if (uri!=null){
                    startActivity(Intent(Intent.ACTION_VIEW, uri))

                }


            }catch (e:Exception){
                e.message
            }

        }


    }

    private fun favFunctionality(
        popularRecycleView: RecyclerView,
        newRecycleView: RecyclerView,
        cFavRecycleView: RecyclerView,
        cDecentRecycleView: RecyclerView,
        cIslamicRecycleView: RecyclerView,
        cMoreRecycleView: RecyclerView,
        universityRV: RecyclerView,
        cDankRV: RecyclerView,
        favBtn: ImageView
    ) {
        popularRecycleView.visibility = View.INVISIBLE
        newRecycleView.visibility = View.INVISIBLE
        cFavRecycleView.visibility = View.VISIBLE
        cDecentRecycleView.visibility = View.INVISIBLE
        cIslamicRecycleView.visibility = View.INVISIBLE
        cMoreRecycleView.visibility = View.INVISIBLE
        universityRV.visibility = View.INVISIBLE
        cDankRV.visibility = View.INVISIBLE
        favBtn.setBackgroundResource(R.drawable.select_fav_new_icon_for_main)
        FavFun()
    }


    //Categories Data
    private fun categoriesData(): ArrayList<MenuItem?> {
        val dataModelList = ArrayList<MenuItem?>()
        dataModelList.add(MenuItem("Popular", 0))
        dataModelList.add(MenuItem("New", 0))
        dataModelList.add(MenuItem("Decent", 0))
        dataModelList.add(MenuItem("Religious", 0))
        dataModelList.add(MenuItem("University", 0))
        dataModelList.add(MenuItem("Dank", 0))
//        dataModelList.add(MenuItem("More", 0))
        return dataModelList
    }


    private fun categoriesInitialize(
        context: Context?,
        adapter: CategoriesAdapter?,
        recyclerView: RecyclerView,
        list: ArrayList<MenuItem?>?
    ) {
        var adapter = adapter
        adapter = CategoriesAdapter(list, context, this)
        val manager = LinearLayoutManager(context)
        manager.orientation = RecyclerView.HORIZONTAL
        val itemDecoration = ItemDecoration(12)
        recyclerView.addItemDecoration(itemDecoration)
        recyclerView.layoutManager = manager
        recyclerView.adapter = adapter
    }

    private fun adapterInit2(
        context: Context?,
        adapter: FavAdapter?,
        recyclerView: RecyclerView,
        list: ArrayList<Uri>
    ) {
        var adapter = adapter
        adapter = FavAdapter(context, list, this)
        val manager = GridLayoutManager(context, 3)
        manager.orientation = RecyclerView.VERTICAL
        recyclerView.layoutManager = manager
        recyclerView.adapter = adapter
    }


    private fun FavFun() {
        adapterInit2(this@MainDashboard, adapter, binding.cFavRecycleView, saveList!!)
    }


    fun load_IA(context: Activity?, adID: String?, cl: Class<*>?, imageUri: Uri, title: String) {
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
                    show_IA(context, cl, imageUri, title)
                }, COUNTER_TIME_AD)
            }

            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                super.onAdFailedToLoad(loadAdError)

                mInterstitialAd = null
                show_IA(context, cl, imageUri, title)

            }
        })
    }

    fun load_IA_2(

        context: Activity?, adID: String?, check: String, popularRecycleView: RecyclerView,
        newRecycleView: RecyclerView,
        cFavRecycleView: RecyclerView,
        cDecentRecycleView: RecyclerView,
        cIslamicRecycleView: RecyclerView,
        cMoreRecycleView: RecyclerView,
        universityRV: RecyclerView,
        cDankRV: RecyclerView,
        favBtn: ImageView
    ) {
        if (mInterstitialAd != null) {
            mInterstitialAd = null
        }
        val adRequest = AdRequest.Builder().build()
        val str2: String? = if (BuildConfig.DEBUG) {
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
                    show_IA_2(
                        context, check, popularRecycleView,
                        newRecycleView,
                        cFavRecycleView,
                        cDecentRecycleView,
                        cIslamicRecycleView,
                        cMoreRecycleView,
                        universityRV,
                        cDankRV,
                        favBtn
                    )
                }, COUNTER_TIME_AD)
            }

            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                super.onAdFailedToLoad(loadAdError)
                progressDialog!!.dismiss()
                mInterstitialAd = null

                show_IA_2(
                    context, check, popularRecycleView,
                    newRecycleView,
                    cFavRecycleView,
                    cDecentRecycleView,
                    cIslamicRecycleView,
                    cMoreRecycleView,
                    universityRV,
                    cDankRV,
                    favBtn
                )

            }
        })
    }

    fun show_IA(context: Activity?, cl: Class<*>?, imageUri: Uri, title: String) {
        if (mInterstitialAd != null) {
            mInterstitialAd!!.show(context!!)
            mInterstitialAd!!.fullScreenContentCallback =
                object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        mInterstitialAd = null
                        var intent = Intent(this@MainDashboard, EditActivity::class.java)
                        intent.putExtra("key", imageUri)
                        intent.putExtra("tempName", title)
                        startActivity(intent)
                        categoriesDisplayAct = 6
                    }

                    override fun onAdShowedFullScreenContent() {
                        super.onAdShowedFullScreenContent()
                        mInterstitialAd = null
                    }
                }
        }else{
            interstitialBuilder?.show(this@MainDashboard)
            interstitialBuilder =
                InterstitialBuilder.create()
                    .setAdId(com.appbrain.AdId.EXIT)
                    .setFinishOnExit(
                        this@MainDashboard
                    ).preload(this@MainDashboard)
            var intent = Intent(this@MainDashboard, EditActivity::class.java)
            intent.putExtra("key", imageUri)
            intent.putExtra("tempName", title)
            startActivity(intent)
            categoriesDisplayAct = 6
        }
    }

    fun show_IA_2(
        context: Activity?, check: String, popularRecycleView: RecyclerView, newRecycleView: RecyclerView,
        cFavRecycleView: RecyclerView, cDecentRecycleView: RecyclerView, cIslamicRecycleView: RecyclerView,
        cMoreRecycleView: RecyclerView, universityRV: RecyclerView, cDankRV: RecyclerView, favBtn: ImageView)
    {
        try {
            if (mInterstitialAd != null) {
                mInterstitialAd!!.show(context!!)
                mInterstitialAd!!.fullScreenContentCallback =
                    object : FullScreenContentCallback() {
                        override fun onAdDismissedFullScreenContent() {
                            mInterstitialAd = null
                            if (check == "HomeBtn") {
                                // onBackPressed()
                                startActivity(Intent(this@MainDashboard,CategoriesDisplayAct::class.java))
                            } else {
                                favFunctionality(
                                    popularRecycleView,
                                    newRecycleView,
                                    cFavRecycleView,
                                    cDecentRecycleView,
                                    cIslamicRecycleView,
                                    cMoreRecycleView,
                                    universityRV,
                                    cDankRV,
                                    favBtn
                                )
                            }
                            categoriesDisplayAct = 6
                        }

                        override fun onAdShowedFullScreenContent() {
                            super.onAdShowedFullScreenContent()
                            mInterstitialAd = null
                        }
                    }
            }else{

                    interstitialBuilder?.show(this@MainDashboard)
                    interstitialBuilder =
                        InterstitialBuilder.create()
                            .setAdId(com.appbrain.AdId.EXIT)
                            .setFinishOnExit(
                                this@MainDashboard
                            ).preload(this@MainDashboard)
                if (check == "HomeBtn") {
                    // onBackPressed()
                    startActivity(Intent(this@MainDashboard,CategoriesDisplayAct::class.java))
                } else {
                    favFunctionality(
                        popularRecycleView,
                        newRecycleView,
                        cFavRecycleView,
                        cDecentRecycleView,
                        cIslamicRecycleView,
                        cMoreRecycleView,
                        universityRV,
                        cDankRV,
                        favBtn
                    )
                }
                categoriesDisplayAct = 6

            }
        }
        catch (e:Exception)
        {
            Log.i("rafaqat", "show_IA_2: "+e.message)
        }

    }

    override fun itemPos(model: MenuItem, newName: Int) {
        val selectTitle = model.title
        when (selectTitle) {
            "Popular" -> {
                visibilityShow2("Popular")
            }

            "New" -> {
                visibilityShow2("New")
            }

            "Religious" -> {
                visibilityShow2("Religious")
            }

            "Decent" -> {
                visibilityShow2("Decent")
            }

            "University" -> {
                visibilityShow2("University")
            }

            "Dank" -> {
                visibilityShow2("Dank")
            }

            "More" -> {
                visibilityShow2("More")
            }
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


        inAppUpdate?.onActivityResult(requestCode, resultCode);

    }


    private fun visibilityShow(select: String) {



        val manager = GridLayoutManager(this@MainDashboard, 3)
        manager.orientation = RecyclerView.VERTICAL
        binding.popularRecycleView.layoutManager = manager
        storageRef = FirebaseStorage.getInstance().getReference("/");
        saveList = getUriListFromSharedPreferences(this@MainDashboard)

        binding.apply {

            when (select) {

                "Popular" -> {
                    meme="Popular"
                    binding.adText.text="Download Sticker Maker App"
                    popularRecycleView.visibility = View.VISIBLE
                    newRecycleView.visibility = View.INVISIBLE
                    cDecentRecycleView.visibility = View.INVISIBLE
                    cIslamicRecycleView.visibility = View.INVISIBLE
                    cFavRecycleView.visibility = View.INVISIBLE
                    cMoreRecycleView.visibility = View.INVISIBLE
                    universityRV.visibility = View.INVISIBLE
                    favBtn.setBackgroundResource(R.drawable.fav_new_icon_for_main)
                    cDankRV.visibility = View.INVISIBLE

                    //Fetch Template + Name Code
                    storageRef.listAll().addOnSuccessListener { it ->
                        for (myRef in it.prefixes) {
                            if (myRef.name == select) {
                                storageRef.child(myRef.name).listAll()
                                    .addOnSuccessListener { it ->
                                        for (item in it.items) {
                                            item.downloadUrl.addOnSuccessListener {
                                                popularList.add(
                                                    PopularModel(
                                                        item.name.removeSuffix(
                                                            ".webp"
                                                        ), it
                                                    )
                                                )
                                                adapter2 = NewAdapter2(
                                                    this@MainDashboard,
                                                    popularList,
                                                    this@MainDashboard
                                                )
                                                binding.popularRecycleView.adapter = adapter2
                                                // Retrieve the list of URIs
                                                try {

                                                    for (i in popularList.indices) {
                                                        val currentDownloadUrl =
                                                            popularList[i].imageUri
                                                        if (!saveList!!.contains(currentDownloadUrl)) {
                                                            popularList[i].fav = View.INVISIBLE
                                                            popularList[i].unFav = View.VISIBLE
                                                        } else {
                                                            popularList[i].fav = View.VISIBLE
                                                            popularList[i].unFav = View.INVISIBLE
                                                        }
                                                    }

                                                    binding.popularRecycleView.adapter = adapter2
                                                } catch (e: Exception) {
                                                    Log.i("rafaqat", "visibilityShow: " + e.message)
                                                }


                                            }
                                        }
                                    }.addOnFailureListener {

                                    }

                            }
                        }
                    }.addOnFailureListener { e ->
                        Log.i("rafaqat", "onFailure: " + e.message)
                    }


                }

                "New" -> {
                    meme="New"
                    binding.adText.text="Download Collage Maker App"

                    popularRecycleView.visibility = View.INVISIBLE
                    newRecycleView.visibility = View.VISIBLE
                    cDecentRecycleView.visibility = View.INVISIBLE
                    cIslamicRecycleView.visibility = View.INVISIBLE
                    cFavRecycleView.visibility = View.INVISIBLE
                    cMoreRecycleView.visibility = View.INVISIBLE
                    universityRV.visibility = View.INVISIBLE
                    cDankRV.visibility = View.INVISIBLE
                    favBtn.setBackgroundResource(R.drawable.fav_new_icon_for_main)

                    //Fetch Template + Name Code
                    storageRef.listAll().addOnSuccessListener { it ->
                        for (myRef in it.prefixes) {
                            if (myRef.name == select) {
                                storageRef.child(myRef.name).listAll().addOnSuccessListener { it ->
                                    for (item in it.items) {
                                        item.downloadUrl.addOnSuccessListener {
                                            newList.add(
                                                PopularModel(
                                                    item.name.removeSuffix(".webp"),
                                                    it
                                                )
                                            )
                                            adapter2 = NewAdapter2(
                                                this@MainDashboard,
                                                newList,
                                                this@MainDashboard
                                            )

                                            // Retrieve the list of URIs
                                            try {
                                                for (i in newList.indices) {
                                                    val currentDownloadUrl =
                                                        newList[i].imageUri
                                                    if (!saveList!!.contains(currentDownloadUrl)) {
                                                        newList[i].fav = View.INVISIBLE
                                                        newList[i].unFav = View.VISIBLE
                                                    } else {
                                                        newList[i].fav = View.VISIBLE
                                                        newList[i].unFav = View.INVISIBLE
                                                    }
                                                }
                                                binding.newRecycleView.adapter = adapter2
                                                Log.i("Retrieve", "visibilityShow000: " + saveList)
                                            } catch (e: Exception) {
                                                Log.i("rafaqat", "visibilityShow: " + e.message)
                                            }


                                        }
                                    }
                                }.addOnFailureListener {

                                }

                            }
                        }
                    }.addOnFailureListener { e ->
                        Log.i("rafaqat", "onFailure: " + e.message)
                    }
                }

                "Decent" -> {
                    meme="Decent"

                    binding. adText.text="Download Status Saver App"

                    popularRecycleView.visibility = View.INVISIBLE
                    newRecycleView.visibility = View.INVISIBLE
                    cDecentRecycleView.visibility = View.VISIBLE
                    cIslamicRecycleView.visibility = View.INVISIBLE
                    cFavRecycleView.visibility = View.INVISIBLE
                    cMoreRecycleView.visibility = View.INVISIBLE
                    universityRV.visibility = View.INVISIBLE
                    cDankRV.visibility = View.INVISIBLE
                    favBtn.setBackgroundResource(R.drawable.fav_new_icon_for_main)
                    //Fetch Template + Name Code
                    storageRef.listAll().addOnSuccessListener { it ->
                        for (myRef in it.prefixes) {
                            if (myRef.name == select) {
                                storageRef.child(myRef.name).listAll().addOnSuccessListener { it ->
                                    for (item in it.items) {
                                        item.downloadUrl.addOnSuccessListener {
                                            decentList.add(
                                                PopularModel(
                                                    item.name.removeSuffix(".webp"),
                                                    it
                                                )
                                            )
                                            adapter2 = NewAdapter2(
                                                this@MainDashboard,
                                                decentList,
                                                this@MainDashboard
                                            )

                                            // Retrieve the list of URIs
                                            try {
//                                                saveList = getUriListFromSharedPreferences(this@MainDashboard)
                                                for (i in decentList.indices) {
                                                    val currentDownloadUrl =
                                                        decentList[i].imageUri
                                                    if (!saveList!!.contains(currentDownloadUrl)) {
                                                        decentList[i].fav = View.INVISIBLE
                                                        decentList[i].unFav = View.VISIBLE
                                                    } else {
                                                        decentList[i].fav = View.VISIBLE
                                                        decentList[i].unFav = View.INVISIBLE
                                                    }
                                                }
                                            } catch (e: Exception) {
                                                Log.i("rafaqat", "visibilityShow: " + e.message)
                                            }

                                            binding.cDecentRecycleView.adapter = adapter2
                                        }
                                    }
                                }.addOnFailureListener {

                                }

                            }
                        }
                    }.addOnFailureListener { e ->
                        Log.i("rafaqat", "onFailure: " + e.message)
                    }

                }

                "Religious" -> {
                    meme="Religious"
                    binding.adText.text="Download Sticker Maker App"

                    popularRecycleView.visibility = View.INVISIBLE
                    newRecycleView.visibility = View.INVISIBLE
                    cDecentRecycleView.visibility = View.INVISIBLE
                    cIslamicRecycleView.visibility = View.VISIBLE
                    universityRV.visibility = View.INVISIBLE
                    cFavRecycleView.visibility = View.INVISIBLE
                    cMoreRecycleView.visibility = View.INVISIBLE
                    cDankRV.visibility = View.INVISIBLE
                    favBtn.setBackgroundResource(R.drawable.fav_new_icon_for_main)

                    //Fetch Template + Name Code
                    storageRef.listAll().addOnSuccessListener { it ->
                        for (myRef in it.prefixes) {
                            if (myRef.name == select) {
                                storageRef.child(myRef.name).listAll().addOnSuccessListener { it ->
                                    for (item in it.items) {
                                        item.downloadUrl.addOnSuccessListener {
                                            islamicList.add(
                                                PopularModel(
                                                    item.name.removeSuffix(".webp"),
                                                    it
                                                )
                                            )
                                            adapter2 = NewAdapter2(
                                                this@MainDashboard,
                                                islamicList,
                                                this@MainDashboard
                                            )

                                            // Retrieve the list of URIs
                                            try {
//                                                saveList = getUriListFromSharedPreferences(this@MainDashboard)
                                                for (i in islamicList.indices) {
                                                    val currentDownloadUrl =
                                                        islamicList[i].imageUri
                                                    if (!saveList!!.contains(currentDownloadUrl)) {
                                                        islamicList[i].fav = View.INVISIBLE
                                                        islamicList[i].unFav = View.VISIBLE
                                                    } else {
                                                        islamicList[i].fav = View.VISIBLE
                                                        islamicList[i].unFav = View.INVISIBLE
                                                    }
                                                }
                                            } catch (e: Exception) {
                                                Log.i("rafaqat", "visibilityShow: " + e.message)
                                            }

                                            binding.cIslamicRecycleView.adapter = adapter2

                                        }
                                    }
                                }.addOnFailureListener {

                                }

                            }
                        }
                    }.addOnFailureListener { e ->
                        Log.i("rafaqat", "onFailure: " + e.message)
                    }
                }

                "University" -> {
                    meme="University"
                    binding.adText.text="Download Collage Maker App"

                    popularRecycleView.visibility = View.INVISIBLE
                    newRecycleView.visibility = View.INVISIBLE
                    cDecentRecycleView.visibility = View.INVISIBLE
                    universityRV.visibility = View.VISIBLE
                    cIslamicRecycleView.visibility = View.INVISIBLE
                    cFavRecycleView.visibility = View.INVISIBLE
                    cMoreRecycleView.visibility = View.INVISIBLE
                    cDankRV.visibility = View.INVISIBLE
                    favBtn.setBackgroundResource(R.drawable.fav_new_icon_for_main)

                    //Fetch Template + Name Code
                    storageRef.listAll().addOnSuccessListener { it ->
                        for (myRef in it.prefixes) {
                            if (myRef.name == select) {
                                storageRef.child(myRef.name).listAll().addOnSuccessListener { it ->
                                    for (item in it.items) {
                                        item.downloadUrl.addOnSuccessListener {
                                            universityList.add(
                                                PopularModel(
                                                    item.name.removeSuffix(".webp"),
                                                    it
                                                )
                                            )
                                            adapter2 = NewAdapter2(
                                                this@MainDashboard,
                                                universityList,
                                                this@MainDashboard
                                            )

                                            // Retrieve the list of URIs
                                            try {
                                                for (i in universityList.indices) {
                                                    val currentDownloadUrl =
                                                        universityList[i].imageUri
                                                    if (!saveList!!.contains(currentDownloadUrl)) {
                                                        universityList[i].fav = View.INVISIBLE
                                                        universityList[i].unFav = View.VISIBLE
                                                    } else {
                                                        universityList[i].fav = View.VISIBLE
                                                        universityList[i].unFav = View.INVISIBLE
                                                    }
                                                }
                                            } catch (e: Exception) {
                                                Log.i("rafaqat", "visibilityShow: " + e.message)
                                            }

                                            binding.universityRV.adapter = adapter2
                                        }
                                    }
                                }.addOnFailureListener {

                                }

                            }
                        }
                    }.addOnFailureListener { e ->
                        Log.i("rafaqat", "onFailure: " + e.message)
                    }
                }

                "Dank" -> {
                    meme="Dank"
                    binding.adText.text="Download Status Saver App"

                    popularRecycleView.visibility = View.INVISIBLE
                    newRecycleView.visibility = View.INVISIBLE
                    cDecentRecycleView.visibility = View.INVISIBLE
                    universityRV.visibility = View.INVISIBLE
                    cIslamicRecycleView.visibility = View.INVISIBLE
                    cFavRecycleView.visibility = View.INVISIBLE
                    cMoreRecycleView.visibility = View.INVISIBLE
                    cDankRV.visibility = View.VISIBLE
                    favBtn.setBackgroundResource(R.drawable.fav_new_icon_for_main)
                    //Fetch Template + Name Code
                    storageRef.listAll().addOnSuccessListener { it ->
                        for (myRef in it.prefixes) {
                            if (myRef.name == select) {
                                storageRef.child(myRef.name).listAll().addOnSuccessListener { it ->
                                    for (item in it.items) {
                                        item.downloadUrl.addOnSuccessListener {
                                            dankList.add(
                                                PopularModel(
                                                    item.name.removeSuffix(".webp"),
                                                    it
                                                )
                                            )
                                            adapter2 = NewAdapter2(
                                                this@MainDashboard,
                                                dankList,
                                                this@MainDashboard
                                            )
                                            // Retrieve the list of URIs
                                            try {
//                                                saveList = getUriListFromSharedPreferences(this@MainDashboard)
                                                for (i in dankList.indices) {
                                                    val currentDownloadUrl =
                                                        dankList[i].imageUri
                                                    if (!saveList!!.contains(currentDownloadUrl)) {
                                                        dankList[i].fav = View.INVISIBLE
                                                        dankList[i].unFav = View.VISIBLE
                                                    } else {
                                                        dankList[i].fav = View.VISIBLE
                                                        dankList[i].unFav = View.INVISIBLE
                                                    }
                                                }
                                            } catch (e: Exception) {
                                                Log.i("rafaqat", "visibilityShow: " + e.message)
                                            }
                                            binding.cDankRV.adapter = adapter2
                                        }
                                    }
                                }.addOnFailureListener {

                                }

                            }
                        }
                    }.addOnFailureListener { e ->
                        Log.i("rafaqat", "onFailure: " + e.message)
                    }
                }

//                "More" -> {
//                    popularRecycleView.visibility = View.INVISIBLE
//                    newRecycleView.visibility = View.INVISIBLE
//                    cDecentRecycleView.visibility = View.INVISIBLE
//                    cIslamicRecycleView.visibility = View.INVISIBLE
//                    cFavRecycleView.visibility = View.INVISIBLE
//                    universityRV.visibility = View.INVISIBLE
//                    cMoreRecycleView.visibility = View.VISIBLE
//                    cDankRV.visibility = View.INVISIBLE
//                    favBtn.setBackgroundResource(R.drawable.fav_new_icon_for_main)
//                    //adapterInit(this@MainDashboard, moreAdapter, cMoreRecycleView, moreData())
//                }
            }

        }

    }

    private fun visibilityShow2(select: String) {
        val manager = GridLayoutManager(this@MainDashboard, 3)
        manager.orientation = RecyclerView.VERTICAL
        binding.popularRecycleView.layoutManager = manager
        storageRef = FirebaseStorage.getInstance().getReference("/");
        saveList = getUriListFromSharedPreferences(this@MainDashboard)


        binding.apply {

            when (select) {
                "Popular" -> {
                    meme="Popular"
                    binding.adText.text="Download Sticker Maker App"


                    popularRecycleView.visibility = View.VISIBLE
                    newRecycleView.visibility = View.INVISIBLE
                    cDecentRecycleView.visibility = View.INVISIBLE
                    cIslamicRecycleView.visibility = View.INVISIBLE
                    cFavRecycleView.visibility = View.INVISIBLE
                    cMoreRecycleView.visibility = View.INVISIBLE
                    universityRV.visibility = View.INVISIBLE
                    favBtn.setBackgroundResource(R.drawable.fav_new_icon_for_main)
                    cDankRV.visibility = View.INVISIBLE
                    if (popularList.size > 0) {
                        adapter2 = NewAdapter2(this@MainDashboard, popularList, this@MainDashboard)
                        // Retrieve the list of URIs
                        try {
                            for (i in popularList.indices) {
                                val currentDownloadUrl = popularList[i].imageUri
                                if (!saveList!!.contains(currentDownloadUrl)) {
                                    popularList[i].fav = View.INVISIBLE
                                    popularList[i].unFav = View.VISIBLE
                                } else {
                                    popularList[i].fav = View.VISIBLE
                                    popularList[i].unFav = View.INVISIBLE
                                }
                            }
                            binding.popularRecycleView.adapter = adapter2
                        } catch (e: Exception) {
                            Log.i("rafaqat", "visibilityShow: " + e.message)
                        }
                    } else {
                        //Fetch Template + Name Code
                        storageRef.listAll().addOnSuccessListener { it ->
                            for (myRef in it.prefixes) {
                                if (myRef.name == select) {
                                    storageRef.child(myRef.name).listAll()
                                        .addOnSuccessListener { it ->
                                            for (item in it.items) {
                                                item.downloadUrl.addOnSuccessListener {
                                                    popularList.add(
                                                        PopularModel(
                                                            item.name.removeSuffix(
                                                                ".webp"
                                                            ), it
                                                        )
                                                    )
                                                    adapter2 = NewAdapter2(
                                                        this@MainDashboard,
                                                        popularList,
                                                        this@MainDashboard
                                                    )
                                                    binding.popularRecycleView.adapter = adapter2
                                                    // Retrieve the list of URIs
                                                    try {
                                                        for (i in popularList.indices) {
                                                            val currentDownloadUrl =
                                                                popularList[i].imageUri
                                                            if (!saveList!!.contains(
                                                                    currentDownloadUrl
                                                                )
                                                            ) {
                                                                popularList[i].fav = View.INVISIBLE
                                                                popularList[i].unFav = View.VISIBLE
                                                            } else {
                                                                popularList[i].fav = View.VISIBLE
                                                                popularList[i].unFav =
                                                                    View.INVISIBLE
                                                            }
                                                        }
                                                        binding.popularRecycleView.adapter =
                                                            adapter2
                                                    } catch (e: Exception) {
                                                        Log.i(
                                                            "rafaqat",
                                                            "visibilityShow: " + e.message
                                                        )
                                                    }


                                                }
                                            }
                                        }.addOnFailureListener {

                                        }

                                }
                            }
                        }.addOnFailureListener { e ->
                            Log.i("rafaqat", "onFailure: " + e.message)
                        }
                    }


                }

                "New" -> {
                    meme="New"
                    binding.adText.text="Download Collage Maker App"

                    popularRecycleView.visibility = View.INVISIBLE
                    newRecycleView.visibility = View.VISIBLE
                    cDecentRecycleView.visibility = View.INVISIBLE
                    cIslamicRecycleView.visibility = View.INVISIBLE
                    cFavRecycleView.visibility = View.INVISIBLE
                    cMoreRecycleView.visibility = View.INVISIBLE
                    universityRV.visibility = View.INVISIBLE
                    cDankRV.visibility = View.INVISIBLE
                    favBtn.setBackgroundResource(R.drawable.fav_new_icon_for_main)

                    if (newList.size > 0) {
                        adapter2 = NewAdapter2(this@MainDashboard, newList, this@MainDashboard)

                        // Retrieve the list of URIs
                        try {
                            for (i in newList.indices) {
                                val currentDownloadUrl =
                                    newList[i].imageUri
                                if (!saveList!!.contains(currentDownloadUrl)) {
                                    newList[i].fav = View.INVISIBLE
                                    newList[i].unFav = View.VISIBLE
                                } else {
                                    newList[i].fav = View.VISIBLE
                                    newList[i].unFav = View.INVISIBLE
                                }
                            }
                            binding.newRecycleView.adapter = adapter2
                        } catch (e: Exception) {
                            Log.i("rafaqat", "visibilityShow: " + e.message)
                        }
                    } else {
                        //Fetch Template + Name Code
                        storageRef.listAll().addOnSuccessListener { it ->
                            for (myRef in it.prefixes) {
                                if (myRef.name == select) {
                                    storageRef.child(myRef.name).listAll()
                                        .addOnSuccessListener { it ->
                                            for (item in it.items) {
                                                item.downloadUrl.addOnSuccessListener {
                                                    newList.add(
                                                        PopularModel(
                                                            item.name.removeSuffix(
                                                                ".webp"
                                                            ), it
                                                        )
                                                    )
                                                    adapter2 = NewAdapter2(
                                                        this@MainDashboard,
                                                        newList,
                                                        this@MainDashboard
                                                    )

                                                    // Retrieve the list of URIs
                                                    try {
                                                        for (i in newList.indices) {
                                                            val currentDownloadUrl =
                                                                newList[i].imageUri
                                                            if (!saveList!!.contains(
                                                                    currentDownloadUrl
                                                                )
                                                            ) {
                                                                newList[i].fav = View.INVISIBLE
                                                                newList[i].unFav = View.VISIBLE
                                                            } else {
                                                                newList[i].fav = View.VISIBLE
                                                                newList[i].unFav = View.INVISIBLE
                                                            }
                                                        }
                                                        binding.newRecycleView.adapter = adapter2
                                                        Log.i(
                                                            "Retrieve",
                                                            "visibilityShow000: " + saveList
                                                        )
                                                    } catch (e: Exception) {
                                                        Log.i(
                                                            "rafaqat",
                                                            "visibilityShow: " + e.message
                                                        )
                                                    }


                                                }
                                            }
                                        }.addOnFailureListener {

                                        }

                                }
                            }
                        }.addOnFailureListener { e ->
                            Log.i("rafaqat", "onFailure: " + e.message)
                        }
                    }
                }

                "Decent" -> {
                    meme="Decent"
                    binding.adText.text="Download Status Saver App"

                    popularRecycleView.visibility = View.INVISIBLE
                    newRecycleView.visibility = View.INVISIBLE
                    cDecentRecycleView.visibility = View.VISIBLE
                    cIslamicRecycleView.visibility = View.INVISIBLE
                    cFavRecycleView.visibility = View.INVISIBLE
                    cMoreRecycleView.visibility = View.INVISIBLE
                    universityRV.visibility = View.INVISIBLE
                    cDankRV.visibility = View.INVISIBLE
                    favBtn.setBackgroundResource(R.drawable.fav_new_icon_for_main)

                    if (decentList.size > 0) {
                        adapter2 = NewAdapter2(
                            this@MainDashboard,
                            decentList,
                            this@MainDashboard
                        )

                        // Retrieve the list of URIs
                        try {
                            for (i in decentList.indices) {
                                val currentDownloadUrl =
                                    decentList[i].imageUri
                                if (!saveList!!.contains(currentDownloadUrl)) {
                                    decentList[i].fav = View.INVISIBLE
                                    decentList[i].unFav = View.VISIBLE
                                } else {
                                    decentList[i].fav = View.VISIBLE
                                    decentList[i].unFav = View.INVISIBLE
                                }
                            }
                        } catch (e: Exception) {
                            Log.i("rafaqat", "visibilityShow: " + e.message)
                        }

                        binding.cDecentRecycleView.adapter = adapter2
                    } else {
                        //Fetch Template + Name Code
                        storageRef.listAll().addOnSuccessListener { it ->
                            for (myRef in it.prefixes) {
                                if (myRef.name == select) {
                                    storageRef.child(myRef.name).listAll()
                                        .addOnSuccessListener { it ->
                                            for (item in it.items) {
                                                item.downloadUrl.addOnSuccessListener {
                                                    decentList.add(
                                                        PopularModel(
                                                            item.name.removeSuffix(".webp"),
                                                            it
                                                        )
                                                    )
                                                    adapter2 = NewAdapter2(
                                                        this@MainDashboard,
                                                        decentList,
                                                        this@MainDashboard
                                                    )

                                                    // Retrieve the list of URIs
                                                    try {
//                                                saveList = getUriListFromSharedPreferences(this@MainDashboard)
                                                        for (i in decentList.indices) {
                                                            val currentDownloadUrl =
                                                                decentList[i].imageUri
                                                            if (!saveList!!.contains(
                                                                    currentDownloadUrl
                                                                )
                                                            ) {
                                                                decentList[i].fav = View.INVISIBLE
                                                                decentList[i].unFav = View.VISIBLE
                                                            } else {
                                                                decentList[i].fav = View.VISIBLE
                                                                decentList[i].unFav = View.INVISIBLE
                                                            }
                                                        }
                                                    } catch (e: Exception) {
                                                        Log.i(
                                                            "rafaqat",
                                                            "visibilityShow: " + e.message
                                                        )
                                                    }

                                                    binding.cDecentRecycleView.adapter = adapter2
                                                }
                                            }
                                        }.addOnFailureListener {

                                        }

                                }
                            }
                        }.addOnFailureListener { e ->
                            Log.i("rafaqat", "onFailure: " + e.message)
                        }
                    }
                }

                "Religious" -> {
                    meme="Religious"
                    binding.adText.text="Download Sticker Maker App"

                    popularRecycleView.visibility = View.INVISIBLE
                    newRecycleView.visibility = View.INVISIBLE
                    cDecentRecycleView.visibility = View.INVISIBLE
                    cIslamicRecycleView.visibility = View.VISIBLE
                    universityRV.visibility = View.INVISIBLE
                    cFavRecycleView.visibility = View.INVISIBLE
                    cMoreRecycleView.visibility = View.INVISIBLE
                    cDankRV.visibility = View.INVISIBLE
                    favBtn.setBackgroundResource(R.drawable.fav_new_icon_for_main)

                    if (islamicList.size > 0) {
                        adapter2 = NewAdapter2(
                            this@MainDashboard,
                            islamicList,
                            this@MainDashboard
                        )

                        // Retrieve the list of URIs
                        try {
                            for (i in islamicList.indices) {
                                val currentDownloadUrl =
                                    islamicList[i].imageUri
                                if (!saveList!!.contains(currentDownloadUrl)) {
                                    islamicList[i].fav = View.INVISIBLE
                                    islamicList[i].unFav = View.VISIBLE
                                } else {
                                    islamicList[i].fav = View.VISIBLE
                                    islamicList[i].unFav = View.INVISIBLE
                                }
                            }
                            Log.i("Retrieve", "visibilityShow000: " + saveList)
                        } catch (e: Exception) {
                            Log.i("rafaqat", "visibilityShow: " + e.message)
                        }

                        binding.cIslamicRecycleView.adapter = adapter2
                    } else {
                        //Fetch Template + Name Code
                        storageRef.listAll().addOnSuccessListener { it ->
                            for (myRef in it.prefixes) {
                                if (myRef.name == select) {
                                    storageRef.child(myRef.name).listAll()
                                        .addOnSuccessListener { it ->
                                            for (item in it.items) {
                                                item.downloadUrl.addOnSuccessListener {
                                                    islamicList.add(
                                                        PopularModel(
                                                            item.name.removeSuffix(".webp"),
                                                            it
                                                        )
                                                    )
                                                    adapter2 = NewAdapter2(
                                                        this@MainDashboard,
                                                        islamicList,
                                                        this@MainDashboard
                                                    )

                                                    // Retrieve the list of URIs
                                                    try {
                                                        for (i in islamicList.indices) {
                                                            val currentDownloadUrl =
                                                                islamicList[i].imageUri
                                                            if (!saveList!!.contains(
                                                                    currentDownloadUrl
                                                                )
                                                            ) {
                                                                islamicList[i].fav = View.INVISIBLE
                                                                islamicList[i].unFav = View.VISIBLE
                                                            } else {
                                                                islamicList[i].fav = View.VISIBLE
                                                                islamicList[i].unFav =
                                                                    View.INVISIBLE
                                                            }
                                                        }
                                                        Log.i(
                                                            "Retrieve",
                                                            "visibilityShow000: " + saveList
                                                        )
                                                    } catch (e: Exception) {
                                                        Log.i(
                                                            "rafaqat",
                                                            "visibilityShow: " + e.message
                                                        )
                                                    }

                                                    binding.cIslamicRecycleView.adapter = adapter2

                                                }
                                            }
                                        }.addOnFailureListener {

                                        }

                                }
                            }
                        }.addOnFailureListener { e ->
                            Log.i("rafaqat", "onFailure: " + e.message)
                        }
                    }
                }

                "University" -> {
                    meme="University"
                    binding.adText.text="Download Collage Maker App"

                    popularRecycleView.visibility = View.INVISIBLE
                    newRecycleView.visibility = View.INVISIBLE
                    cDecentRecycleView.visibility = View.INVISIBLE
                    universityRV.visibility = View.VISIBLE
                    cIslamicRecycleView.visibility = View.INVISIBLE
                    cFavRecycleView.visibility = View.INVISIBLE
                    cMoreRecycleView.visibility = View.INVISIBLE
                    cDankRV.visibility = View.INVISIBLE
                    favBtn.setBackgroundResource(R.drawable.fav_new_icon_for_main)

                    if (universityList.size > 0) {
                        adapter2 = NewAdapter2(
                            this@MainDashboard,
                            universityList,
                            this@MainDashboard
                        )
                        try {
                            for (i in universityList.indices) {
                                val currentDownloadUrl =
                                    universityList[i].imageUri
                                if (!saveList!!.contains(currentDownloadUrl)) {
                                    universityList[i].fav = View.INVISIBLE
                                    universityList[i].unFav = View.VISIBLE
                                } else {
                                    universityList[i].fav = View.VISIBLE
                                    universityList[i].unFav = View.INVISIBLE
                                }
                            }
                        } catch (e: Exception) {
                            Log.i("rafaqat", "visibilityShow: " + e.message)
                        }

                        binding.universityRV.adapter = adapter2
                    } else {
                        //Fetch Template + Name Code
                        storageRef.listAll().addOnSuccessListener { it ->
                            for (myRef in it.prefixes) {
                                if (myRef.name == select) {
                                    storageRef.child(myRef.name).listAll()
                                        .addOnSuccessListener { it ->
                                            for (item in it.items) {
                                                item.downloadUrl.addOnSuccessListener {
                                                    universityList.add(
                                                        PopularModel(
                                                            item.name.removeSuffix(".webp"),
                                                            it
                                                        )
                                                    )
                                                    adapter2 = NewAdapter2(
                                                        this@MainDashboard,
                                                        universityList,
                                                        this@MainDashboard
                                                    )

                                                    // Retrieve the list of URIs
                                                    try {
//                                                saveList = getUriListFromSharedPreferences(this@MainDashboard)
                                                        for (i in universityList.indices) {
                                                            val currentDownloadUrl =
                                                                universityList[i].imageUri
                                                            if (!saveList!!.contains(
                                                                    currentDownloadUrl
                                                                )
                                                            ) {
                                                                universityList[i].fav =
                                                                    View.INVISIBLE
                                                                universityList[i].unFav =
                                                                    View.VISIBLE
                                                            } else {
                                                                universityList[i].fav = View.VISIBLE
                                                                universityList[i].unFav =
                                                                    View.INVISIBLE
                                                            }
                                                        }
                                                    } catch (e: Exception) {
                                                        Log.i(
                                                            "rafaqat",
                                                            "visibilityShow: " + e.message
                                                        )
                                                    }

                                                    binding.universityRV.adapter = adapter2
                                                }
                                            }
                                        }.addOnFailureListener {

                                        }

                                }
                            }
                        }.addOnFailureListener { e ->
                            Log.i("rafaqat", "onFailure: " + e.message)
                        }
                    }
                }

                "Dank" -> {
                    meme="Dank"
                    binding.adText.text="Download Status Saver App"

                    popularRecycleView.visibility = View.INVISIBLE
                    newRecycleView.visibility = View.INVISIBLE
                    cDecentRecycleView.visibility = View.INVISIBLE
                    universityRV.visibility = View.INVISIBLE
                    cIslamicRecycleView.visibility = View.INVISIBLE
                    cFavRecycleView.visibility = View.INVISIBLE
                    cMoreRecycleView.visibility = View.INVISIBLE
                    cDankRV.visibility = View.VISIBLE
                    favBtn.setBackgroundResource(R.drawable.fav_new_icon_for_main)

                    if (dankList.size > 0) {
                        adapter2 = NewAdapter2(
                            this@MainDashboard,
                            dankList,
                            this@MainDashboard
                        )
                        // Retrieve the list of URIs
                        try {
                            for (i in dankList.indices) {
                                val currentDownloadUrl =
                                    dankList[i].imageUri
                                if (!saveList!!.contains(
                                        currentDownloadUrl
                                    )
                                ) {
                                    dankList[i].fav = View.INVISIBLE
                                    dankList[i].unFav = View.VISIBLE
                                } else {
                                    dankList[i].fav = View.VISIBLE
                                    dankList[i].unFav = View.INVISIBLE
                                }
                            }
                        } catch (e: Exception) {
                            Log.i(
                                "rafaqat",
                                "visibilityShow: " + e.message
                            )
                        }
                        binding.cDankRV.adapter = adapter2
                    } else {
                        //Fetch Template + Name Code
                        storageRef.listAll().addOnSuccessListener { it ->
                            for (myRef in it.prefixes) {
                                if (myRef.name == select) {
                                    storageRef.child(myRef.name).listAll()
                                        .addOnSuccessListener { it ->
                                            for (item in it.items) {
                                                item.downloadUrl.addOnSuccessListener {
                                                    dankList.add(
                                                        PopularModel(
                                                            item.name.removeSuffix(".webp"),
                                                            it
                                                        )
                                                    )
                                                    adapter2 = NewAdapter2(
                                                        this@MainDashboard,
                                                        dankList,
                                                        this@MainDashboard
                                                    )
                                                    // Retrieve the list of URIs
                                                    try {
                                                        for (i in dankList.indices) {
                                                            val currentDownloadUrl =
                                                                dankList[i].imageUri
                                                            if (!saveList!!.contains(
                                                                    currentDownloadUrl
                                                                )
                                                            ) {
                                                                dankList[i].fav = View.INVISIBLE
                                                                dankList[i].unFav = View.VISIBLE
                                                            } else {
                                                                dankList[i].fav = View.VISIBLE
                                                                dankList[i].unFav = View.INVISIBLE
                                                            }
                                                        }
                                                    } catch (e: Exception) {
                                                        Log.i(
                                                            "rafaqat",
                                                            "visibilityShow: " + e.message
                                                        )
                                                    }
                                                    binding.cDankRV.adapter = adapter2
                                                }
                                            }
                                        }.addOnFailureListener {

                                        }

                                }
                            }
                        }.addOnFailureListener { e ->
                            Log.i("rafaqat", "onFailure: " + e.message)
                        }
                    }
                }

//                "More" -> {
//                    popularRecycleView.visibility = View.INVISIBLE
//                    newRecycleView.visibility = View.INVISIBLE
//                    cDecentRecycleView.visibility = View.INVISIBLE
//                    cIslamicRecycleView.visibility = View.INVISIBLE
//                    cFavRecycleView.visibility = View.INVISIBLE
//                    universityRV.visibility = View.INVISIBLE
//                    cMoreRecycleView.visibility = View.VISIBLE
//                    cDankRV.visibility = View.INVISIBLE
//                    favBtn.setBackgroundResource(R.drawable.fav_new_icon_for_main)
//                    //adapterInit(this@MainDashboard, moreAdapter, cMoreRecycleView, moreData())
//                }
            }

        }

    }

    override fun main_item_image_click(popModel: PopularModel?) {

        if (packageCheck) {
            ref!!.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (dataSnapshot in snapshot.children) {
                            val key = dataSnapshot.key
                            if (key == Constant.MAIN_ACT) {
                                for (s in dataSnapshot.children) {
                                    if (s.key == Constant.INTERSTITIAL) {
                                        for (ss in s.children) {
                                            val model: AdModel? = ss.getValue(AdModel::class.java)
                                            var adId: String
                                            if (ss.key == TemplateItemClick) {
                                                if (model?.status == "true") {
                                                    adId = if (BuildConfig.DEBUG) {
                                                        AdId.AD_MOB_INTERSTITIAL_TEST
                                                    } else {
                                                        model.adId
                                                    }
                                                    if (categoriesDisplayAct > 0) {
                                                        progressDialog!!.dismiss()
                                                        if (categoriesDisplayAct==3){
                                                            interstitialBuilder?.show(this@MainDashboard)
                                                            interstitialBuilder =
                                                                InterstitialBuilder.create()
                                                                    .setAdId(com.appbrain.AdId.EXIT)
                                                                    .setFinishOnExit(
                                                                        this@MainDashboard
                                                                    ).preload(this@MainDashboard)
                                                        }

                                                        itemClickFun(popModel)
                                                        categoriesDisplayAct--
                                                    } else {
                                                        progressDialog!!.show()
                                                        load_IA(
                                                            this@MainDashboard,
                                                            adId,
                                                            EditActivity::class.java,
                                                            popModel!!.imageUri,
                                                            popModel.title
                                                        )
                                                    }
                                                } else {
                                                    itemClickFun(popModel)

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
        } else {
            progressDialog!!.dismiss()
            itemClickFun(popModel)
        }

    }

    override fun itemFav(model: PopularModel?, fav: ImageView?, unFav: ImageView?, position: Int) {
        val i = model?.imageUri
        index = saveList!!.indexOf(i)
        if (!saveList!!.contains(i)) {
            saveList!!.add(i!!)
            saveUriListToSharedPreferences(this, saveList!!)
            model.fav = View.VISIBLE
            model.unFav = View.INVISIBLE
            fav?.visibility = model.fav
            unFav?.visibility = model.unFav
            FavFun()
        } else {
            saveList!!.removeAt(index)
            saveUriListToSharedPreferences(this, saveList!!)
            model?.fav = View.INVISIBLE
            model?.unFav = View.VISIBLE
            fav?.visibility = model?.fav!!
            unFav?.visibility = model.unFav
            FavFun()
        }
    }

    override fun favItemClick(model: Uri?) {
        val path = model?.lastPathSegment
        val removePrefix: String
        var removeSuffix: String? = null
        val intent = Intent(this, EditActivity::class.java)

        if (path!!.startsWith("Popular")) {
            removePrefix = path.removePrefix("Popular/")
            removeSuffix = removePrefix.removeSuffix(".webp")
        } else if (path.startsWith("New")) {
            removePrefix = path.removePrefix("New/")
            removeSuffix = removePrefix.removeSuffix(".webp")
        } else if (path.startsWith("Decent")) {
            removePrefix = path.removePrefix("Decent/")
            removeSuffix = removePrefix.removeSuffix(".webp")
        } else if (path.startsWith("Religious")) {
            removePrefix = path.removePrefix("Religious/")
            removeSuffix = removePrefix.removeSuffix(".webp")
        } else if (path.startsWith("University")) {
            removePrefix = path.removePrefix("University/")
            removeSuffix = removePrefix.removeSuffix(".webp")
        } else if (path.startsWith("Dank")) {
            removePrefix = path.removePrefix("Dank/")
            removeSuffix = removePrefix.removeSuffix(".webp")
        }

        intent.putExtra("key", model)
        intent.putExtra("tempName", removeSuffix)
        startActivity(intent)
    }


    private fun itemClickFun(model: PopularModel?) {
        var intent = Intent(this, EditActivity::class.java)
        intent.putExtra("key", model?.imageUri)
        intent.putExtra("tempName", model?.title)
        startActivity(intent)

    }


    //Runtime Permission
    private fun runTimePermission() {
        try {
            ActivityCompat.requestPermissions(
                this@MainDashboard,
                permissions(),
                permissionRequestCode
            );
        }catch (e:Exception){
            e.printStackTrace()
        }


    }

    //Request Permission
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        try {
            if (requestCode == permissionRequestCode) {
                if (grantResults.isNotEmpty()) {
                    Log.i("rafaqat", "Granted: ")
                    permissionCheck = false

                    ImagePicker.with(this@MainDashboard)
                        .crop()                    //Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(
                            1080,
                            1080
                        )    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start()

                } else {
                    runTimePermission()

                }

            }
        }catch (e:Exception){
            e.printStackTrace()
        }

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

    override fun onStart() {
        super.onStart()
        valueChecker()
    }

    override fun onResume() {
        super.onResume()
        inAppUpdate!!.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        inAppUpdate!!.onDestroy()
    }

    private fun valueChecker() {
        val ex: ExecutorService = Executors.newSingleThreadScheduledExecutor()
        ex.execute { packageCheck = isInternetAvailable(this@MainDashboard) }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}
