package com.summer.bookshelf.repositories

import com.summer.bookshelf.persistence.pref.Preference

class SplashRepositoryImpl(private val preference: Preference) : SplashRepository {
    override suspend fun getUserLoginStatus() = preference.getLoggedInUserId() != 0
}