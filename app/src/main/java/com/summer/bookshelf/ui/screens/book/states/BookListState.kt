package com.summer.bookshelf.ui.screens.book.states

sealed class BookListState {

    data object Idle : BookListState()

    data class Loading(var message: String) : BookListState()

    data class Error(var message: String) : BookListState()
}
