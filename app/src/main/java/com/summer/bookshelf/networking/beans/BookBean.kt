package com.summer.bookshelf.networking.beans


import com.google.gson.annotations.SerializedName

data class BookBean(
    @SerializedName("id")
    val id: String?,
    @SerializedName("image")
    val image: String?,
    @SerializedName("publishedChapterDate")
    val publishedChapterDate: Long?,
    @SerializedName("score")
    val score: Double?,
    @SerializedName("title")
    val title: String?
)