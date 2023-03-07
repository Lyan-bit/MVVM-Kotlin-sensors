package com.example.har

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.example.har.adapter.MyPagerAdapter
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val myPagerAdapter = MyPagerAdapter(this, supportFragmentManager)
        val viewpager: ViewPager = findViewById(R.id.view_pager)
        viewpager.adapter = myPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewpager)
    }
}
