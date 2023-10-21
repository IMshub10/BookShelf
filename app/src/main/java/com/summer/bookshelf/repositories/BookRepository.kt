package com.summer.bookshelf.repositories

import com.summer.bookshelf.ui.models.BookModel
import kotlinx.coroutines.flow.Flow

interface BookRepository {
    fun fetchBooks(): Flow<List<BookModel>>
}