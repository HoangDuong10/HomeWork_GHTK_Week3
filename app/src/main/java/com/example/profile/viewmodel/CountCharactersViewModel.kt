package com.example.profile.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CountCharactersViewModel : ViewModel() {
    val characterCount = mutableStateOf<Map<Char, Int>>(emptyMap())
    fun countCharacters(input: String) {
        val countMap = input.groupingBy { it }.eachCount()
        characterCount.value = countMap
    }
}