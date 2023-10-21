package com.summer.bookshelf.ui.dialogs

import android.app.Dialog
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.databinding.ObservableField
import androidx.fragment.app.DialogFragment
import com.summer.bookshelf.R
import com.summer.bookshelf.databinding.DialogAlertSpanableBinding
import com.summer.bookshelf.utils.UiUtils
import com.summer.bookshelf.utils.extensions.gone
import com.summer.bookshelf.utils.extensions.setValue
import com.summer.bookshelf.utils.extensions.visible

class HelperAlertDialog(
    private var spannableDialogType: DialogType,
) : DialogFragment() {

    enum class DialogType {
        NO_BUTTON,
        TWO_BUTTON,
        PROGRESS,
        SUCCESS
    }

    data class UiModels(
        val titleText: ObservableField<String> = ObservableField(""),
        val contentText: ObservableField<SpannableString> = ObservableField(SpannableString("")),
        val confirmText: ObservableField<String> = ObservableField(""),
        val cancelText: ObservableField<String> = ObservableField(""),
    )

    private var mBinding: DialogAlertSpanableBinding? = null
    private val uiModels = UiModels()
    private var confirmClickListener: View.OnClickListener? = null
    private var cancelClickListener: View.OnClickListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        dialog?.window?.attributes?.windowAnimations = R.style.SpannableDialogAnimation
        mBinding = DialogAlertSpanableBinding.inflate(layoutInflater)
        if (uiModels.confirmText.get().isNullOrEmpty()) {
            uiModels.confirmText.setValue(getString(R.string.txt_ok))
        }
        if (uiModels.cancelText.get().isNullOrEmpty()) {
            uiModels.cancelText.setValue(getString(R.string.cancel))
        }
        mBinding?.run {
            model = uiModels
        }
        isCancelable = false
        initUI()
        return mBinding?.root
    }

    private fun initUI() {
        mBinding?.run {
            tvDialogAlertConfirmButton.setOnClickListener(confirmClickListener)
            tvDialogAlertCancelButton.setOnClickListener(
                cancelClickListener ?: View.OnClickListener { dismiss() })
            ivDialogAlertClose.setOnClickListener(cancelClickListener ?: View.OnClickListener {
                if (dialog?.isShowing == true) {
                    dismiss()
                }
            })
            when (spannableDialogType) {
                DialogType.NO_BUTTON -> {
                    ivDialogAlertIcon.setBackgroundResource(R.drawable.ic_error_circular_primary_filled)
                    pgDialogAlertProgress.gone()
                    clDialogAlertContainer.gone()
                    ivDialogAlertClose.visible()
                    ivDialogAlertIcon.visible()
                }

                DialogType.TWO_BUTTON -> {
                    ivDialogAlertIcon.setBackgroundResource(R.drawable.ic_error_circular_primary_filled)
                    pgDialogAlertProgress.gone()
                    ivDialogAlertClose.gone()
                    clDialogAlertContainer.visible()
                    ivDialogAlertIcon.visible()
                }

                DialogType.PROGRESS -> {
                    ivDialogAlertIcon.setBackgroundResource(R.drawable.ic_error_circular_primary_filled)
                    ivDialogAlertIcon.gone()
                    clDialogAlertContainer.gone()
                    ivDialogAlertClose.gone()
                    pgDialogAlertProgress.visible()
                }

                DialogType.SUCCESS -> {
                    ivDialogAlertIcon.setBackgroundResource(R.drawable.round_check_circle_24)
                    clDialogAlertContainer.gone()
                    ivDialogAlertClose.gone()
                    pgDialogAlertProgress.gone()
                    tvDialogAlertCancelButton.gone()
                    ivDialogAlertIcon.visible()
                    clDialogAlertContainer.visible()
                    tvDialogAlertConfirmButton.visible()
                }
            }
        }
    }

    fun changeAlertType(dialogType: DialogType) {
        spannableDialogType = dialogType
        uiModels.titleText.setValue("")
        uiModels.contentText.setValue(SpannableString(""))
        initUI()
    }

    fun setContentText(
        contentString: String,
        spanStartIndex: Int = -1,
        spanEndIndex: Int = -1,
        spanColor: Int = 0,
    ) {
        uiModels.contentText.set(
            if (spanColor != 0 && spanStartIndex != -1 && spanEndIndex != -1) {
                val spannable = SpannableString(contentString)
                spannable.setSpan(
                    ForegroundColorSpan(spanColor),
                    spanStartIndex,
                    spanEndIndex + 1,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannable
            } else {
                SpannableString(contentString)
            }
        )
        uiModels.contentText.notifyChange()
    }

    fun setTitleText(contentString: String) {
        uiModels.titleText.setValue(
            contentString
        )
    }

    fun setConfirmText(buttonText: String) {
        uiModels.confirmText.setValue(buttonText)
    }

    fun setCancelText(cancelText: String) {
        uiModels.cancelText.setValue(cancelText)
    }

    fun setConfirmClickListener(clickListener: View.OnClickListener) {
        confirmClickListener = clickListener
        mBinding?.tvDialogAlertConfirmButton?.setOnClickListener(clickListener)
    }

    fun setCancelClickListener(clickListener: View.OnClickListener) {
        cancelClickListener = clickListener
        mBinding?.tvDialogAlertCancelButton?.setOnClickListener(clickListener)
        mBinding?.ivDialogAlertClose?.setOnClickListener(clickListener)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?) = Dialog(requireActivity()).apply {
        window?.attributes?.windowAnimations = R.style.SpannableDialogAnimation
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(RelativeLayout(requireActivity()).apply {
            setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(), android.R.color.transparent
                )
            )
            layoutParams = ViewGroup.LayoutParams(
                UiUtils.getScreenWidthIntDp(requireActivity()) - 160,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        })
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        window?.setLayout(
            UiUtils.getScreenWidthIntDp(requireActivity()) - 128,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
}