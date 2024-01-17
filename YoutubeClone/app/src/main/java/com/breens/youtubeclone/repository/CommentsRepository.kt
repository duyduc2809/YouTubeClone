package com.breens.youtubeclone.repository

import com.breens.youtubeclone.data.api.YoutubeApi
import com.breens.youtubeclone.data.models.CommentResponse
import retrofit2.Response
import javax.inject.Inject

class CommentsRepository @Inject constructor(
    private val api: YoutubeApi
){
    suspend fun getCommentByVideoId(videoId: String): Response<CommentResponse> {
        return api.fetchCommentsByVideoId(videoId = videoId)
    }
}