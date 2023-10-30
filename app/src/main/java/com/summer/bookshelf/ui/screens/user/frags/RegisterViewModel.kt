package com.summer.bookshelf.ui.screens.user.frags

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.summer.bookshelf.R
import com.summer.bookshelf.persistence.db.entities.UserEntity
import com.summer.bookshelf.repositories.LoginRepository
import com.summer.bookshelf.ui.inputs.DropdownInputModel
import com.summer.bookshelf.ui.inputs.TextInputModel
import com.summer.bookshelf.ui.screens.user.states.RegisterFragState
import com.summer.bookshelf.utils.RegexConstants
import com.summer.bookshelf.utils.RegexConstants.containsDigitRegex
import com.summer.bookshelf.utils.RegexConstants.containsLowerAlphabets
import com.summer.bookshelf.utils.RegexConstants.containsSpecialCharacter
import com.summer.bookshelf.utils.RegexConstants.containsUpperAlphabets
import com.summer.bookshelf.utils.RegexConstants.emailRegex
import com.summer.bookshelf.utils.RegexConstants.passwordRegex
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
            it.matches(emailRegex.toRegex())
        },
    )

    val passwordInputModel = TextInputModel(
        hint = R.string.password,
        isRequired = true,
        validator = {
            it.matches(passwordRegex.toRegex())
        },
        obfuscateText = true
    )

    init {
        loadCountries()
    }

    fun loadCountries() {
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
                _state.value = RegisterFragState.Error(
                    "Unable to load countries, check your internet connection",
                    FAILED_TO_LOAD_COUNTRIES
                )
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
            setPasswordError()
            isValid = false
        }

        if (isValid) {
            viewModelScope.launch(Dispatchers.IO) {
                val userExists =
                    loginRepository.checkUserExists(email = emailInputModel.editTextContent!!)
                if (userExists) {
                    _state.value =
                        RegisterFragState.Error(
                            "Account with same e-mail already exists",
                            ACCOUNT_ALREADY_EXISTS
                        )
                } else {
                    loginRepository.insertUser(
                        UserEntity(
                            email = emailInputModel.editTextContent!!,
                            name = nameInputModel.editTextContent!!,
                            password = passwordInputModel.editTextContent!!,
                            countryCode = countryInputModel.selectedDropDownModel.value!!.id
                        )
                    )
                    _state.value = RegisterFragState.SaveComplete("Registration Complete")
                }
            }
        } else {
            _state.value = RegisterFragState.Idle
        }
    }

    private fun setPasswordError() {
        if (passwordInputModel.editTextContent.orEmpty().isEmpty())
            passwordInputModel.setError(R.string.invalid_password)
        else if (!passwordInputModel.editTextContent!!.matches(containsDigitRegex.toRegex()))
            passwordInputModel.setError(R.string.missing_numbers)
        else if (!passwordInputModel.editTextContent!!.matches(containsLowerAlphabets.toRegex()))
            passwordInputModel.setError(R.string.missing_lower_alphas)
        else if (!passwordInputModel.editTextContent!!.matches(containsUpperAlphabets.toRegex()))
            passwordInputModel.setError(R.string.missing_upper_alphas)
        else if (!passwordInputModel.editTextContent!!.matches(containsSpecialCharacter.toRegex()))
            passwordInputModel.setError(R.string.missing_special_characters)
        else
            passwordInputModel.setError(R.string.password_must_be_of_8)
    }

    companion object {
        const val FAILED_TO_LOAD_COUNTRIES = 1
        const val ACCOUNT_ALREADY_EXISTS = 2
    }
}