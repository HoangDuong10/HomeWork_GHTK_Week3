package com.example.profile.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val apiService : ApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://cache.giaohangtietkiem.vn/d/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}