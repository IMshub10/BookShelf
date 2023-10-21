package com.summer.bookshelf.ui.screens.user.frags

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.summer.bookshelf.R
import com.summer.bookshelf.persistence.db.entities.UserEntity
import com.summer.bookshelf.repositories.LoginRepository
import com.summer.bookshelf.ui.inputs.DropdownInputModel
import com.summer.bookshelf.ui.inputs.TextInputModel
import com.summer.bookshelf.ui.screens.user.states.RegisterFragState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val _state = MutableStateFlow<RegisterFragState>(RegisterFragState.Idle)
    val state: StateFlow<RegisterFragState> = _state.asStateFlow()

    val countryInputModel = DropdownInputModel(
        hint = R.string.country,
        isRequired = true,
        dropdownList = mutableListOf(),
    )

    val nameInputModel = TextInputModel(
        hint = R.string.name,
        isRequired = true,
        validator = {
            it.isNotBlank()
        },
    )

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

    init {
        _state.value = RegisterFragState.Loading("Loading Countries")

        loginRepository.fetchCountryList().map { list ->

            countryInputModel.dropdownList = list
            countryInputModel.notifyChange()

            if (list.isNotEmpty()) {
                loginRepository.fetchDefaultCountry().map {

                    _state.value = RegisterFragState.Idle

                    it?.let {
                        if (countryInputModel.selectedDropDownModel.value == null) {
                            countryInputModel.setSelectedDropDownModel(it)
                        }
                    }

                }.launchIn(viewModelScope)
            } else {
                _state.value = RegisterFragState.Error("Unable to load countries")
            }
        }.launchIn(viewModelScope)
    }

    fun validateNSave() {
        _state.value = RegisterFragState.Loading("Saving")

        var isValid = true

        if (!countryInputModel.isValid()) {
            countryInputModel.setError(R.string.invalid_input)
            isValid = false
        }

        if (!nameInputModel.isValid()) {
            nameInputModel.setError(R.string.invalid_input)
            isValid = false
        }

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
                loginRepository.insertUser(
                    UserEntity(
                        email = emailInputModel.editTextContent!!,
                        name = nameInputModel.editTextContent!!,
                        password = passwordInputModel.editTextContent!!,
                        country = countryInputModel.selectedDropDownModel.value!!.id
                    )
                )
                withContext(Dispatchers.Default) {
                    _state.value = RegisterFragState.SaveComplete("Registration Complete")
                }
            }
        } else {
            _state.value = RegisterFragState.Idle
        }
    }
}