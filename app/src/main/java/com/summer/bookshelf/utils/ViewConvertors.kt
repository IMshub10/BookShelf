package com.summer.bookshelf.utils

import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.content.res.AppCompatResources
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
    @BindingAdapter("setObfuscation")
    fun TextInputLayout.setObfuscation(value: Boolean) {
        if(value){
            endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE
        }
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

    @BindingAdapter("setBookmark")
    @JvmStatic
    fun AppCompatImageView.setBookmark(isBookmarked: Boolean) {
        setImageDrawable(
            AppCompatResources.getDrawable(
                context,
                if (isBookmarked) R.drawable.ic_bookmarked else R.drawable.ic_bookmark
            )
        )
    }

    @BindingAdapter("setRegisterSpannable")
    @JvmStatic
    fun AppCompatTextView.setRegisterSpannable(text: String) {
        this.text = SpannableString(text).apply {
            setSpan(
                ForegroundColorSpan(context.getColor(R.color.blue_light)),
                text.indexOf("Register"),
                text.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }

}