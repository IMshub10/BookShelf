package com.summer.bookshelf.ui.screens.user.frags

import android.os.Bundle
import com.summer.bookshelf.R
import com.summer.bookshelf.base.ui.BaseActivity
import com.summer.bookshelf.base.ui.BaseFragment
import com.summer.bookshelf.databinding.FragRegisterBinding
import com.summer.bookshelf.ui.dialogs.HelperAlertDialog
import com.summer.bookshelf.ui.screens.user.states.RegisterFragState
import com.summer.bookshelf.utils.extensions.collectLatestFlow
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterFrag : BaseFragment<FragRegisterBinding>() {

    override val layoutResId: Int
        get() = R.layout.frag_register

    private val viewModel: RegisterViewModel by viewModel()

    override fun onFragmentReady(instanceState: Bundle?) {
        mBinding.model = viewModel

        observe()

        listeners()
    }

    private fun observe() {
        collectLatestFlow(viewModel.state) {
            when (it) {
                is RegisterFragState.Error -> {
                    with((requireActivity() as BaseActivity<*>)) {
                        showHelperDialog(HelperAlertDialog.DialogType.NO_BUTTON)
                        helperDialog?.setTitleText(it.message)
                    }
                }

                RegisterFragState.Idle -> {
                    with((requireActivity() as BaseActivity<*>)) {
                        helperDialog?.dismiss()
                    }
                }

                is RegisterFragState.Loading -> {
                    with((requireActivity() as BaseActivity<*>)) {
                        showHelperDialog(HelperAlertDialog.DialogType.PROGRESS)
                        helperDialog?.setTitleText(it.message)
                    }
                }

                is RegisterFragState.SavingData -> {
                    with((requireActivity() as BaseActivity<*>)) {
                        showHelperDialog(HelperAlertDialog.DialogType.PROGRESS)
                        helperDialog?.setTitleText(it.message)
                    }
                }

                is RegisterFragState.SaveComplete -> {
                    with((requireActivity() as BaseActivity<*>)) {
                        showHelperDialog(HelperAlertDialog.DialogType.SUCCESS)
                        helperDialog?.setTitleText(it.message)
                        helperDialog?.setConfirmClickListener {
                            requireActivity().finish()
                        }
                    }
                }
            }
        }
    }


    private fun listeners() {
        with(mBinding) {
            mbFragRegSave.setOnClickListener {
                viewModel.validateNSave()
            }
        }
    }
}