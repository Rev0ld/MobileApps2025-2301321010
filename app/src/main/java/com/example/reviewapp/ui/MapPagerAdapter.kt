package com.example.reviewapp.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class MapPagerAdapter(
    fa: FragmentActivity,
    private val name: String,
    private val rating: String,
    private val review: String,
    private val lat: Double?,
    private val lon: Double?
) : FragmentStateAdapter(fa){
    override fun getItemCount() = 1
    override fun createFragment(position: Int): Fragment {
        return MapFragment.newInstance(name, rating, review, lat, lon)
    }
}