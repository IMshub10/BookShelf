package com.summer.bookshelf.utils

import android.app.Activity
import android.util.DisplayMetrics

object UiUtils {

    fun getScreenWidthIntDp(activity: Activity): Int {
        val displayMetrics = DisplayMetrics()
        activity.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        return (displayMetrics.widthPixels)
    }
}