package com.breens.youtubeclone.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor()
    : ViewModel() {

    init {
        Log.d("SharedViewModel", "ViewModel initialized")
    }
    private val _selectedVideoId = MutableLiveData<String>()

    val selectedVideoId: LiveData<String> get() = _selectedVideoId

    fun setVideoId(videoId: String) {
        _selectedVideoId.value = videoId
        Log.d("SharedViewModel", "Video ID set: ${selectedVideoId.value}")
    }
}