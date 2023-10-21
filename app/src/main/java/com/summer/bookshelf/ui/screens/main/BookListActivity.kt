package com.summer.bookshelf.ui.screens.main

import android.os.Bundle
import android.util.Log
import com.summer.bookshelf.R
import com.summer.bookshelf.base.ui.BaseActivity
import com.summer.bookshelf.databinding.ActivityBookListBinding
import com.summer.bookshelf.databinding.ActivitySplashScreenBinding
import com.summer.bookshelf.ui.screens.splash.SplashViewModel
import com.summer.bookshelf.utils.extensions.collectLatestFlow
import org.koin.androidx.viewmodel.ext.android.viewModel

class BookListActivity : BaseActivity<ActivityBookListBinding>() {

    override val layoutResId: Int
        get() = R.layout.activity_book_list

    private val viewModel: BookListViewModel by viewModel()

    override fun onActivityReady(savedInstanceState: Bundle?) {
        setupActionBar(mBinding.tbActBookList)

        observe()
    }

    private fun observe() {
        collectLatestFlow(viewModel.books) {
            it?.let {
                Log.d("Books", it.toString())
            }
        }
    }
}