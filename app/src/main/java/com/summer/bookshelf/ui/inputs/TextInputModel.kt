package com.summer.bookshelf.ui.inputs

import androidx.annotation.StringRes
import androidx.core.widget.addTextChangedListener
import androidx.databinding.BaseObservable
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.flow.MutableStateFlow

class TextInputModel(
    @StringRes val hint: Int,
    var singleLine: Boolean = true,
    var isRequired: Boolean = false,
    val validator: (input: String) -> Boolean,
) : BaseObservable() {

    var errorString = MutableStateFlow<Int?>(null)

    var editTextContent: String? = null


    fun setError(@StringRes error: Int?) {
        errorString.value = error
        notifyChange()
    }

    fun isValid() =
        if (isRequired) {
            editTextContent?.let(validator) ?: false
        } else {
            true
        }


    fun setModel(content: String) {
        editTextContent = content
        notifyChange()
    }

    fun reset() {
        editTextContent = ""
        notifyChange()
    }


    companion object {
        @BindingAdapter("setUpTextEditText")
        @JvmStatic
        fun TextInputEditText.setUpTextEditText(model: TextInputModel) {

            setText(model.editTextContent.orEmpty())

            //listeners
            setOnFocusChangeListener { _, _ ->
                if (model.errorString.value != null) {
                    model.setError(null)
                }
            }

            addTextChangedListener {
                model.editTextContent = it?.trim()?.toString()
                this.error = null
            }
        }
    }
}