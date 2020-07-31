package com.example.findmyrepresentatives

import android.Manifest
import android.content.Context
import android.content.Intent
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import kotlinx.android.synthetic.main.activity_main.*
import java.security.Security
import java.util.*

/**
 * The main activity of the app.
 * This screen gives users a search box where they can serach for their representatives by postal code.
 */
class MainActivity : AppCompatActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        searchBox.setOnEditorActionListener{v, actionId, event ->
            when(actionId) {
                EditorInfo.IME_ACTION_SEND -> {
                    button.performClick()
                    true
                }
                else -> false
            }
        }
    }

    fun enterPostalCode(view: View) {
        val searchBox = findViewById<EditText>(R.id.searchBox)
        var postalCode = searchBox.text.toString().trim()
        val errorMessage = findViewById<TextView>(R.id.errorMessage)
        if (!Utils.isValidPostalCode(postalCode)) {
            errorMessage.text = getString(R.string.postal_code_error, postalCode)
            errorMessage.visibility = View.VISIBLE
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

    fun useLocation(view: View) = runWithPermissions(Manifest.permission.ACCESS_FINE_LOCATION) {
        try {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location == null) {
                        Log.d("location null", "hmm")
                        throw SecurityException()
                    } else {
                        var query = "representatives/?point="
                        query += location.latitude
                        query += ','
                        query += location.longitude
                        val intent = Intent(this, ResultsActivity::class.java).apply {
                            putExtra("query", "representatives/")
                            putExtra("latlong", location.latitude.toString() + ',' + location.longitude.toString())
                        }
                        startActivity(intent)
                    }
                }
        }
        catch (e: SecurityException) {
            Log.d("no permission given", "not cool")
        }
    }
}