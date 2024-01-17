package com.breens.youtubeclone.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.breens.youtubeclone.data.models.ChannelResponse
import com.breens.youtubeclone.repository.ChannelsRepository
import com.breens.youtubeclone.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class ChannelsViewModel @Inject constructor(
    private val repository: ChannelsRepository
): ViewModel() {

    private val _channelInfo: MutableLiveData<Resource<ChannelResponse>> = MutableLiveData()
    val channelInfo: LiveData<Resource<ChannelResponse>> = _channelInfo

    private val _channelThumbnailUrl: MutableLiveData<String> = MutableLiveData()
    val channelThumbnailUrl: LiveData<String> = _channelThumbnailUrl

    fun fetchChannelById(id: String) = viewModelScope.launch {
        _channelInfo.postValue(Resource.Loading())
        Log.d("VideosViewModel", "Fetching video by ID: $id")
        val response = repository.getChannelById(id)
        _channelInfo.postValue(handleChannelInfo(response))
    }

    fun getChannelThumbnailUrl(id: String, callback: (String?) -> Unit) {
        viewModelScope.launch {
            _channelInfo.postValue(Resource.Loading())
            val response = repository.getChannelById(id)
            _channelInfo.postValue(handleChannelInfo(response))

            if (response.isSuccessful) {
                response.body()?.let { channelResponse ->
                    val channelThumbnailUrl =
                        channelResponse.items.firstOrNull()?.snippet?.thumbnails?.high?.url
                    callback(channelThumbnailUrl)
                }
            }
        }
    }

    private fun handleChannelInfo(response: Response<ChannelResponse>): Resource<ChannelResponse> {
        if (response.isSuccessful) {
            response.body()?.let { channelResponse ->
                return Resource.Success(channelResponse)
            }
        }
        return Resource.Error(response.message())
    }
}