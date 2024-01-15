package com.breens.youtubeclone.data.models

data class YoutubeResponse(
    val items: List<YoutubeVideos>
)

data class YoutubeVideos (
    val id: String,
    val snippet: VideoDetails,
    val contentDetails: ContentDetails,
    val statistics: VideoStats
)

data class VideoDetails (
    val publishedAt: String,
    val channelId: String,
    val title: String,
    val description: String,
    val thumbnails: Thumbnails,
    val channelTitle: String,
    val tags: List<String>,
    val categoryId: Int,
    val liveBroadcastContent: String,
    val defaultLanguage: String,
    val localized: Localized,
    val defaultAudioLanguage: String
)
data class ContentDetails (
    val duration: String,
    val dimension: String,

)

data class VideoStats (
    val viewCount: String,
    val likeCount: String,
    val favoriteCount: String,
    val commentCount: String
)