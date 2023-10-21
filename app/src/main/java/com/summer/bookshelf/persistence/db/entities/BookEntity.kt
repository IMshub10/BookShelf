package com.summer.bookshelf.persistence.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class BookEntity(
    @PrimaryKey(false)
    @ColumnInfo("id")
    var id: String,
    @ColumnInfo("image")
    val image: String?,
    @ColumnInfo("published_chapter_date")
    val publishedChapterDate: Long,
    @ColumnInfo("published_chapter_year")
    val publishedChapterYear: Int,
    @ColumnInfo("score")
    val score: Double,
    @ColumnInfo("title")
    val title: String
)