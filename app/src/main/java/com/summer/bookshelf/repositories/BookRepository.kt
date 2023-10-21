package com.summer.bookshelf.repositories

import com.summer.bookshelf.persistence.db.entities.BookEntity
import kotlinx.coroutines.flow.Flow

interface BookRepository {
    fun fetchBooks ():Flow<List<BookEntity>>
}