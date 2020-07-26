package com.example.findmyrepresentatives

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView

/**
 * The main activity of the app.
 * This screen gives users a search box where they can serach for their representatives by postal code.
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun enterPostalCode(view: View) {
        val searchBox = findViewById<TextView>(R.id.searchBox)
        var postalCode = searchBox.text.toString().trim()
        val errorMessage = findViewById<TextView>(R.id.errorMessage)
        if (!Utils.isValidPostalCode(postalCode)) {
            errorMessage.text = getString(R.string.postal_code_error, postalCode)
            errorMessage.visibility = View.VISIBLE
        }
        else {
            errorMessage.visibility = View.INVISIBLE
            postalCode = postalCode.replace(" ", "")
            postalCode = postalCode.toUpperCase()
            val intent = Intent(this, ResultsActivity::class.java).apply {
                putExtra("postalCode", postalCode)
            }
            startActivity(intent)
        }
    }
}