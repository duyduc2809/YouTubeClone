package com.breens.youtubeclone.data.models

data class CommentResponse(
    val nextPageToken: String,
    val items: List<Item>
)

data class  Item(
    val id: String,
    val snippet: ItemSnippet,
)

data class ItemSnippet(
    val channelId: String,
    val videoId: String,
    val topLevelComment: TopComment,
    val canReply: Boolean,
    val totalReplyCount: Int,
    val isPublic: Boolean
)

data class TopComment (
    val id: String,
    val snippet: CommentDetails
)

data class CommentDetails (
    val channelId: String,
    val videoId: String,
    val textDisplay: String,
    val textOriginal: String,
    val authorDisplayName: String,
    val authorProfileImageUrl: String,
    val authorChannelUrl: String,
    val authorChannelId: AuthorChannelId,
    val canRate: Boolean,
    val viewerRating: String,
    val likeCount: Int,
    val publishedAt: String,
    val updatedAt: String

)

data class AuthorChannelId (
    val value: String
)
