package com.summer.bookshelf.persistence.pref

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

class Preference(private val sharedPreferences: SharedPreferences) {

    companion object {
        private const val APP_PREFERENCE_NAME = "book_shelf_pref"

        fun createSharedPref(applicationContext: Context): SharedPreferences =
            applicationContext.getSharedPreferences(APP_PREFERENCE_NAME, MODE_PRIVATE)
    }

}