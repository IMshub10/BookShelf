package com.summer.bookshelf.ui.screens.book

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import com.summer.bookshelf.R
import com.summer.bookshelf.base.ui.BaseActivity
import com.summer.bookshelf.databinding.ActivityBookListBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class BookListActivity : BaseActivity<ActivityBookListBinding>() {

    override val layoutResId: Int
        get() = R.layout.activity_book_list

    private val viewModel: BookListViewModel by viewModel()

    private var searchItem: MenuItem? = null

    private val searchHandler = Handler(Looper.getMainLooper())

    override fun onActivityReady(savedInstanceState: Bundle?) {
        setupActionBar(mBinding.tbActBookList)

        listeners()
    }

    private fun listeners() {
        with(mBinding) {

        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_book_list, menu)
        searchItem = menu?.findItem(R.id.item_search)

        val searchView = menu?.findItem(R.id.item_search)?.actionView as SearchView?
        searchView?.maxWidth = Int.MAX_VALUE
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchHandler.apply {
                    removeCallbacksAndMessages(null)
                    postDelayed({
                        viewModel.loadBooks(query.orEmpty())
                    }, 1000L)
                }
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                searchHandler.apply {
                    removeCallbacksAndMessages(null)
                    postDelayed({
                        viewModel.loadBooks(query.orEmpty())
                    }, 1000L)
                }
                return false
            }
        })
        searchView?.setOnSearchClickListener {
            mBinding.tbActBookList.title = null
        }
        searchView?.setOnCloseListener {
            mBinding.tbActBookList.title = getString(R.string.app_name)
            return@setOnCloseListener false
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.item_logout) {
            viewModel.logout()
            return true
        }
        return super.onOptionsItemSelected(item)


    }
}