package com.breens.youtubeclone.data.api

import com.breens.youtubeclone.data.models.CategoriesResponse
import com.breens.youtubeclone.data.models.ChannelResponse
import com.breens.youtubeclone.data.models.Channels
import com.breens.youtubeclone.data.models.CommentResponse
import com.breens.youtubeclone.data.models.SearchResponse
import com.breens.youtubeclone.data.models.YoutubeResponse
import com.breens.youtubeclone.utils.Constants.Companion.CHANNEL_INFO
import com.breens.youtubeclone.utils.Constants.Companion.COMMENT_THREADS
import com.breens.youtubeclone.utils.Constants.Companion.DETAILS
import com.breens.youtubeclone.utils.Constants.Companion.KEY
import com.breens.youtubeclone.utils.Constants.Companion.LIST_CATEGORIES
import com.breens.youtubeclone.utils.Constants.Companion.LIST_OF_VIDEOS
import com.breens.youtubeclone.utils.Constants.Companion.MAX_RESULTS
import com.breens.youtubeclone.utils.Constants.Companion.MOST_POPULAR
import com.breens.youtubeclone.utils.Constants.Companion.REGION_CODE
import com.breens.youtubeclone.utils.Constants.Companion.REPLIES
import com.breens.youtubeclone.utils.Constants.Companion.SEARCH
import com.breens.youtubeclone.utils.Constants.Companion.SNIPPET
import com.breens.youtubeclone.utils.Constants.Companion.STATISTICS
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Part
import retrofit2.http.Query


interface YoutubeApi {

    @GET(LIST_OF_VIDEOS)
    suspend fun fetchVideos(
        @Query("part") part: String = "$SNIPPET,$DETAILS,$STATISTICS",
        @Query("chart") chart: String = MOST_POPULAR,
        @Query("regionCode") regionCode: String = REGION_CODE,
        @Query("key") key: String = KEY
    ): Response<YoutubeResponse>

    @GET(LIST_OF_VIDEOS)
    suspend fun fetchVideosByCategory(
        @Query("part") part: String = "$SNIPPET,$DETAILS,$STATISTICS",
        @Query("chart") chart: String = MOST_POPULAR,
        @Query("regionCode") regionCode: String = REGION_CODE,
        @Query("videoCategoryId") videoCategoryId: String,
        @Query("key") key: String = KEY
    ): Response<YoutubeResponse>

    @GET(LIST_OF_VIDEOS)
    suspend fun fetchVideoById(
        @Query("part") part: String = "$SNIPPET, $DETAILS, $STATISTICS",
        @Query("id") id: String,
        @Query("key") key: String = KEY
    ): Response<YoutubeResponse>

    @GET(LIST_CATEGORIES)
    suspend fun fetchCategories(
        @Query("part") part: String = SNIPPET,
        @Query("regionCode") regionCode: String = REGION_CODE,
        @Query("key") key: String = KEY
    ): Response<CategoriesResponse>

    @GET(SEARCH)
    suspend fun fetchVideosSearchByKeyword(
        @Query("part") part: String = SNIPPET,
        @Query("maxResults") maxResults: Int = 25,
        @Query("q") query: String,
        @Query("key") key: String = KEY
    ): Response<SearchResponse>

    @GET(CHANNEL_INFO)
    suspend fun fetchChannelById(
        @Query("part") part: String = "$SNIPPET, $DETAILS, $STATISTICS",
        @Query("id") id: String,
        @Query("key") key: String = KEY
    ): Response<ChannelResponse>

    @GET(COMMENT_THREADS)
    suspend fun fetchCommentsByVideoId(
        @Query("part") part: String = "$SNIPPET, $REPLIES",
        @Query("videoId") videoId: String,
        @Query("key") key: String = KEY
    ): Response<CommentResponse>
}