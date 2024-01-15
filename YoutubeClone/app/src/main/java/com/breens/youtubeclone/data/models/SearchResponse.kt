package com.breens.youtubeclone.data.models

data class SearchResponse(
    val items: List<VideosSearch>
)
data class VideosSearch (
    val id: SearchID,
    val snippet: VideoDetails,
    )
data class SearchID (
    val kind: String,
    val videoID: String,
)
