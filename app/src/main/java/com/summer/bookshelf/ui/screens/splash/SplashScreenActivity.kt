package com.summer.bookshelf.ui.screens.splash

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.summer.bookshelf.R
import com.summer.bookshelf.base.ui.BaseActivity
import com.summer.bookshelf.databinding.ActivitySplashScreenBinding
import com.summer.bookshelf.ui.screens.book.BookListActivity
import com.summer.bookshelf.ui.screens.user.UserActivity
import com.summer.bookshelf.utils.LauncherUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : BaseActivity<ActivitySplashScreenBinding>() {

    override val layoutResId: Int
        get() = R.layout.activity_splash_screen

    private val viewModel: SplashViewModel by viewModel()

    override fun onActivityReady(savedInstanceState: Bundle?) {
        lifecycleScope.launch {
            val checkAccExists = viewModel.getUserLoginStatus()
            withContext(Dispatchers.Main) {
                LauncherUtils.startActivityWithClearTop(
                    this@SplashScreenActivity,
                    if (checkAccExists) BookListActivity::class.java else UserActivity::class.java
                )
            }
        }
    }
}