package com.mememaker.mememakerpro.creatememe

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mememaker.mememakerpro.creatememe.Constant.ADMOB_TEST_AD
import com.mememaker.mememakerpro.creatememe.Constant.SAVE_ACT_NATIVE
import com.mememaker.mememakerpro.creatememe.Constant.SaveDisplayAct
import com.mememaker.mememakerpro.creatememe.adapters.MyAdapter
import com.mememaker.mememakerpro.creatememe.adsManager.AdId
import com.mememaker.mememakerpro.creatememe.adsManager.RafaqatAdMobManager
import com.mememaker.mememakerpro.creatememe.constant.Constant
import com.mememaker.mememakerpro.creatememe.databinding.ActivitySavedImagesBinding
import com.mememaker.mememakerpro.creatememe.model.AdModel
import com.mememaker.mememakerpro.creatememe.model.ImageModel
import com.mememaker.mememakerpro.creatememe.myinterface.MyViewListener
import java.io.File

class SavedImages : AppCompatActivity(), MyViewListener {
    var path: File? = null
    var list: ArrayList<ImageModel>? = null
    private var adapter: MyAdapter? = null
    private var ref: DatabaseReference? = null

    private lateinit var binding:ActivitySavedImagesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_saved_images)

        ref = FirebaseDatabase.getInstance().reference.child(ADMOB_TEST_AD)

        try {
            ref!!.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (dataSnapshot in snapshot.children) {
                            val key = dataSnapshot.key
                            if (key == SaveDisplayAct) {
                                for (s in dataSnapshot.children) {
                                    if (s.key == SAVE_ACT_NATIVE) {
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
                                                this@SavedImages,
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
            navMenu.setOnClickListener {
                onBackPressed()
            }

        }

        path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
//        val layoutManager = LinearLayoutManager(this)
//        layoutManager.orientation = LinearLayoutManager.VERTICAL
//        binding.recycleView.layoutManager = layoutManager
        binding.recycleView.layoutManager = GridLayoutManager(this,3)
        list = ArrayList()
        adapter = MyAdapter(this, list, this)
        val s = path.toString() + "/" + "Meme Maker"
        val f = File(s)
        val files = f.listFiles()
        if (f != null) {
            try {
                //Check pdf is r not
//                for (File file : files) {
//                    if (file.isDirectory() && !file.isHidden()) {
//                        list.add(new ImageModel(file.getName(), file.getPath(), file.length()));
//                    }
//                }
                for (file in files) {
                    if (file.path.endsWith(".png") || file.path.endsWith(".jpg")) {
                        list!!.add(ImageModel(file.name, file.path, file.length()))
                    }
                }
            } catch (e: Exception) {
                Log.i(Constant.MY_TAG, "onCreate: " + e.message)
            }
        } else {
        }
        binding.recycleView.adapter = adapter
        adapter!!.notifyDataSetChanged()
    }

    override fun viewImg(path: ImageModel) {
        val intent = Intent(this@SavedImages, ShowImage::class.java)
        intent.putExtra("image", path.path)
        intent.putExtra("memeName", path.title)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}