package com.summer.bookshelf.ui.screens.book

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.summer.bookshelf.repositories.BookRepository
import com.summer.bookshelf.ui.models.BookModel
import com.summer.bookshelf.ui.screens.book.states.BookListState
import com.summer.bookshelf.ui.screens.user.states.LoginState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class BookListViewModel(
    private val bookRepository: BookRepository
) : ViewModel() {

    private val _state = MutableStateFlow<BookListState>(BookListState.Idle)
    val state: StateFlow<BookListState> = _state.asStateFlow()

    companion object {
        const val TAG = "BookListViewModel"
    }

    private val _booksYears = MutableStateFlow<Set<Int>?>(null)
    val bookYears = _booksYears.asStateFlow()

    private val _books = MutableStateFlow<List<BookModel>?>(null)
    val books = _books.asStateFlow()

    init {
        _state.value = BookListState.Loading("Loading Books")
        bookRepository.fetchBooks().map {
            _booksYears.value = it.keys
            _books.value = it.flatMap { flatMap ->
                flatMap.value
            }
            _state.value = BookListState.Idle
        }.launchIn(viewModelScope)
    }

    fun bookMark(bookModel: BookModel) {
        _state.value = BookListState.Loading("Book Marking ${bookModel.title}")
        viewModelScope.launch(Dispatchers.IO) {
            bookRepository.bookMark(bookModel)
            _state.value = BookListState.Idle
        }
    }

    fun logout() {
        _state.value = BookListState.Loading("Logging out...")
        viewModelScope.launch(Dispatchers.IO) {
            bookRepository.logout()
            _state.value = BookListState.Logout
        }
    }
}