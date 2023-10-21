package com.summer.bookshelf.ui.models

data class DropdownModel(
    var id: String,
    var title: String,
) {

    override fun toString(): String {
        return title
    }
}