package com.mememaker.mememakerpro.creatememe.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager
import com.mememaker.mememakerpro.creatememe.R
import com.mememaker.mememakerpro.creatememe.databinding.ActivityHelpBinding

class HelpActivity : AppCompatActivity() {
    var images = arrayOf(
        R.drawable.screen_1, R.drawable.screen_2,
        R.drawable.screen_3, R.drawable.screen_4,
        R.drawable.screen_5, R.drawable.screen_6
    )

    private lateinit var binding:ActivityHelpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_help)

        supportActionBar?.hide()
        binding.tablayout.setupWithViewPager(binding.pager, true)

        val viewPagerAdapter = ViewPagerAdapter(this, images)
        binding.pager.adapter = viewPagerAdapter

        binding.pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    5 -> {
//                        if (var2 == 2) {
//                            binding.start.visibility = View.GONE
//                            binding.tablayout.visibility = View.VISIBLE
//                        } else if (var2 == 1) {
//                            // it will run
                        binding.start.visibility = View.VISIBLE
                        binding.tablayout.visibility = View.GONE
//                        }
//                        binding.next.visibility = View.INVISIBLE
                    }
                    0 -> {
//                        binding.skip.visibility = View.INVISIBLE
//                        binding.back.visibility = View.INVISIBLE
                        binding.tablayout.visibility = View.VISIBLE
                    }
                    else -> {
                        binding.start.visibility = View.GONE
                        binding.tablayout.visibility = View.VISIBLE
//                        binding.next.visibility = View.VISIBLE
//                        binding.skip.visibility = View.VISIBLE
//                        binding.back.visibility = View.VISIBLE
                    }
                }
            }

            override fun onPageScrollStateChanged(state: Int) { }
        })

        binding.start.setOnClickListener {
            val intent = Intent(this, MainDashboard::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId== android.R.id.home){
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}