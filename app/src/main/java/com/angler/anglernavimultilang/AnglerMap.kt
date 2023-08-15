package com.angler.anglernavimultilang

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import androidx.appcompat.widget.Toolbar
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.*

class AnglerMap : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var mapView: MapView
    private lateinit var imageOverlay: ImageView
    private lateinit var googleMap: GoogleMap
    private lateinit var locationEditText: EditText
    private lateinit var dropPinButton: Button
    private lateinit var distanceTextView: TextView
    private var currentLocation: LatLng? = null
    private val PERMISSION_REQUEST_CODE = 123

    private var currentMarker: Marker? = null
    private var destinationMarker: Marker? = null
    private var polyline: Polyline? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_angler_map)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_REQUEST_CODE)
        }


        mapView = findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
        mapView.setOnClickListener {
            toggleImageOverlayVisibility(false)
        }
        locationEditText = findViewById(R.id.locationEditText)
        dropPinButton = findViewById(R.id.dropPinButton)
        distanceTextView = findViewById(R.id.distanceTextView)
        imageOverlay = findViewById(R.id.imageOverlay)
        imageOverlay.setOnClickListener {
            toggleImageOverlayVisibility(false)
        }

        dropPinButton.setOnClickListener {
            dropPinsAndDrawRoute()
        }
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, you can now request the current location in onMapReady
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.setOnMarkerClickListener(this)

        googleMap.setOnMapClickListener { point ->
            toggleImageOverlayVisibility(false)
        }


        // Request current location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    val defaultRoutePoints = loadRouteFromGpxFile()
                    loadAndDisplayRoute(defaultRoutePoints)
                    currentLocation = LatLng(location.latitude, location.longitude)
                    googleMap.addMarker(MarkerOptions().position(currentLocation!!).title("Current Location"))
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15f))
                }
            }
        }

    }

    override fun onMarkerClick(marker: Marker): Boolean {
        if (marker == destinationMarker) {
            toggleImageOverlayVisibility(true)
        } else {
            toggleImageOverlayVisibility(false)
        }
        return true
    }



    private fun toggleImageOverlayVisibility(show: Boolean) {
        Log.d("ImageOverlay", "Toggling overlay visibility: $show")

        if (show) {
            imageOverlay.visibility = View.VISIBLE
            // Adjust overlay position and size
            val layoutParams = imageOverlay.layoutParams as FrameLayout.LayoutParams
            layoutParams.gravity = Gravity.CENTER
            layoutParams.topMargin = resources.getDimensionPixelSize(R.dimen.overlay_top_margin)
            layoutParams.bottomMargin = resources.getDimensionPixelSize(R.dimen.overlay_bottom_margin)
            imageOverlay.layoutParams = layoutParams
        } else {
            imageOverlay.visibility = View.INVISIBLE
        }
    }





    private fun loadAndDisplayRoute(routePoints: List<LatLng>) {
        val routePoints: List<LatLng> = loadRouteFromGpxFile()

        googleMap.addPolyline(
            PolylineOptions()
                .addAll(routePoints)
                .color(Color.RED)
        )
    }

    private fun loadRouteFromGpxFile(): List<LatLng> {
        val routePoints = mutableListOf<LatLng>()

        // Adding example coordinates
        routePoints.add(LatLng(13.0827, 80.2707))
        routePoints.add(LatLng(9.9252, 78.1198))
        routePoints.add(LatLng(9.9696, 77.2532))

        // Add more route points as needed

        return routePoints
    }

    private fun dropPinsAndDrawRoute() {
        val enteredLocation = locationEditText.text.toString()
        val geocoder = Geocoder(this)


        val addresses: List<Address>? = geocoder.getFromLocationName(enteredLocation, 1)

        if (addresses != null && addresses.isNotEmpty()) {
            val destination = LatLng(addresses[0].latitude, addresses[0].longitude)

            if (currentLocation != null) {
                // Clear previous markers and polyline
                googleMap.clear()

                // Add markers and draw polyline
                currentMarker = googleMap.addMarker(MarkerOptions().position(currentLocation!!).title("Current Location"))
                destinationMarker = googleMap.addMarker(MarkerOptions().position(destination).title("Destination"))
                polyline = googleMap.addPolyline(
                    PolylineOptions()
                        .add(currentLocation, destination)
                        .color(Color.BLUE)
                        .geodesic(true)
                        .pattern(listOf(Dash(30f), Gap(20f)))
                )

                // Calculate and display distance
                val distance = calculateDistance(currentLocation, destination)
                distanceTextView.text = String.format("Distance: %.2f km", distance)

                // Adjust camera to include both markers
                adjustCameraToIncludeMarkers(currentMarker!!, destinationMarker!!)
            } else {
                Toast.makeText(this, "Current location not available", Toast.LENGTH_SHORT).show()
            }

            // Move camera
            val cameraPosition = CameraUpdateFactory.newLatLngZoom(destination, 15f)
            googleMap.moveCamera(cameraPosition)
        } else {
            Toast.makeText(this, "Location not found", Toast.LENGTH_SHORT).show()
        }
    }



    private fun adjustCameraToIncludeMarkers(marker1: Marker, marker2: Marker) {
        val builder = LatLngBounds.builder()
        builder.include(marker1.position)
        builder.include(marker2.position)
        val bounds = builder.build()

        val padding = 100 // Adjust padding as needed
        val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding)
        googleMap.animateCamera(cameraUpdate)
    }


    private fun calculateDistance(start: LatLng?, end: LatLng?): Float {
        if (start != null && end != null) {
            val results = FloatArray(1)
            Location.distanceBetween(start.latitude, start.longitude, end.latitude, end.longitude, results)
            return results[0] / 1000 // Convert to kilometers
        }
        return 0f
    }

}
