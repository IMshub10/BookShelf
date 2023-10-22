package com.summer.bookshelf.ui.screens.book.states

sealed class CreateTagState {
    data object Idle : CreateTagState()

    data class Error(var message: String) : CreateTagState()

    data object SaveComplete : CreateTagState()
}
