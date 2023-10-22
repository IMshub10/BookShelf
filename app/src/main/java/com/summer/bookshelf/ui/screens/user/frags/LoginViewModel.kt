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
            it.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}\$".toRegex())
        },
    )

    val passwordInputModel = TextInputModel(
        hint = R.string.password,
        isRequired = true,
        validator = {
            it.matches("^(?=.*[0-9])(?=.*[!@#\$%&()])(?=.*[a-z])(?=.*[A-Z]).{8,}$".toRegex())
        },
    )

    fun validateNLogin(withFingerPrint: Boolean) {
        _state.value = LoginState.Loading("Logging In...")

        var isValid = true

        if (!emailInputModel.isValid()) {
            emailInputModel.setError(R.string.invalid_input)
            isValid = false
        }

        if (!passwordInputModel.isValid() && !withFingerPrint) { //don't validate password with fingerprint
            passwordInputModel.setError(R.string.invalid_input)
            isValid = false
        }
        if (isValid) {
            viewModelScope.launch(Dispatchers.IO) {
                val userId = loginRepository.getUserByCredentials(
                    emailInputModel.editTextContent!!,
                    if (withFingerPrint) null else passwordInputModel.editTextContent!!
                )
                if (userId != 0) {
                    loginRepository.setLoggedInUserId(userId)
                    _state.value = LoginState.UserLoggedIn
                } else {
                    _state.value = LoginState.Error("User not found!")
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