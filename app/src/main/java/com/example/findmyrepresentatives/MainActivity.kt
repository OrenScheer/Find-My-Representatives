package com.example.findmyrepresentatives

import android.Manifest
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * The main activity of the app.
 * This screen gives users a search box where they can search for their representatives by postal code.
 * @author Oren Scheer
 */
class MainActivity : AppCompatActivity() {
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private var currentLocation: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        search_box.setOnEditorActionListener{ _, actionId, _ ->
            when(actionId) {
                EditorInfo.IME_ACTION_SEND -> {
                    button.performClick()
                    true
                }
                else -> false
            }
        }
    }

    /**
     * The function called when the search button is pressed by a postal code.
     * @param view the source of the click
     */
    fun enterPostalCode(view: View) {
        var postalCode = search_box.text.toString().trim()
        val errorMessage = findViewById<TextView>(R.id.postal_code_error)
        if (!Utils.isValidPostalCode(postalCode)) {
            postal_code_error.text = getString(R.string.postal_code_error, postalCode)
            postal_code_error.visibility = View.VISIBLE
        }
        else {
            errorMessage.visibility = View.INVISIBLE
            postalCode = postalCode.replace(" ", "")
            postalCode = "postcodes/" + postalCode.toUpperCase(Locale.CANADA)
            val intent = Intent(this, ResultsActivity::class.java).apply {
                putExtra("query", postalCode)
            }
            startActivity(intent)
        }
    }

    /**
     * The function called when the find location button is pressed.
     * @param view the source of the click
     */
    fun useLocation(view: View) = runWithPermissions(Manifest.permission.ACCESS_FINE_LOCATION) {
        try {
            val locationRequest = LocationRequest().apply { // We will only be requesting the location once
                interval = TimeUnit.SECONDS.toMillis(60)
                fastestInterval = TimeUnit.SECONDS.toMillis(30)
                maxWaitTime = TimeUnit.SECONDS.toMillis(2)
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }

            val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
            val client: SettingsClient = LocationServices.getSettingsClient(this)
            val task:  Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build()) // Check if settings allow for access to location
            task.addOnFailureListener() { exception ->
                if (exception is ResolvableApiException){
                    location_error.visibility = View.VISIBLE // Location is probably turned off
                    fusedLocationProviderClient.removeLocationUpdates(locationCallback) // We need to wait for the user to press the button again once location is turned on
                }
            }

            locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult?) {
                    super.onLocationResult(locationResult)
                    if (locationResult?.lastLocation != null) {
                        currentLocation = locationResult.lastLocation
                        Log.d("current lat", currentLocation!!.latitude.toString())
                        val intent = Intent(applicationContext, ResultsActivity::class.java).apply {
                            putExtra("query", "representatives/") // URL to use for the API call
                            putExtra("latlong",     currentLocation!!.latitude.toString() + ',' + currentLocation!!.longitude.toString()) // Followed by the latitude and longitude as a "point" option
                        }
                        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
                        startActivity(intent)
                    }
                    else {
                        location_error.visibility = View.VISIBLE
                    }
                }
            }
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
        }
        catch (e: SecurityException) {
            location_error.visibility = View.VISIBLE // Permission wasn't granted, shouldn't happen using QuickPermissions
        }
    }

    override fun onResume() {
        super.onResume()
        currentLocation = null
    }

    override fun onStop() {
        super.onStop()
        if (currentLocation != null) {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback) // Stop updates just in case they are still active
        }
    }

    override fun onPause() {
        super.onPause()
        if (currentLocation != null) {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback) // Stop updates just in case they are still active
        }
    }

}