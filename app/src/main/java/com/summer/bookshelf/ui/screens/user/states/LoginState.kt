package com.summer.bookshelf.ui.screens.user.states

sealed class LoginState {
    data class Loading(var message: String) : LoginState()

    data object Idle : LoginState()
    
    data class Error(var message: String) : LoginState()

    data object UserLoggedIn : LoginState()
}
