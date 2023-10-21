package com.summer.bookshelf.utils

import android.text.SpannableString
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.text.buildSpannedString
import androidx.core.text.color
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.textfield.TextInputLayout
import com.summer.bookshelf.R
import com.summer.bookshelf.ui.inputs.DropdownInputModel
import com.summer.bookshelf.ui.inputs.TextInputModel
import com.summer.bookshelf.utils.extensions.gone
import com.summer.bookshelf.utils.extensions.visible

object ViewConvertors {

    @BindingAdapter("setSpannableText")
    @JvmStatic
    fun AppCompatTextView.setSpannableText(
        spannableString: SpannableString,
    ) {
        if (spannableString.toString().isEmpty()) this.gone() else this.visible()
        text = spannableString
    }


    @BindingAdapter("setRequired")
    @JvmStatic
    fun TextInputLayout.setRequired(isRequired: Boolean) {
        if (isRequired) makeRequired()
    }

    private fun TextInputLayout.makeRequired() {
        if (hint == null) return
        hint = buildSpannedString {
            append(hint)
            color(resources.getColor(R.color.error)) { append("*") }
        }
    }

    @BindingAdapter("setRequired")
    @JvmStatic
    fun AppCompatTextView.setRequired(isRequired: Boolean) {
        if (isRequired) makeRequired()
    }

    private fun AppCompatTextView.makeRequired() {
        if (text == null) return
        text = buildSpannedString {
            append(text)
            if (text[text.lastIndex] != '*')
                color(resources.getColor(R.color.error)) { append("*") }
        }
    }

    @JvmStatic
    @BindingAdapter("setText")
    fun AppCompatTextView.setText(@StringRes resId: Int?) {
        text = context.getString(resId ?: R.string.empty)
    }

    @JvmStatic
    @BindingAdapter(value = ["required", "hint"], requireAll = true)
    fun TextInputLayout.setHint(required: Boolean, @StringRes hint: Int) {
        this.hint = context.getString(hint)
        setRequired(required)
    }

    @JvmStatic
    @BindingAdapter("setError")
    fun TextInputLayout.setError(@StringRes error: Int?) {
        this.error = error?.let { context.getString(it) }
    }


    @JvmStatic
    @BindingAdapter("setImage")
    fun AppCompatImageView.setImage(imageUrl: String?) {
        imageUrl?.let {
            Glide.with(context).load(imageUrl)
                .fitCenter()
                .error(R.drawable.ic_launcher_foreground)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(this)
        } ?: Glide.with(context).load(R.drawable.ic_launcher_foreground)
            .fitCenter()
            .error(R.drawable.ic_launcher_foreground)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(this)
    }

}