package com.summer.bookshelf.ui.screens.book

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.summer.bookshelf.R
import com.summer.bookshelf.base.ui.BaseActivity
import com.summer.bookshelf.databinding.ActivityBookListBinding
import com.summer.bookshelf.ui.adapters.BookAdapter
import com.summer.bookshelf.ui.dialogs.HelperAlertDialog
import com.summer.bookshelf.ui.models.BookModel
import com.summer.bookshelf.ui.screens.book.states.BookListState
import com.summer.bookshelf.ui.screens.user.UserActivity
import com.summer.bookshelf.utils.LauncherUtils
import com.summer.bookshelf.utils.extensions.collectLatestFlow
import org.koin.androidx.viewmodel.ext.android.viewModel

class BookListActivity : BaseActivity<ActivityBookListBinding>() {

    override val layoutResId: Int
        get() = R.layout.activity_book_list

    private val viewModel: BookListViewModel by viewModel()

    private val adapter by lazy {
        BookAdapter(object : BookAdapter.SelectionCallback {
            override fun onBookmark(bookModel: BookModel) {
                bookModel.isBookmarked = !bookModel.isBookmarked
                viewModel.bookMark(bookModel)
                bookModel.notifyChange()
            }
        })
    }

    override fun onActivityReady(savedInstanceState: Bundle?) {
        setupActionBar(mBinding.tbActBookList)

        setupRecyclerView()

        observe()

        listeners()
    }

    private fun listeners() {
        with(mBinding) {
            rvActBookList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                }

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val firstVisiblePosition =
                        (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

                    if (firstVisiblePosition in 0..<adapter.currentList.size) {
                        val visibleYear = adapter.currentList[firstVisiblePosition].year
                        viewModel.bookYears.value?.indexOf(visibleYear)?.let {
                            tabActBookList.getTabAt(it)?.select()
                        }
                    }
                }
            })

            ivActBookListLogout.setOnClickListener {
                viewModel.logout()
            }
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

        collectLatestFlow(viewModel.state) {
            when (it) {

                BookListState.Idle -> {
                    helperDialog?.dismiss()
                }

                is BookListState.Error -> {
                    showHelperDialog(HelperAlertDialog.DialogType.NO_BUTTON)
                    helperDialog?.setTitleText(it.message)
                }

                is BookListState.Loading -> {
                    showHelperDialog(HelperAlertDialog.DialogType.PROGRESS)
                    helperDialog?.setTitleText(it.message)
                }

                BookListState.Logout -> {
                    LauncherUtils.startActivityWithClearTop(
                        this,
                        UserActivity::class.java
                    )
                }
            }
        }

        collectLatestFlow(viewModel.bookYears) {
            it?.let {
                setUpTabs(it)
            }
        }
        collectLatestFlow(viewModel.books) {
            it?.let {
                adapter.submitList(it)
            }
        }
    }
}