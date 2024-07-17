package com.example.profile.repository

import com.example.profile.model.DataResponse
import com.example.profile.retrofit.RetrofitInstance
import retrofit2.Response

class ProfileRepository {
     suspend fun getProfile() : Response<DataResponse>{
        return RetrofitInstance.apiService.getData()
    }
}