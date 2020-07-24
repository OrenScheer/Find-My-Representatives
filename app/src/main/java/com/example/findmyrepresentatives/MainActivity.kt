package com.example.findmyrepresentatives

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun enterPostalCode(view: View) {
        val searchBox = findViewById<TextView>(R.id.searchBox)
        val postalCode = searchBox.text.toString().trim()
        if (!Utils.isValidPostalCode(postalCode)) {
            // Somehow create an error message (view?)
            val errorMessage = findViewById<TextView>(R.id.errorMessage).apply {
                text = postalCode + getString(R.string.postal_code_error)
            }
            errorMessage.visibility = View.VISIBLE
        }
        else {
            val errorMessage = findViewById<TextView>(R.id.errorMessage)
            errorMessage.visibility = View.INVISIBLE
        }
    }
}