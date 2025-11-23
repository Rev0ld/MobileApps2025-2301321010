package com.example.reviewapp.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.reviewapp.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapPickerActivity : AppCompatActivity(), OnMapReadyCallback {

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }

    private lateinit var map: GoogleMap
    private var selectedLatLng: LatLng? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var initialLocation: LatLng = LatLng(0.0, 0.0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_picker)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        findViewById<Button>(R.id.confirmBtn).setOnClickListener {
            selectedLatLng?.let {
                val result = Intent()
                result.putExtra("lat", it.latitude)
                result.putExtra("lng", it.longitude)
                setResult(RESULT_OK, result)
                finish()
            }
        }

        checkLocationPermissionAndFetchLocation()
    }

    private fun checkLocationPermissionAndFetchLocation() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fetchLastKnownLocation()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun fetchLastKnownLocation() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        initialLocation = LatLng(location.latitude, location.longitude)
                        if (::map.isInitialized) {
                            map.clear() // Clear previous markers if any
                            map.addMarker(MarkerOptions().position(initialLocation).title("Your Location"))

                            moveCameraToInitialLocation()
                            selectedLatLng = initialLocation // set as selected
                        }
                    } else {
                        // Location is null, fallback to default
                        if (::map.isInitialized) {
                            moveCameraToInitialLocation()
                        }
                    }
                }
                .addOnFailureListener {
                    // Handle failure gracefully
                    if (::map.isInitialized) {
                        moveCameraToInitialLocation()
                    }
                }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchLastKnownLocation()
            } else {
                // Permission denied, fallback to default 0.0,0.0
                if (::map.isInitialized) {
                    moveCameraToInitialLocation()
                }
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        moveCameraToInitialLocation()

        map.uiSettings.isZoomControlsEnabled = true

        map.setOnMapClickListener { latLng ->
            map.clear()
            map.addMarker(MarkerOptions().position(latLng))
            selectedLatLng = latLng
        }
    }

    private fun moveCameraToInitialLocation() {

        // If we still have the default world-view coordinates, zoom out
        val zoom = if (initialLocation.latitude == 0.0 && initialLocation.longitude == 0.0) {
            2f   // Global zoom, user can move freely
        } else {
            15f  // User’s real GPS location
        }

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(initialLocation, zoom))
    }
}