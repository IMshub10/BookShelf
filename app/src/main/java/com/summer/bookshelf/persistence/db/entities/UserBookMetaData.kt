package com.summer.bookshelf.persistence.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "user_books_meta_data", primaryKeys = ["user_id", "book_id"])
class UserBookMetaData(
    @ColumnInfo("user_id")
    val userId: Int,
    @ColumnInfo("book_id")
    val bookId: String,
    @ColumnInfo("is_bookmarked")
    val isBookmarked: Boolean,
    @ColumnInfo("tags")
    val tags: List<String>
)