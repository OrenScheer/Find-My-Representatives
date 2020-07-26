package com.example.findmyrepresentatives

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import okhttp3.*
import java.io.IOException


class SingleResultActivity : AppCompatActivity() {
    private val client = OkHttpClient()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_result)

        val postalCode = intent.getStringExtra("postalCode")
        val postalCodeConfirmation = findViewById<TextView>(R.id.postalCodeConfirmation).apply {
            text = postalCode
        }
        getRepresentative()
    }

    fun getRepresentative() {
        var ans = "not yet"
        val request = Request.Builder()
            .url("https://represent.opennorth.ca/postcodes/K1T2J7/")
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("Fail", e.message.toString())
            }
            override fun onResponse(call: Call, response: Response) {
                ans = response.body()!!.string()
                Log.d("Success", ans)
            }
        })
        val info = findViewById<TextView>(R.id.information).apply {
            text = ans
        }
    }
}