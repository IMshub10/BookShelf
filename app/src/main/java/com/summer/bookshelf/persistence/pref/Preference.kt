package com.summer.bookshelf.persistence.pref

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

class Preference(private val sharedPreferences: SharedPreferences) {

    companion object {
        private const val APP_PREFERENCE_NAME = "book_shelf_pref"

        private const val LOGGED_IN_USER_ID = "logged_in_user_id"

        fun createSharedPref(applicationContext: Context): SharedPreferences =
            applicationContext.getSharedPreferences(APP_PREFERENCE_NAME, MODE_PRIVATE)
    }

    suspend fun setLoggedInUserId(userId: Int) {
        sharedPreferences.edit().putInt(LOGGED_IN_USER_ID, userId).apply()
    }

    suspend fun getLoggedInUserId() =
        sharedPreferences.getInt(LOGGED_IN_USER_ID, 0)

}