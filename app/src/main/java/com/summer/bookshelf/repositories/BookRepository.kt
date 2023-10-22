package com.summer.bookshelf.repositories

import com.summer.bookshelf.persistence.db.entities.UserBookMetaData
import com.summer.bookshelf.ui.models.BookModel
import kotlinx.coroutines.flow.Flow

interface BookRepository {
    fun fetchBooks(query: String): Flow<Map<Int, List<BookModel>>>
    suspend fun bookMark(bookModel: BookModel)
    suspend fun logout()
    suspend fun addTag(bookId: String, tagName: String)
    suspend fun fetchUserMetaData(bookId: String): Flow<UserBookMetaData?>
}