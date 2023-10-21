package com.summer.bookshelf.ui.screens.user.frags

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.summer.bookshelf.R
import com.summer.bookshelf.repositories.LoginRepository
import com.summer.bookshelf.ui.inputs.TextInputModel
import com.summer.bookshelf.ui.screens.user.states.LoginState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val _state = MutableStateFlow<LoginState>(LoginState.Idle)
    val state: StateFlow<LoginState> = _state.asStateFlow()

    val emailInputModel = TextInputModel(
        hint = R.string.email,
        isRequired = true,
        validator = {
            it.isNotBlank()
        },
    )

    val passwordInputModel = TextInputModel(
        hint = R.string.password,
        isRequired = true,
        validator = {
            it.matches("^(?=.*[0-9])(?=.*[!@#\$%&()])(?=.*[a-z])(?=.*[A-Z]).{8,}$".toRegex())
        },
    )

    fun validateNLogin() {
        _state.value = LoginState.Loading("Logging In...")

        var isValid = true

        if (!emailInputModel.isValid()) {
            emailInputModel.setError(R.string.invalid_input)
            isValid = false
        }

        if (!passwordInputModel.isValid()) {
            passwordInputModel.setError(R.string.invalid_input)
            isValid = false
        }
        if (isValid) {
            viewModelScope.launch(Dispatchers.IO) {
                val isUserNamePassCorrect = loginRepository.isUserNamePassCorrect(
                    emailInputModel.editTextContent!!,
                    passwordInputModel.editTextContent!!
                )
                loginRepository.setUserLoginStatus(true)

                if (isUserNamePassCorrect) {
                    _state.value = LoginState.UserLoggedIn
                } else {
                    _state.value = LoginState.Error("Username password does not match")
                }
            }
        } else {
            _state.value = LoginState.Idle
        }
    }

    fun resetStates() {
        emailInputModel.setModel("")
        passwordInputModel.setModel("")
        _state.value = LoginState.Idle
    }
}