package com.breens.youtubeclone.data.models

data class Thumbnails(
    val high: ThumbnailsAttributes
)
data class ThumbnailsAttributes (
    val url: String
)