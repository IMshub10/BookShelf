package com.summer.bookshelf.persistence.pref

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

class Preference(private val sharedPreferences: SharedPreferences) {

    companion object {
        private const val APP_PREFERENCE_NAME = "book_shelf_pref"

        private const val USER_LOGIN_STATUS = "user_login_status"

        fun createSharedPref(applicationContext: Context): SharedPreferences =
            applicationContext.getSharedPreferences(APP_PREFERENCE_NAME, MODE_PRIVATE)
    }

    suspend fun setUserLoginStatus(loggedIn: Boolean) {
        sharedPreferences.edit().putBoolean(USER_LOGIN_STATUS, loggedIn).apply()
    }

    suspend fun getUserLoginStatus() =
        sharedPreferences.getBoolean(USER_LOGIN_STATUS, false)

}