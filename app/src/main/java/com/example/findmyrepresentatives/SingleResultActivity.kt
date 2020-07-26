package com.example.findmyrepresentatives

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.IllegalArgumentException


class SingleResultActivity (): AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_result)

        val postalCode = intent.getStringExtra("postalCode")
        val postalCodeConfirmation = findViewById<TextView>(R.id.postalCodeConfirmation).apply {
            text = postalCode
        }
        if (postalCode != null) {
            getRepresentative(postalCode)
        }
    }

    fun getRepresentative(postalCode: String) {
        var ans = "not yet"

        RepresentApi.retrofitService.getRepresentatives(postalCode).enqueue(
            object: Callback<RepresentDataSet> {
                override fun onFailure(call: Call<RepresentDataSet>, t: Throwable) {
                    Log.d("Error", t.toString())
                }

                override fun onResponse(call: Call<RepresentDataSet>, response: Response<RepresentDataSet>) {
                    if (response.code() == 404) {
                        Log.d("invalid", postalCode)
                        throw IllegalArgumentException("This is not a valid Canadian postal code.")
                    }
                    val success: RepresentDataSet? = response.body()
                    val rep: List<Representative> = success!!.representatives_centroid
                    val info = findViewById<TextView>(R.id.information).apply {
                        text = rep[0].first_name + rep[0].last_name
                    }
                }
            }
        )
        Log.d("ans", ans)
    }
}