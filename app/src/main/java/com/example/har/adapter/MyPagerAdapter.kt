package com.example.har.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.har.fragments.ClassificationFragment
import com.example.har.fragments.SensorDataFragment

class MyPagerAdapter(private val mContext: Context, fm: FragmentManager?) : FragmentPagerAdapter(fm!!) {

    companion object {
        private val TAB_TITLES = arrayOf("Accelerometer","Classification")
    }

   override fun getItem(position: Int): Fragment {
        // instantiate a fragment for the page.
            return when (position) {
                 0 -> { 
                    AccelerometerClassificationFragment.newInstance(mContext) 
                }
                 1 -> { 
                    ClassificationFragment.newInstance(mContext) 
                }
                else -> AccelerometerClassificationFragment.newInstance(mContext) 
             }
    }

    override fun getPageTitle(position: Int): CharSequence {
        return TAB_TITLES[position]
    }

    override fun getCount(): Int {
        return 2
    }
}

