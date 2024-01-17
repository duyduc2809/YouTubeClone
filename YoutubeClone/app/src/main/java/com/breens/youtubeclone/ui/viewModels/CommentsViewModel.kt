package com.breens.youtubeclone.ui.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.breens.youtubeclone.data.models.ChannelResponse
import com.breens.youtubeclone.data.models.CommentResponse
import com.breens.youtubeclone.repository.CommentsRepository
import com.breens.youtubeclone.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class CommentsViewModel @Inject constructor(
    private val repository: CommentsRepository
): ViewModel() {

    private val _commentInfo: MutableLiveData<Resource<CommentResponse>> = MutableLiveData()
    val commentInfo: LiveData<Resource<CommentResponse>> = _commentInfo

    fun fetchCommentByVideoId(videoId: String) = viewModelScope.launch {
        _commentInfo.postValue(Resource.Loading())
        Log.d("CommentsViewModel", "Fetching comment by videoId: $videoId")
        val response = repository.getCommentByVideoId(videoId)
        _commentInfo.postValue(handleCommentInfo(response))
    }

    private fun handleCommentInfo(response: Response<CommentResponse>): Resource<CommentResponse> {
        if (response.isSuccessful) {
            response.body()?.let { commentResponse ->
                return Resource.Success(commentResponse)
            }
        }
        return Resource.Error(response.message())
    }
}