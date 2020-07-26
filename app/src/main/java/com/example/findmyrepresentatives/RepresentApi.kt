package com.example.findmyrepresentatives

import retrofit2.Call
import retrofit2.http.GET

interface RepresentApi {
    @GET("K1T2J7/")
    fun getRepresentatives():
            Call<List<Representative>>
}