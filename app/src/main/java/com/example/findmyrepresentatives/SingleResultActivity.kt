package com.example.findmyrepresentatives

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SingleResultActivity (): AppCompatActivity() {

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
        RepresentApi.retrofitService.getRepresentatives().enqueue(
            object: Callback<String> {
                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.d("Error", t.toString())
                }

                override fun onResponse(call: Call<String>, response: Response<String>) {
                    ans = response.body().toString()
                    Log.d("success", ans)
                }

            }
        )
        Log.d("ans", ans)
        val info = findViewById<TextView>(R.id.information).apply {
            text = ans
        }
    }
}