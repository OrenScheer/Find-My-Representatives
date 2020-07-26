package com.example.findmyrepresentatives

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView

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
            // Somehow create an error message (view?)
            errorMessage.text = getString(R.string.postal_code_error, postalCode)
            errorMessage.visibility = View.VISIBLE
        }
        else {
            errorMessage.visibility = View.INVISIBLE
            postalCode = postalCode.replace(" ", "")
            postalCode = postalCode.toUpperCase()
            val intent = Intent(this, SingleResultActivity::class.java).apply {
                putExtra("postalCode", postalCode)
            }
            startActivity(intent)
        }
    }
}