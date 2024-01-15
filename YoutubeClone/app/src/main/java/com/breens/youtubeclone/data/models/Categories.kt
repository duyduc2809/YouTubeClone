package com.breens.youtubeclone.data.models

data class CategoriesResponse(
    val items: List<Categories>
)
data class Categories(
    val id: String,
    val snippet: CategorySnippet
)

data class CategorySnippet(
    val title: String,
    val assignable: Boolean,
    val channelId: String
)
