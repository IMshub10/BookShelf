package com.summer.bookshelf.ui.screens.user.frags

import android.os.Bundle
import android.view.View
import androidx.biometric.BiometricPrompt
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.summer.bookshelf.R
import com.summer.bookshelf.base.ui.BaseFragment
import com.summer.bookshelf.databinding.FragLoginBinding
import com.summer.bookshelf.ui.dialogs.HelperAlertDialog
import com.summer.bookshelf.ui.screens.book.BookListActivity
import com.summer.bookshelf.ui.screens.user.states.LoginState
import com.summer.bookshelf.utils.BiometricResultListener
import com.summer.bookshelf.utils.LauncherUtils
import com.summer.bookshelf.utils.canAuthenticateWithBiometric
import com.summer.bookshelf.utils.extensions.collectLatestFlow
import com.summer.bookshelf.utils.extensions.showShortToast
import com.summer.bookshelf.utils.verifyFingerPrint
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFrag : BaseFragment<FragLoginBinding>(), BiometricResultListener {

    override val layoutResId: Int
        get() = R.layout.frag_login

    private val viewModel: LoginViewModel by viewModel()

    override fun onFragmentReady(instanceState: Bundle?) {

        mBinding.model = viewModel

        mBinding.ivFragLoginFingerPrint.isVisible = canAuthenticateWithBiometric()

        viewModel.resetStates()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observe()

        listeners()
    }

    private fun observe() {
        collectLatestFlow(viewModel.state) {
            when (it) {
                LoginState.Idle -> {
                    helperDialog?.dismiss()
                }

                is LoginState.Loading -> {
                    showHelperDialog(HelperAlertDialog.DialogType.PROGRESS)
                    helperDialog?.setTitleText(it.message)
                }

                is LoginState.Error -> {
                    showHelperDialog(HelperAlertDialog.DialogType.NO_BUTTON)
                    helperDialog?.setTitleText(it.message)
                }

                LoginState.UserLoggedIn -> {
                    LauncherUtils.startActivityWithClearTop(
                        requireActivity(),
                        BookListActivity::class.java
                    )
                }
            }
        }
    }

    private fun listeners() {
        with(mBinding) {
            mbFragLoginRegister.setOnClickListener {
                if (findNavController().currentDestination?.id == R.id.loginFrag)
                    findNavController().navigate(R.id.action_loginFrag_to_registerFrag)
            }

            mbFragLoginLogin.setOnClickListener {
                viewModel.validateNLogin(false)
            }

            ivFragLoginFingerPrint.setOnClickListener {
                verifyFingerPrint(this@LoginFrag)
            }
        }
    }


    override fun onBiometricSuccess(result: BiometricPrompt.AuthenticationResult) =
        viewModel.validateNLogin(true)

    override fun onBiometricError(message: String) =
        showShortToast(message)
}