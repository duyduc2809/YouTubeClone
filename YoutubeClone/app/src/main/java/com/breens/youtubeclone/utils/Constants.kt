package com.breens.youtubeclone.utils

class Constants {
    companion object {
        const val BASE_URL = "https://www.googleapis.com/youtube/v3/"

        //Endpoints
        const val LIST_OF_VIDEOS = "videos"
        const val LIST_CATEGORIES = "videoCategories"
        const val SEARCH = "search"
        const val CHANNEL_INFO = "channels"

        //PART PROPERTIES
        const val SNIPPET = "snippet"
        const val DETAILS = "contentDetails"
        const val STATISTICS = "statistics"

        //CHART PROPERTIES
        const val MOST_POPULAR = "mostPopular"

        //REGION_CODE
        const val REGION_CODE = "VN"

        //KEY
        const val KEY = "AIzaSyBe9fcvaYIG5OZRScxr-K7VpSqQkQxtK5A"

        const val MAX_RESULTS = "25"
    }
}