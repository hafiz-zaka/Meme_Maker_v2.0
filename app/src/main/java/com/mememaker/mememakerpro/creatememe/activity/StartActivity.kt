package com.mememaker.mememakerpro.creatememe.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mememaker.mememakerpro.creatememe.BuildConfig
import com.mememaker.mememakerpro.creatememe.Constant
import com.mememaker.mememakerpro.creatememe.Constant.START_ACT
import com.mememaker.mememakerpro.creatememe.R
import com.mememaker.mememakerpro.creatememe.adsManager.AdId
import com.mememaker.mememakerpro.creatememe.adsManager.RafaqatAdMobManager
import com.mememaker.mememakerpro.creatememe.databinding.ActivityStartBinding
import com.mememaker.mememakerpro.creatememe.model.AdModel

class StartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStartBinding
    private var ref: DatabaseReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_start)
        ref = FirebaseDatabase.getInstance().reference.child(Constant.ADMOB_TEST_AD)

        try {
            ref!!.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (dataSnapshot in snapshot.children) {
                            val model: AdModel? = dataSnapshot.getValue(AdModel::class.java)
                            var key = dataSnapshot.key
                                if (key == START_ACT) {
                                    var adId:String
                                    if (model!!.status == "true") {
                                        adId = if (BuildConfig.DEBUG) {
                                            AdId.AD_MOB_NATIVE_TEST
                                        } else {
                                            model.adId
                                        }
                                        //Load and Show AdMob Native
                                        RafaqatAdMobManager.getInstance().loadNative(this@StartActivity,binding.nativeTemplate,adId)
                                        binding.advertisingArea.visibility = View.INVISIBLE
                                        binding.nativeTemplate.visibility = View.VISIBLE
                                    }

                                    else {
                                        // binding.advertisingArea.visibility = View.VISIBLE
                                        binding.nativeTemplate.visibility = View.INVISIBLE

                                    }
                                }

                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        }catch (e:Exception){}

        binding.privacyPolicy.setOnClickListener {

            val alert = AlertDialog.Builder(this@StartActivity)
            alert.setTitle("Privacy Policy")
            val wv = WebView(this@StartActivity)
            wv.settings.javaScriptEnabled = true
            wv.loadUrl("https://docs.google.com/document/d/1SEbdpQ3DibjFk6RTY72Gh-Tee9zuHAkPA2sECaORWjI/edit")
            wv.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView, request: String): Boolean {
                    view.settings.javaScriptEnabled = true
                    view.loadUrl(request)

                    return true
                }

        }
            alert.setView(wv)
            alert.setNegativeButton("Close") { dialog, _ -> dialog.dismiss() }
            alert.show()
        }
        binding.start.setOnClickListener {
            if (binding.checkBox.isChecked) {
                startActivity(Intent(this@StartActivity, MainDashboard::class.java))
                finish()
            } else {
                Toast.makeText(this, "Select Check Box", Toast.LENGTH_SHORT).show()
            }
        }
        binding.help.setOnClickListener {
            if (binding.checkBox.isChecked) {
                startActivity(Intent(this@StartActivity, HelpActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Select Check Box", Toast.LENGTH_SHORT).show()
            }


        }
    }
}