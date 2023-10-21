package com.summer.bookshelf.utils.extensions

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment

fun View.visible() {
    isVisible = true
}

fun View.gone() {
    isGone = true
}

fun View.invisible() {
    isInvisible = true
}

fun Fragment.showShortToast(content: String) =
    Toast.makeText(requireContext(), content, Toast.LENGTH_SHORT).show()

fun Fragment.showLongToast(content: String) =
    Toast.makeText(requireContext(), content, Toast.LENGTH_LONG).show()

fun Activity.showShortToast(content: String) =
    Toast.makeText(this, content, Toast.LENGTH_SHORT).show()

fun Activity.showLongToast(content: String) =
    Toast.makeText(this, content, Toast.LENGTH_LONG).show()

fun Activity.closeApp() {
    startActivity(Intent(Intent.ACTION_MAIN).apply {
        addCategory(Intent.CATEGORY_HOME)
    })
}