package com.example.findmyrepresentatives

import android.Manifest
import android.content.Intent
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

/**
 * The main activity of the app.
 * This screen gives users a search box where they can search for their representatives by postal code.
 */
class MainActivity : AppCompatActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        search_box.setOnEditorActionListener{ v, actionId, event ->
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
            location_error.visibility = View.INVISIBLE
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location == null) {
                        location_error.visibility = View.VISIBLE // If there's an issue with location (for example, it was never used before)
                    } else {
                        val intent = Intent(this, ResultsActivity::class.java).apply {
                            putExtra("query", "representatives/") // URL to use for the API call
                            putExtra("latlong",     location.latitude.toString() + ',' + location.longitude.toString()) // Followed by the latitude and longitude as a "point" option
                        }
                        startActivity(intent)
                    }
                }
        }
        catch (e: SecurityException) {
            location_error.visibility = View.VISIBLE // Permission wasn't granted
        }
    }
}