package com.summer.bookshelf.ui.inputs

import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.annotation.StringRes
import androidx.databinding.BaseObservable
import androidx.databinding.BindingAdapter
import com.summer.bookshelf.R
import com.summer.bookshelf.ui.models.DropdownModel
import kotlinx.coroutines.flow.MutableStateFlow

class DropdownInputModel(
    @StringRes val hint: Int,
    var isRequired: Boolean = false,
    var dropdownList: List<DropdownModel> = listOf(),
) : BaseObservable() {

    var errorString = MutableStateFlow<Int?>(null)

    var selectedDropDownModel = MutableStateFlow<DropdownModel?>(null)

    fun setSelectedDropDownModel(model: DropdownModel) {
        selectedDropDownModel.value = model
        notifyChange()
    }

    fun setError(@StringRes error: Int?) {
        errorString.value = error
        notifyChange()
    }

    fun isValid() =
        if (isRequired) {
            selectedDropDownModel.value != null
        } else {
            true
        }

    companion object {

        @BindingAdapter("setUpDropDown")
        @JvmStatic
        fun AutoCompleteTextView.setUpDropDown(model: DropdownInputModel) {

            val adapter = ArrayAdapter(context, R.layout.item_drop_down_layout, model.dropdownList)
            setAdapter(adapter)

            model.selectedDropDownModel.value?.let {
                this.setText(it.title, false)
            }

            setOnItemClickListener { _, _, i, _ ->
                val item = model.dropdownList[i]
                model.selectedDropDownModel.value = item
                error = null
            }
        }
    }
}