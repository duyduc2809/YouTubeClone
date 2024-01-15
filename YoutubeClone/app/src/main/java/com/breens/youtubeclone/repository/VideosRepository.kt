package com.breens.youtubeclone.repository

import com.breens.youtubeclone.data.api.YoutubeApi
import com.breens.youtubeclone.data.models.CategoriesResponse
import com.breens.youtubeclone.data.models.SearchResponse
import com.breens.youtubeclone.data.models.YoutubeResponse
import retrofit2.Response
import javax.inject.Inject

class VideosRepository @Inject constructor(
    private val api: YoutubeApi
) {

    suspend fun getPopularVideos(): Response<YoutubeResponse> {
        return api.fetchVideos()
    }

    suspend fun getVideosByCategory(categoryId: String): Response<YoutubeResponse> {
        return api.fetchVideosByCategory(videoCategoryId = categoryId)
    }

    suspend fun  getVideoById(id: String): Response<YoutubeResponse> {
        return api.fetchVideoById(id = id)
    }
    suspend fun getCategories(): Response<CategoriesResponse> {
        return api.fetchCategories()
    }

    suspend fun getVideosSearchByKeyword(query: String): Response<SearchResponse> {
        return api.fetchVideosSearchByKeyword(query = query)
    }

}