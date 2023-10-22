package com.summer.bookshelf.repositories

import com.summer.bookshelf.ui.models.BookModel
import kotlinx.coroutines.flow.Flow

interface BookRepository {
    fun fetchBooks(query: String): Flow<Map<Int, List<BookModel>>>
    suspend fun bookMark(bookModel: BookModel)
    suspend fun logout()
}