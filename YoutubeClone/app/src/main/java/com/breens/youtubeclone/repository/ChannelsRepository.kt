package com.breens.youtubeclone.repository

import com.breens.youtubeclone.data.api.YoutubeApi
import com.breens.youtubeclone.data.models.ChannelResponse
import retrofit2.Response
import javax.inject.Inject

class ChannelsRepository @Inject constructor(
    private val api: YoutubeApi
) {
    suspend fun getChannelById(id: String): Response<ChannelResponse> {
        return api.fetchChannelById(id = id)
    }
}