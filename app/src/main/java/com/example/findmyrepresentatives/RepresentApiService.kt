package com.example.findmyrepresentatives

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://represent.opennorth.ca/postcodes/"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface RepresentApiService {
    @GET("K1T2J7/")
    fun getRepresentatives():
            Call<String>
}

object RepresentApi {
    val retrofitService: RepresentApiService by lazy {
        retrofit.create(RepresentApiService::class.java)
    }
}