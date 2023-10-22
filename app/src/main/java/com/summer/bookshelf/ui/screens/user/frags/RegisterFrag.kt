package com.summer.bookshelf.ui.screens.user.frags

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.summer.bookshelf.R
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
        viewModel.loadCountries()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observe()

        listeners()
    }

    private fun observe() {
        collectLatestFlow(viewModel.state) {
            when (it) {
                is RegisterFragState.Error -> {
                    if (it.errorCode == RegisterViewModel.ACCOUNT_ALREADY_EXISTS) {
                        showHelperDialog(HelperAlertDialog.DialogType.NO_BUTTON)
                        helperDialog?.setTitleText(it.message)
                    } else {
                        showHelperDialog(HelperAlertDialog.DialogType.TWO_BUTTON)
                        helperDialog?.setTitleText(it.message)
                        helperDialog?.setConfirmText(getString(R.string.reload))
                        helperDialog?.setConfirmClickListener {
                            viewModel.loadCountries()
                        }
                    }
                }

                RegisterFragState.Idle -> {
                    helperDialog?.dismiss()
                }

                is RegisterFragState.Loading -> {
                    showHelperDialog(HelperAlertDialog.DialogType.PROGRESS)
                    helperDialog?.setTitleText(it.message)
                }

                is RegisterFragState.SaveComplete -> {
                    showHelperDialog(HelperAlertDialog.DialogType.SUCCESS)
                    helperDialog?.setTitleText(it.message)
                    helperDialog?.setConfirmClickListener {
                        if (findNavController().currentDestination?.id == R.id.registerFrag)
                            findNavController().navigateUp()
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
            ivFragRegBack.setOnClickListener {
                if (findNavController().currentDestination?.id == R.id.registerFrag)
                    findNavController().navigateUp()
            }
        }
    }
}