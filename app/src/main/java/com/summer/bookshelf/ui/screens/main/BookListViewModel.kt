package com.summer.bookshelf.ui.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.summer.bookshelf.persistence.db.entities.BookEntity
import com.summer.bookshelf.repositories.BookRepository
import com.summer.bookshelf.ui.models.BookModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map

class BookListViewModel(
    private val bookRepository: BookRepository
) : ViewModel() {

    companion object {
        const val TAG = "BookListViewModel"
    }

    private val _booksYears = MutableStateFlow<Set<Int>?>(null)
    val bookYears = _booksYears.asStateFlow()

    private val _books = MutableStateFlow<List<BookModel>?>(null)
    val books = _books.asStateFlow()

    init {
        bookRepository.fetchBooks().map {
            _booksYears.value = it.keys
            _books.value = it.flatMap { flatMap ->
                flatMap.value
            }
        }.launchIn(viewModelScope)
    }
}