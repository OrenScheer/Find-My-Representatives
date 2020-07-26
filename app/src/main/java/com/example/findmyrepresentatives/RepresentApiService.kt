package com.example.findmyrepresentatives

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

private const val BASE_URL = "https://represent.opennorth.ca/postcodes/"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface RepresentApiService {
    @GET("/postcodes/{code}")
    fun getRepresentatives(@Path("code") postalCode: String):
            Call<String>
}

object RepresentApi {
    val retrofitService: RepresentApiService by lazy {
        retrofit.create(RepresentApiService::class.java)
    }
}