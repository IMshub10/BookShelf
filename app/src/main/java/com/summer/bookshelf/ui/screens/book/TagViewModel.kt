package com.summer.bookshelf.ui.screens.book

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.summer.bookshelf.R
import com.summer.bookshelf.repositories.BookRepository
import com.summer.bookshelf.ui.inputs.TextInputModel
import com.summer.bookshelf.ui.screens.book.states.CreateTagState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TagViewModel(private val bookRepository: BookRepository) : ViewModel() {

    private val _state = MutableStateFlow<CreateTagState>(CreateTagState.Idle)
    val state: StateFlow<CreateTagState> = _state.asStateFlow()

    lateinit var bookId: String

    val tagInputModel = TextInputModel(
        hint = R.string.name,
        isRequired = true,
        validator = {
            it.isNotBlank()
        },
    )

    fun validateNSave() {
        var isValid = true

        if (!tagInputModel.isValid()) {
            tagInputModel.setError(R.string.invalid_input)
            isValid = false
        }
        if (isValid) {
            viewModelScope.launch(Dispatchers.IO) {
                bookRepository.addTag(bookId = bookId, tagName = tagInputModel.editTextContent!!)
                _state.value = CreateTagState.SaveComplete
            }
        } else {
            _state.value = CreateTagState.Idle
        }
    }
}