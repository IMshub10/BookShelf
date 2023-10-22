package com.summer.bookshelf.ui.screens.book.frags

import android.os.Bundle
import android.view.LayoutInflater
import com.summer.bookshelf.R
import com.summer.bookshelf.base.ui.BaseFragment
import com.summer.bookshelf.databinding.FragBookItemBinding
import com.summer.bookshelf.databinding.ItemTagsBinding
import com.summer.bookshelf.ui.screens.book.BookListViewModel
import com.summer.bookshelf.utils.extensions.collectLatestFlow
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class BookItemFrag : BaseFragment<FragBookItemBinding>() {

    override val layoutResId: Int
        get() = R.layout.frag_book_item

    private val viewModel: BookListViewModel by activityViewModel()

    private var addTagDialog: AddTagDialog? = null

    override fun onFragmentReady(instanceState: Bundle?) {
        mBinding.model = viewModel.selectedBookModel
        viewModel.fetchTags()
        observe()

        listeners()
    }

    private fun listeners() {
        with(mBinding) {
            mbItemBookAddTag.setOnClickListener {
                showTagDialog()
            }
        }
    }

    private fun observe() {
        collectLatestFlow(viewModel.tagList) { list ->
            with(mBinding) {
                fbItemBookTagContainer.removeAllViews()
                list.forEach {
                    ItemTagsBinding.inflate(
                        LayoutInflater.from(context),
                        fbItemBookTagContainer,
                        true
                    ).run {
                        this.tvItemTagsRoot.text = it
                    }
                }
            }
        }
    }

    private fun showTagDialog() {
        if (addTagDialog?.isAdded != true) {
            addTagDialog =
                AddTagDialog.onNewInstance(viewModel.selectedBookModel!!.id)
            addTagDialog?.show(childFragmentManager, "")
        }
    }

}