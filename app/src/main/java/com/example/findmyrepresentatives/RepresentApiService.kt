package com.example.findmyrepresentatives

import com.squareup.moshi.Moshi
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap
import java.util.concurrent.TimeUnit

private const val BASE_URL = "https://represent.opennorth.ca/" // Base URL for the API calls

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
 * @author Oren Scheer
 */
interface RepresentApiService {
    @GET("{query}") // The {query} part is passed as an argument to getRepresentatives()
    fun getRepresentatives(
        @Path("query") query: String, // In the case of a postal code, query will be of the form "postcodes/A0A0A0"
        @QueryMap options: Map<String, String> // In the case of a device location, query will be of the form "representatives/", and options will add the lat and long as an option in the URL
    ):
            Call<RepresentDataSet>
}

/**
 * A singleton used any time there is an API call.
 * @author Oren Scheer
 */
object RepresentApi {
    val retrofitService: RepresentApiService by lazy {
        retrofit.create(RepresentApiService::class.java) // Lazily creates a Retrofit instance from the interface
    }
}