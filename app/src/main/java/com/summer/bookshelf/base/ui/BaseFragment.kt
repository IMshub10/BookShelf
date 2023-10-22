package com.summer.bookshelf.base.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.summer.bookshelf.ui.dialogs.HelperAlertDialog

abstract class BaseFragment<B : ViewDataBinding> : Fragment() {

    @get:LayoutRes
    protected abstract val layoutResId: Int

    private var binding: B? = null
    protected val mBinding: B
        get() = binding!!

    var helperDialog: HelperAlertDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(
                layoutInflater,
                layoutResId,
                container,
                false
            )
        onFragmentReady(savedInstanceState)
        return mBinding.root
    }

    protected abstract fun onFragmentReady(instanceState: Bundle?)

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }


    fun showHelperDialog(dialogType: HelperAlertDialog.DialogType = HelperAlertDialog.DialogType.NO_BUTTON) {
        if (helperDialog == null) {
            helperDialog = HelperAlertDialog(dialogType)
        } else {
            helperDialog?.changeAlertType(dialogType)
        }
        if (helperDialog?.isAdded != true) {
            helperDialog?.show(childFragmentManager, "")
        }
    }
}