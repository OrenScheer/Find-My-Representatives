package com.example.findmyrepresentatives

import com.squareup.moshi.Moshi
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.http.GET
import retrofit2.http.Path

private const val BASE_URL = "https://represent.opennorth.ca/postcodes/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface RepresentApiService {
    @GET("/postcodes/{code}/representatives/")
    fun getRepresentatives(@Path("code") postalCode: String):
            Call<RepresentDataSet>
}

object RepresentApi {
    val retrofitService: RepresentApiService by lazy {
        retrofit.create(RepresentApiService::class.java)
    }
}