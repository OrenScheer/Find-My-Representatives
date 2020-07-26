package com.example.findmyrepresentatives

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RepresentApiClient {
    private val representApi: RepresentApi

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://represent.opennorth.ca/postcodes/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        representApi = retrofit.create(RepresentApi::class.java)
    }

    fun getRepresentatives(): Call<List<Representative>> {
        return representApi.getRepresentatives()
    }


}