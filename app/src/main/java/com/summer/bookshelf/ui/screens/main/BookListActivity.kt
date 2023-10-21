package com.summer.bookshelf.ui.screens.main

import android.os.Bundle
import android.util.Log
import com.summer.bookshelf.R
import com.summer.bookshelf.base.ui.BaseActivity
import com.summer.bookshelf.databinding.ActivityBookListBinding
import com.summer.bookshelf.databinding.ActivitySplashScreenBinding
import com.summer.bookshelf.ui.adapters.BookAdapter
import com.summer.bookshelf.ui.screens.splash.SplashViewModel
import com.summer.bookshelf.utils.extensions.collectLatestFlow
import org.koin.androidx.viewmodel.ext.android.viewModel

class BookListActivity : BaseActivity<ActivityBookListBinding>() {

    override val layoutResId: Int
        get() = R.layout.activity_book_list

    private val viewModel: BookListViewModel by viewModel()

    private val adapter by lazy {
        BookAdapter()
    }

    override fun onActivityReady(savedInstanceState: Bundle?) {
        setupActionBar(mBinding.tbActBookList)

        setupRecyclerView()

        observe()
    }

    private fun setupRecyclerView() {
        with(mBinding) {
            rvActBookList.adapter = adapter
        }
    }

    private fun observe() {
        collectLatestFlow(viewModel.books) {
            it?.let {
                adapter.submitList(it)
            }
        }
    }
}