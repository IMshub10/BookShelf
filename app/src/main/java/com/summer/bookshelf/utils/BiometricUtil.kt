package com.summer.bookshelf.utils

import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.summer.bookshelf.R

fun Fragment.verifyFingerPrint(listener: BiometricResultListener) {
    BiometricPrompt(this, ContextCompat.getMainExecutor(requireContext()),
        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                listener.onBiometricError(errString.toString())
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                listener.onBiometricSuccess(result)

            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                listener.onBiometricError(resources.getString(R.string.cancel))

            }
        }).authenticate(
        BiometricPrompt.PromptInfo.Builder()
            .setTitle(resources.getString(R.string.app_name))
            .setSubtitle(resources.getString(R.string.authenticate_with_biometric))
            .setNegativeButtonText(resources.getString(R.string.cancel))
            .setAllowedAuthenticators(BIOMETRIC_STRONG)
            .build()
    )
}

fun FragmentActivity.verifyFingerPrint(listener: BiometricResultListener) {
    BiometricPrompt(this, ContextCompat.getMainExecutor(this),
        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                listener.onBiometricError(errString.toString())
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                listener.onBiometricSuccess(result)

            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                listener.onBiometricError(resources.getString(R.string.cancel))

            }
        }).authenticate(
        BiometricPrompt.PromptInfo.Builder()
            .setTitle(resources.getString(R.string.app_name))
            .setSubtitle(resources.getString(R.string.authenticate_with_biometric))
            .setNegativeButtonText(resources.getString(R.string.cancel))
            .setAllowedAuthenticators(BIOMETRIC_STRONG)
            .build()
    )
}


interface BiometricResultListener {
    fun onBiometricSuccess(result: BiometricPrompt.AuthenticationResult)
    fun onBiometricError(message: String)
}


fun Fragment.canAuthenticateWithBiometric(): Boolean {
    val biometricManager = BiometricManager.from(requireContext())
    return when (biometricManager.canAuthenticate(BIOMETRIC_STRONG)) {
        BiometricManager.BIOMETRIC_SUCCESS -> true
        else -> false
    }
}
