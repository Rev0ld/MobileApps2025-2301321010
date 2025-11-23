package com.example.reviewapp

import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

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