package com.summer.bookshelf.ui.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.summer.bookshelf.persistence.db.entities.BookEntity
import com.summer.bookshelf.repositories.BookRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map

class BookListViewModel(
    private val bookRepository: BookRepository
) : ViewModel() {

    companion object {
        const val TAG = "MainViewModel"
    }

    private val _books = MutableStateFlow<List<BookEntity>?>(null)
    val books = _books.asStateFlow()

    init {
        bookRepository.fetchBooks().map {
            _books.value = it
        }.launchIn(viewModelScope)
    }
}