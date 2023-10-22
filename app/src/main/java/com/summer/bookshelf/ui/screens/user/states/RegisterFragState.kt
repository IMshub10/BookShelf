package com.summer.bookshelf.ui.screens.user.states

sealed class RegisterFragState {

    data class Loading(var message: String) : RegisterFragState()

    data object Idle : RegisterFragState()

    data class Error(var message: String, var errorCode: Int) : RegisterFragState()

    data class SaveComplete(var message: String) : RegisterFragState()
}