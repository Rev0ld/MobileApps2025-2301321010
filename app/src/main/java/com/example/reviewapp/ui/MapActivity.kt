package com.example.reviewapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.reviewapp.ui.MapPagerAdapter
import com.example.reviewapp.R
import com.google.android.material.tabs.TabLayout

class MapActivity : AppCompatActivity(){
    lateinit var pager: ViewPager2
    lateinit var tabs: TabLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        pager = findViewById(R.id.viewPager)
        tabs = findViewById(R.id.tabLayout)
        val name = intent.getStringExtra("extra_name") ?: ""
        val rating = intent.getStringExtra("extra_rating") ?: ""
        val review = intent.getStringExtra("extra_review") ?: ""
        val lat = intent.getDoubleExtra("extra_lat", 0.0)
        val lon = intent.getDoubleExtra("extra_lon", 0.0)
        val adapter = MapPagerAdapter(this, name, rating, review, lat, lon)
        pager.adapter = adapter
    }
}