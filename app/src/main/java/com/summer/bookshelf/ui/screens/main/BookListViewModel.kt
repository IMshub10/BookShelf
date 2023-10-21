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

    private val selectedYear = MutableStateFlow<Int?>(null)

    private val _books = MutableStateFlow<Map<Int, List<BookModel>>?>(null)
    val books = _books.asStateFlow()

    fun getBooksByYear(year: Int) = _books.value?.let {
        it[year]
    } ?: listOf()

    init {
        bookRepository.fetchBooks().map {
            _books.value = it
        }.launchIn(viewModelScope)
    }
}