package com.summer.bookshelf.utils

import android.app.Activity
import android.content.Intent

object LauncherUtils {

    fun startActivityWithClearTop(activity: Activity, clas: Class<*>?) {
        activity.startActivity(Intent(activity, clas).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        })
        activity.finish()
    }
}