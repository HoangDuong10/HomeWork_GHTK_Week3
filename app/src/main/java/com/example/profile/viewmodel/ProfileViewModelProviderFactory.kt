package com.example.profile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.profile.repository.ProfileRepository

@Suppress("UNCHECKED_CAST")
class ProfileViewModelProviderFactory(private val profileRepository : ProfileRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProfileViewModel(profileRepository) as T
    }
}