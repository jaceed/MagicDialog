package com.github.jaceed.magicdialog

import android.content.Context
import android.graphics.drawable.PaintDrawable
import android.graphics.drawable.StateListDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.core.content.res.ResourcesCompat
import com.github.jaceed.extender.view.visible
import com.github.jaceed.magicdialog.databinding.FragmentDialogBaseCommonBinding
import com.github.jaceed.magicdialog.utils.colorBy
import com.github.jaceed.magicdialog.utils.colorOf
import com.github.jaceed.magicdialog.utils.colorOr

/**
 * Created by Jacee.
 * Date: 2019.03.18
 */
abstract class BaseCommonDialog : BaseDialog() {

    // An interaction obtained by attached context
    private var attachedInteraction: OnDialogFragmentInteraction? = null
    // An interaction obtained by set function
    private var interactionExcluded: OnDialogFragmentInteraction? = null

    private val normalColor: Int by lazy {
        colorOf(R.attr.magicTextNormalColor, ResourcesCompat.getColor(resources, R.color.magicTextColor, null))
    }
    private val alertColor: Int by lazy {
        colorOr(R.attr.magicTextAlertColor, R.attr.colorPrimary)
    }
    private val warningColor: Int by lazy {
        colorOr(R.attr.magicTextWarningColor, R.attr.colorError)
    }
    private val cornerSize: Float by lazy {
        resources.getDimensionPixelSize(R.dimen.dialog_background_corner).toFloat()
    }
    private val leftRadii by lazy {
        floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f, cornerSize, cornerSize)
    }

    private val rightRadii by lazy {
        floatArrayOf(0f, 0f, 0f, 0f, cornerSize, cornerSize, 0f, 0f)
    }

    private fun TextView.configure(bgNormal: Int, bgPressed: Int, text: Int, left: Boolean) {
        val state = StateListDrawable()
        state.addState(intArrayOf(android.R.attr.state_pressed), PaintDrawable(bgPressed).apply {
            setCornerRadii(if (left) leftRadii else rightRadii)
        })
        state.addState(intArrayOf(), PaintDrawable(bgNormal).apply {
            setCornerRadii(if (left) leftRadii else rightRadii)
        })
        background = state
        setTextColor(text)
    }

    private fun generateButtonPressedColor(@AttrRes attr: Int, textColor: Int): Int {
        return colorOf(attr, textColor) and 0x00FFFFFF or 0x10000000
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        attachedInteraction = if (context is OnDialogFragmentInteraction) context else null
    }

    final override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentDialogBaseCommonBinding.inflate(inflater).apply {
            title.visible = onConfigureTitle(title).also {
                s3.visible = it
            }
            colorBy(R.attr.magicTitleColor) {
                title.setTextColor(it)
            }
            message.visible = onConfigureMessage(message).also {
                s3.visible = it && title.visible
            }
            colorBy(R.attr.magicMessageColor) {
                message.setTextColor(it)
            }

            colorBy(R.attr.magicDivider) {
                divider.setBackgroundColor(it)
            }

            when (onConfigureButtons()) {
                ButtonType.DOUBLE -> {
                    btnCancel.visible = true
                    btnOk.visible = true
                }
                ButtonType.SINGLE -> {
                    btnCancel.visible = false
                    btnOk.visible = true
                }
                else -> {
                    btnCancel.visible = false
                    btnOk.visible = false
                    divider.visible = false
                }
            }

            onConfigureNegative(btnCancel).let {
                when (it) {
                    Action.NORMAL, Action.NONE -> {
                        btnCancel.configure(magicBackgroundColorDefault, generateButtonPressedColor(R.attr.magicTextNormalColor, normalColor), normalColor, true)
                    }
                    Action.ALERT -> {
                        btnCancel.configure(magicBackgroundColorDefault, generateButtonPressedColor(R.attr.magicTextAlertColor, alertColor), alertColor, true)
                    }
                    Action.WARNING -> {
                        btnCancel.configure(magicBackgroundColorDefault, generateButtonPressedColor(R.attr.magicTextWarningColor, warningColor), warningColor, true)
                    }
                }
            }
            onConfigurePositive(btnOk).let {
                when (it) {
                    Action.NORMAL, Action.NONE -> {
                        btnOk.configure(magicBackgroundColorDefault, generateButtonPressedColor(R.attr.magicTextNormalColor, normalColor), normalColor, false)
                    }
                    Action.ALERT -> {
                        btnOk.configure(magicBackgroundColorDefault, generateButtonPressedColor(R.attr.magicTextAlertColor, alertColor), alertColor, false)
                    }
                    Action.WARNING -> {
                        btnOk.configure(magicBackgroundColorDefault, generateButtonPressedColor(R.attr.magicTextWarningColor, warningColor), warningColor, false)
                    }
                }
            }
        }.root
    }

    protected open fun onConfigureTitle(v: TextView): Boolean {
        return false
    }

    protected open fun onConfigureMessage(v: TextView): Boolean {
        return false
    }

    protected open fun onConfigureButtons(): ButtonType {
        return ButtonType.NONE
    }

    /**
     * @return true to show the negative button, default true
     */
    protected open fun onConfigureNegative(v: TextView): Action {
        v.setOnClickListener {
            onNegativeClick(v)
            interactionExcluded?.onDialogNegativeClick(v) ?: attachedInteraction?.onDialogNegativeClick(v)
            dismiss()
        }
        return Action.NORMAL
    }

    /**
     * @return true to show the positive button, default true
     */
    protected open fun onConfigurePositive(v: TextView): Action {
        v.setOnClickListener {
            onPositiveClick(v)
            interactionExcluded?.onDialogPositiveClick(v) ?: attachedInteraction?.onDialogPositiveClick(v)
            dismiss()
        }
        return Action.ALERT
    }

    protected open fun onNegativeClick(v: TextView) {

    }

    protected open fun onPositiveClick(v: TextView) {

    }

    fun setOnDialogFragmentInteraction(interaction: OnDialogFragmentInteraction): BaseCommonDialog {
        interactionExcluded = interaction
        return this
    }

}