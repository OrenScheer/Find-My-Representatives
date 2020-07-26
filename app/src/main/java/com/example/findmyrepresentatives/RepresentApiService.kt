package com.example.findmyrepresentatives

import com.squareup.moshi.Moshi
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.http.GET
import retrofit2.http.Path

private const val BASE_URL = "https://represent.opennorth.ca/postcodes/"

// Moshi is used to deserialize the JSON returned from the API to objects.
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

// Retrofit is used for asynchronous API calls.
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

/**
 * A Retrofit interface that defines the API.
 */
interface RepresentApiService {
    @GET("/postcodes/{code}/representatives/") // The {code} part is passed as an argument to getRepresentatives()
    fun getRepresentatives(@Path("code") postalCode: String):
            Call<RepresentDataSet>
}

/**
 * A singleton used any time there is an API call.
 */
object RepresentApi {
    val retrofitService: RepresentApiService by lazy {
        retrofit.create(RepresentApiService::class.java) // Lazily creates a Retrofit instance from the interface
    }
}