package com.github.jaceed.magicdialog

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.FragmentActivity
import com.github.jaceed.extender.view.visible
import com.github.jaceed.magicdialog.databinding.FragmentDialogBaseCommonBinding

/**
 * Created by Jacee.
 * Date: 2019.03.18
 */
abstract class BaseCommonDialog : BaseDialog() {

    private var listener: OnDialogFragmentInteraction? = null
    private var listenerExcluded: OnDialogFragmentInteraction? = null

    protected fun FragmentActivity.themeBy(@AttrRes attrStyle: Int, attrs: IntArray, res: ((res: TypedArray) -> Unit)? = null) {
        val typedValue = TypedValue()
        theme.resolveAttribute(attrStyle, typedValue, false).let {
            if (it && typedValue.type == TypedValue.TYPE_REFERENCE) {
                res?.invoke(theme.obtainStyledAttributes(typedValue.data, attrs))
            }
        }
    }

    protected fun FragmentActivity.themeBy(@AttrRes attrStyle: Int, attrs: IntArray): TypedArray? {
        val typedValue = TypedValue()
        return theme.resolveAttribute(attrStyle, typedValue, false).let {
            if (it && typedValue.type == TypedValue.TYPE_REFERENCE) {
                theme.obtainStyledAttributes(typedValue.data, attrs)
            } else {
                null
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = if (context is OnDialogFragmentInteraction) context else null
    }

    @SuppressLint("ResourceType")
    final override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentDialogBaseCommonBinding.inflate(inflater).apply {
            onCreateContent(inflater)?.let {
                contentContainer.addView(it)
            }

            when (onConfigureButtons()) {
                ButtonType.DOUBLE -> {
                    btnCancel.visible = true
                    btnOk.visible = true
                    buttonGap.visible = true
                }
                ButtonType.SINGLE -> {
                    btnCancel.visible = true
                    btnOk.visible = false
                    buttonGap.visible = false
                }
                else -> {
                    btnCancel.visible = false
                    btnOk.visible = false
                    buttonTop.visible = false
                    buttonBottom.visible = false
                }
            }
            if (btnCancel.visible) {
                requireActivity().themeBy(R.attr.magicNegativeStyle, intArrayOf(android.R.attr.background, android.R.attr.textColor)) {
                    it.getDrawable(0)?.let { bg ->
                        btnCancel.background = bg
                    }
                    btnCancel.setTextColor(it.getColor(1, getColor(requireContext(), R.color.button_text_gray_variant)))
                }
            }
            if (btnOk.visible) {
                requireActivity().themeBy(R.attr.magicPositiveStyle, intArrayOf(android.R.attr.background, android.R.attr.textColor)) {
                    it.getDrawable(0)?.let { bg ->
                        btnOk.background = bg
                    }
                    btnOk.setTextColor(it.getColor(1, getColor(requireContext(), R.color.primary_white)))
                }
            }
            onConfigureNegative(btnCancel)
            onConfigurePositive(btnOk)
        }.root
    }

    override fun onCancelable(): Boolean = arguments?.getBoolean(ARG_CANCELLABLE, true) ?: true

    abstract fun onCreateContent(inflater: LayoutInflater): View?

    protected open fun onConfigureButtons(): ButtonType {
        return arguments?.getSerializable(ARG_BUTTON_TYPE) as? ButtonType ?: ButtonType.DOUBLE

    }

    /**
     * @return true to show the negative button, default true
     */
    protected open fun onConfigureNegative(v: TextView) {
        v.setOnClickListener {
            onNegativeClick(v)
            listenerExcluded?.onDialogNegativeClick(v) ?: listener?.onDialogNegativeClick(v)
            dismiss()
        }
        (arguments?.getSerializable(ARG_BUTTON_CONFIG) as? Config)?.let {
            v.text = it.cancel
        }
    }

    /**
     * @return true to show the positive button, default true
     */
    protected open fun onConfigurePositive(v: TextView) {
        v.setOnClickListener {
            onPositiveClick(v)
            listenerExcluded?.onDialogPositiveClick(v) ?: listener?.onDialogPositiveClick(v)
            dismiss()
        }
        (arguments?.getSerializable(ARG_BUTTON_CONFIG) as? Config)?.let {
            v.text = it.ok
        }
    }

    protected open fun onNegativeClick(v: TextView) {

    }

    protected open fun onPositiveClick(v: TextView) {

    }

    fun setOnDialogFragmentInteraction(listener: OnDialogFragmentInteraction): BaseCommonDialog {
        listenerExcluded = listener
        return this
    }


    abstract class Builder<T>(private val context: Context) {

        protected val arguments = Bundle()

        fun negative(button: String?): Builder<T> {
            arguments.putSerializable(ARG_BUTTON_CONFIG_NEGATIVE, Config.negative(context, button))
            return this
        }

        fun positive(button: String?): Builder<T> {
            arguments.putSerializable(ARG_BUTTON_CONFIG_POSITIVE, Config.positive(context, button))
            return this
        }

        fun type(buttonType: ButtonType): Builder<T> {
            arguments.putSerializable(ARG_BUTTON_TYPE, buttonType)
            return this
        }

        fun cancellable(cancellable: Boolean): Builder<T> {
            arguments.putBoolean(ARG_CANCELLABLE, cancellable)
            return this
        }

        protected open fun onPreBuild() {
            val config = Config.of(context)
            val neg = arguments.getSerializable(ARG_BUTTON_CONFIG_NEGATIVE) as? Config
            val pos = arguments.getSerializable(ARG_BUTTON_CONFIG_POSITIVE) as? Config
            arguments.putSerializable(
                ARG_BUTTON_CONFIG, Config(
                    neg?.cancel.takeIf { !it.isNullOrBlank() } ?: config.cancel,
                    pos?.ok.takeIf { !it.isNullOrBlank() } ?: config.ok
                ))
        }

        protected abstract fun create(): T

        fun build(): T {
            onPreBuild()
            return create()
        }

    }


    companion object {
        private const val ARG_BUTTON_CONFIG = "button_config"
        private const val ARG_BUTTON_CONFIG_NEGATIVE = "button_config_negative"
        private const val ARG_BUTTON_CONFIG_POSITIVE = "button_config_positive"
        private const val ARG_BUTTON_TYPE = "button_type"
        private const val ARG_CANCELLABLE = "cancellable"

        const val ARG_TITLE = "title"
    }

}