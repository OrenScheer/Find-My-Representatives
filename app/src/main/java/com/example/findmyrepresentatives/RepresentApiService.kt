package com.example.findmyrepresentatives

import com.squareup.moshi.Moshi
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap
import java.util.concurrent.TimeUnit

private const val BASE_URL = "https://represent.opennorth.ca/"

// Moshi is used to deserialize the JSON returned from the API to objects.
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

// OkHttp is the client for Retrofit, which specifies a timeout length
private val client: OkHttpClient = OkHttpClient().apply {
    newBuilder().connectTimeout(5, TimeUnit.SECONDS).callTimeout(5, TimeUnit.SECONDS)
}

// Retrofit is used for asynchronous API calls.
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .client(client)
    .build()

/**
 * A Retrofit interface that defines the API.
 */
interface RepresentApiService {
    @GET("{query}") // The {code} part is passed as an argument to getRepresentatives()
    fun getRepresentatives(
        @Path("query") query: String,
        @QueryMap options: Map<String, String>
    ):
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