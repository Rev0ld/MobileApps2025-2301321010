package com.example.reviewapp

import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class MapFragment : Fragment() {

    private var name: String = ""
    private var rating: String = ""
    private var review: String = ""

    private var lat: Double? = null
    private var lon: Double? = null

    private var googleMap: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        name = requireArguments().getString(ARG_NAME, "")
        rating = requireArguments().getString(ARG_RATING, "")
        review = requireArguments().getString(ARG_REVIEW, "")
        val args = requireArguments()
        lat = if (args.containsKey(ARG_LAT)) args.getDouble(ARG_LAT) else null
        lon = if (args.containsKey(ARG_LON)) args.getDouble(ARG_LON) else null
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map)
                as SupportMapFragment

        mapFragment.getMapAsync { map ->
            googleMap = map
            showLocation()
        }
    }
    private fun showLocation() {
        googleMap?.let { map ->
            when {
                lat != null && lon != null  -> {
                    val point = LatLng(lat!!, lon!!)
                    map.clear()
                    map.addMarker(MarkerOptions().position(point).title(name))
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 15f))
                }
            }
        }
    }

    companion object {
        private const val ARG_NAME = "ARG_NAME"
        private const val ARG_RATING = "ARG_RATING"
        private const val ARG_REVIEW = "ARG_REVIEW"
        private const val ARG_LAT = "ARG_LAT"
        private const val ARG_LON = "ARG_LON"

        fun newInstance(name: String,rating: String,review: String, lat: Double?, lon: Double?) = MapFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_NAME, name)
                putString(ARG_RATING, rating)
                putString(ARG_REVIEW, review)
                lat?.let { putDouble(ARG_LAT, it) }
                lon?.let { putDouble(ARG_LON, it) }
            }
        }
    }
}