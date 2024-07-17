package com.example.profile.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.profile.model.DataResponse
import com.example.profile.repository.ProfileRepository
import com.example.profile.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response


class ProfileViewModel(private val profileRepository : ProfileRepository) : ViewModel(){
    val profile : MutableLiveData<Resource<DataResponse>> = MutableLiveData()
    fun getProfile() = viewModelScope.launch {
        profile.postValue(Resource.Loading())
        //delay(2000)
        val response = profileRepository.getProfile()
        profile.postValue(handleProfileResponse(response))
    }

    private fun handleProfileResponse(response: Response<DataResponse>) : Resource<DataResponse>{
        if(response.isSuccessful){
            response.body()?.let{ resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

}