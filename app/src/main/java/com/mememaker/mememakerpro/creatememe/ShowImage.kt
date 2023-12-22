package com.mememaker.mememakerpro.creatememe

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.mememaker.mememakerpro.creatememe.activity.MainDashboard
import com.mememaker.mememakerpro.creatememe.databinding.ActivityShowImageBinding
import java.io.File
import java.io.IOException

class ShowImage : AppCompatActivity() {
    private lateinit var intent: Intent
    private var path: String? = null
    private var memeName: String? = null
    private lateinit var binding:ActivityShowImageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_show_image)
        intent = getIntent()
        path = intent.getStringExtra("image")
        memeName = intent.getStringExtra("memeName")


        binding.apply {

            navMenu.setOnClickListener {
                onBackPressed()
            }

            title.text=memeName

            Glide.with(this@ShowImage)
                .load(path)
                .into(image)


            card1.setOnClickListener {
                ShareCompat.IntentBuilder(this@ShowImage).setStream(Uri.parse(path)).setType("image/*")
                    .setChooserTitle("Share Image").startChooser()
            }

            delete.setOnClickListener {
                val dialog = AlertDialog.Builder(this@ShowImage, R.style.AlertDialogStyle)
                    .setTitle("Delete")
                    .setCancelable(false)
                    .setMessage("Do you really want to delete this class?")
                    .setPositiveButton(android.R.string.yes) { dialog, which -> //String s = textView.getText().toString();
                        val file = File(path)
                        file.delete()
                        val intent = Intent(this@ShowImage, MainDashboard::class.java)
                        startActivity(intent)
                        finish()
                        if (file.exists()) {
                            try {
                                file.canonicalFile.delete()
                                val intent1 = Intent(this@ShowImage, MainDashboard::class.java)
                                startActivity(intent1)
                                finish()
                            } catch (e: IOException) {
                            }
                            if (file.exists()) {
                                applicationContext.deleteFile(file.name)
                                val intent2 = Intent(this@ShowImage, MainDashboard::class.java)
                                startActivity(intent2)
                                finish()
                            }
                        }
                    }
                    .setNegativeButton(android.R.string.no, null)
                    .create()
                dialog.show()
            }
        }



    }
}