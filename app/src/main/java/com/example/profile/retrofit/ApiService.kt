package com.example.profile.retrofit

import com.example.profile.model.DataResponse
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("af3cc8b64fa487da8be34c1cc1c5d2d5.json")
    suspend fun getData() : Response<DataResponse>
}