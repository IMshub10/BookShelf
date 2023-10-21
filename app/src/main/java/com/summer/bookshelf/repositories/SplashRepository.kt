package com.summer.bookshelf.repositories

interface SplashRepository {
    suspend fun getUserLoginStatus(): Boolean
}