package com.summer.bookshelf.ui.screens.splash

import androidx.lifecycle.ViewModel
import com.summer.bookshelf.repositories.SplashRepository

class SplashViewModel(private val splashRepository: SplashRepository) : ViewModel() {

    suspend fun getUserLoginStatus() =
        splashRepository.getUserLoginStatus()
}