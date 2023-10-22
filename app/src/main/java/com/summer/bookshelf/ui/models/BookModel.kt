package com.summer.bookshelf.ui.models

import androidx.databinding.BaseObservable
import androidx.room.ColumnInfo

data class BookModel(
    @ColumnInfo("id")
    val id: String,
    @ColumnInfo("title")
    val title: String,
    @ColumnInfo("image")
    val image: String?,
    @ColumnInfo("score")
    val score: String,
    @ColumnInfo("year")
    val year: Int,
    @ColumnInfo("is_bookmarked")
    var isBookmarked: Boolean,
) : BaseObservable()