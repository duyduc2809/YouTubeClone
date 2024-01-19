package com.breens.youtubeclone.data.models

data class ChannelResponse (
    val items: List<Channels>
)

data class Channels (
    val id: String,
    val snippet: ChannelDetails,
    val contentDetails: ChannelContentDetails,
    val brandingSettings: Brands,
    val statistics: ChannelStats
)

data class ChannelDetails (
    val title: String,
    val description: String,
    val customUrl: String,
    val publishedAt: String,
    val thumbnails: ChannelThumbnails,
    val localized: ChannelLocalized,
    val country: String

)

data class ChannelContentDetails (
    val relatedPlaylist: RelatedPlaylist
)

data class RelatedPlaylist (
    val likes: String,
    val uploads: String
)

data class Brands (
    val channel: BrandDetails,
    val image: Banner
)

data class BrandDetails (
    val title: String,
    val description: String,
    val keywords: String,
    val trackingAnalyticsAccountId: String,
    val unsubscribedTrailer: String,
    val country: String
)

data class Banner (
    val bannerExternalUrl: String
)
data class ChannelStats (
    val viewCount: String,
    val subscriberCount: String,
    val hiddenSubscriberCount: Boolean,
    val videoCount: String
)

data class ChannelLocalized (
    val title: String,
    val description: String,
)

data class ChannelThumbnails (
    val default: ChannelThumbAttr,
    val medium: ChannelThumbAttr,
    val high: ChannelThumbAttr
)

data class ChannelThumbAttr (
    val url: String,
    val width: Int,
    val height: Int
)