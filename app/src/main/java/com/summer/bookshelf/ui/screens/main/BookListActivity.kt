package com.summer.bookshelf.ui.screens.main

import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import com.summer.bookshelf.R
import com.summer.bookshelf.base.ui.BaseActivity
import com.summer.bookshelf.databinding.ActivityBookListBinding
import com.summer.bookshelf.ui.adapters.BookAdapter
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

        listeners()
    }

    private fun listeners() {
        with(mBinding) {
            tabActBookList.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    tab?.text?.let {
                        val year = try {
                            it.toString().toInt()
                        } catch (e: Exception) {
                            e.printStackTrace()
                            null
                        }
                        year?.let {
                            adapter.submitList(viewModel.getBooksByYear(year))
                        }
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {}

                override fun onTabReselected(tab: TabLayout.Tab?) {

                }

            })
        }
    }

    private fun setUpTabs(list: Set<Int>) {
        with(mBinding) {
            list.forEach {
                tabActBookList.addTab(tabActBookList.newTab().setText(it.toString()))
            }
        }
    }

    private fun setupRecyclerView() {
        with(mBinding) {
            rvActBookList.adapter = adapter
        }
    }

    private fun observe() {
        collectLatestFlow(viewModel.books) {
            it?.let {
                setUpTabs(it.keys)
            }
        }
    }
}