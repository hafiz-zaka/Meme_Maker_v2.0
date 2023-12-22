package com.mememaker.mememakerpro.creatememe.activity

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.mememaker.mememakerpro.creatememe.databinding.ViewpagerItemBinding

class ViewPagerAdapter(private val activity: Activity, private val imagesArray: Array<Int>) :
    PagerAdapter() {
    override fun getCount(): Int {
        return imagesArray.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object` as View
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val binding=ViewpagerItemBinding.inflate(activity.layoutInflater)
        val view = binding.root
        binding.img.setImageResource(imagesArray[position])
        container.addView(view)
        return view
    }

    override fun destroyItem(container: View, position: Int, `object`: Any) {
        (container as ViewPager).removeView(`object` as View)
    }
}