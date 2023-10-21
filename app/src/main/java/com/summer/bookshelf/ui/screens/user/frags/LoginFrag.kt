package com.summer.bookshelf.ui.screens.user.frags

import android.os.Bundle
import com.summer.bookshelf.R
import com.summer.bookshelf.base.ui.BaseFragment
import com.summer.bookshelf.databinding.FragLoginBinding
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class LoginFrag : BaseFragment<FragLoginBinding>() {

    override val layoutResId: Int
        get() = R.layout.frag_login

    private val viewModel: RegisterViewModel by activityViewModel()

    override fun onFragmentReady(instanceState: Bundle?) {

    }
}