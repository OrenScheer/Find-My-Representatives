package com.example.findmyrepresentatives

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_results.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.IllegalArgumentException

/**
 * The results activity.
 * This screen shows a list of representatives.
 */
class ResultsActivity (): AppCompatActivity() {
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: RecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager

        val placeholder = Representative("https://media1.giphy.com/media/3o7bu3XilJ5BOiSGic/giphy.gif?cid=ecf05e47odo19iqs9f8ly2jdje5he3do7mhbc8qjstf2snn1&rid=giphy.gif",
        "LOADING", "LOADING", "LOADING",
            "LOADING", "LOADING")

        val data = listOf(placeholder)

        adapter = RecyclerAdapter(data)
        recyclerView.adapter = adapter

        val postalCode = intent.getStringExtra("postalCode")
        if (postalCode != null) {
            getRepresentative(postalCode)
        }
    }

    fun getRepresentative(postalCode: String) {

        RepresentApi.retrofitService.getRepresentatives(postalCode).enqueue(
            object: Callback<RepresentDataSet> {
                override fun onFailure(call: Call<RepresentDataSet>, t: Throwable) {
                    Log.d("Error", t.toString())
                }

                override fun onResponse(call: Call<RepresentDataSet>, response: Response<RepresentDataSet>) {
                    if (response.code() == 404) {
                        Log.d("invalid", postalCode)
                        throw IllegalArgumentException("This is not a valid Canadian postal code.") // Crashes - fix, should return to home screen
                    }
                    else {
                        adapter.setRepresentatives(response.body()!!.representatives_centroid)
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        )
    }
}