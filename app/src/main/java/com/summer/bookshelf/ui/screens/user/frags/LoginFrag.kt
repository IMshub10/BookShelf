package com.summer.bookshelf.ui.screens.user.frags

import android.os.Bundle
import androidx.biometric.BiometricPrompt
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.summer.bookshelf.R
import com.summer.bookshelf.base.ui.BaseActivity
import com.summer.bookshelf.base.ui.BaseFragment
import com.summer.bookshelf.databinding.FragLoginBinding
import com.summer.bookshelf.ui.dialogs.HelperAlertDialog
import com.summer.bookshelf.ui.screens.main.MainActivity
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

        observe()

        listeners()
    }

    private fun observe() {
        collectLatestFlow(viewModel.state) {
            when (it) {
                LoginState.Idle -> {
                    with((requireActivity() as BaseActivity<*>)) {
                        helperDialog?.dismiss()
                    }
                }

                is LoginState.Loading -> {
                    with((requireActivity() as BaseActivity<*>)) {
                        showHelperDialog(HelperAlertDialog.DialogType.PROGRESS)
                        helperDialog?.setTitleText(it.message)
                    }
                }

                is LoginState.Error -> {
                    with((requireActivity() as BaseActivity<*>)) {
                        showHelperDialog(HelperAlertDialog.DialogType.NO_BUTTON)
                        helperDialog?.setTitleText(it.message)
                    }
                }

                LoginState.UserLoggedIn -> {
                    LauncherUtils.startActivityWithClearTop(
                        requireActivity(),
                        MainActivity::class.java
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
                viewModel.validateNLogin()
            }

            ivFragLoginFingerPrint.setOnClickListener {
                verifyFingerPrint(this@LoginFrag)
            }
        }
    }


    override fun onBiometricSuccess(result: BiometricPrompt.AuthenticationResult) =
        LauncherUtils.startActivityWithClearTop(
            requireActivity(),
            MainActivity::class.java
        )

    override fun onBiometricError(message: String) =
        showShortToast(message)
}