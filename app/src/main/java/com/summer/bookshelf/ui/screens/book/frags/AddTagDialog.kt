package com.summer.bookshelf.ui.screens.book.frags

import android.os.Bundle
import android.view.View
import com.summer.bookshelf.R
import com.summer.bookshelf.base.ui.BaseDialogFragment
import com.summer.bookshelf.databinding.DialogAddTagBinding
import com.summer.bookshelf.ui.screens.book.TagViewModel
import com.summer.bookshelf.ui.screens.book.states.CreateTagState
import com.summer.bookshelf.utils.extensions.collectLatestFlow
import com.summer.bookshelf.utils.extensions.showShortToast
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddTagDialog :
    BaseDialogFragment<DialogAddTagBinding>() {

    override val layoutResId: Int
        get() = R.layout.dialog_add_tag

    private val viewModel: TagViewModel by viewModel()

    override fun onFragmentReady(instanceState: Bundle?) {
        isCancelable = false
        mBinding.model = viewModel
        arguments?.getString(BOOK_ID)?.let {
            viewModel.bookId = it
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observe()

        listeners()
    }

    private fun observe() {
        collectLatestFlow(viewModel.state) {
            when (it) {
                is CreateTagState.Error -> {
                    showShortToast(it.message)
                }

                CreateTagState.Idle -> {}
                CreateTagState.SaveComplete -> {
                    dismiss()
                }
            }
        }
    }

    private fun listeners() {
        with(mBinding) {
            tvDialogAddTagCancelButton.setOnClickListener {
                dismiss()
            }
            tvDialogAddTagConfirmButton.setOnClickListener {
                viewModel.validateNSave()
            }
        }
    }

    companion object {
        const val BOOK_ID = "book_id"

        fun onNewInstance(bookId: String) = AddTagDialog().apply {
            arguments = Bundle().apply {
                putString(BOOK_ID, bookId)
            }
        }
    }
}