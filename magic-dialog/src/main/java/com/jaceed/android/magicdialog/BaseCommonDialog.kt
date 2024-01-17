package com.jaceed.android.magicdialog

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.annotation.StyleRes
import androidx.constraintlayout.widget.ConstraintSet
import com.jaceed.android.magicdialog.R
import com.jaceed.android.magicdialog.databinding.FragmentDialogBaseCommonBinding
import com.jaceed.android.magicdialog.utils.colorOf
import com.jaceed.android.magicdialog.utils.intResAvailable
import com.jaceed.android.magicdialog.utils.resourceOf
import com.github.jaceed.extender.view.visible

/**
 *
 * Child classes have to inherit the Builder to create instances
 *
 * Created by Jacee.
 * Date: 2019.03.18
 */
abstract class BaseCommonDialog : BaseDialog() {

    private var listener: OnDialogFragmentInteraction? = null
    private var listenerExcluded: OnDialogFragmentInteraction? = null

    override val minWidthEnabled: Boolean = true

    private var buttonsLayout = R.layout.layout_action_buttons

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = if (context is OnDialogFragmentInteraction) context else null
    }

    override fun onAppearanceStyle(context: Context): Int? = arguments.intResAvailable(ARG_STYLE) ?: super.onAppearanceStyle(context)

    @SuppressLint("ResourceType")
    final override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentDialogBaseCommonBinding.inflate(inflater).apply {
            onCreateContent(inflater)?.let {
                magicContentContainer.addView(it)
            }

            val buttons = arguments.intResAvailable(ARG_BUTTON_LAYOUT) ?: resourceOf(R.attr.magicButtonsLayout, buttonsLayout)
            inflater.inflate(buttons, rootContainer, false).apply {
                val negativeButton by lazy { findViewById<TextView>(R.id.magic_button_negative) }
                val positiveButton by lazy { findViewById<TextView>(R.id.magic_button_positive) }
                val neutralButton by lazy { findViewById<TextView>(R.id.magic_button_neutral) }

                val top by lazy { findViewById<View>(R.id.magic_buttons_gap_top) }
                val bottom by lazy { findViewById<View>(R.id.magic_buttons_gap_bottom) }

                with(onConfigureButtons()) {
                    negativeButton.visible = flag and BUTTON_NEGATIVE == BUTTON_NEGATIVE
                    positiveButton.visible = flag and BUTTON_POSITIVE == BUTTON_POSITIVE
                    neutralButton?.visible = flag and BUTTON_NEUTRAL == BUTTON_NEUTRAL
                    top?.visible = flag != 0
                    bottom?.visible = flag != 0
                }
                onConfigureNegative(negativeButton)
                onConfigurePositive(positiveButton)
                neutralButton?.let { neu -> onConfigureNeutral(neu) }
            }.let {
                if (it.id == View.NO_ID) {
                    it.id = View.generateViewId()
                }
                rootContainer.addView(it)
                val constraintSet = ConstraintSet()
                constraintSet.clone(rootContainer)
                constraintSet.constrainWidth(it.id, 0)
                constraintSet.connect(it.id, ConstraintSet.TOP, magicContentContainer.id, ConstraintSet.BOTTOM)
                constraintSet.connect(it.id, ConstraintSet.START, rootContainer.id, ConstraintSet.START)
                constraintSet.connect(it.id, ConstraintSet.END, rootContainer.id, ConstraintSet.END)
                constraintSet.connect(it.id, ConstraintSet.BOTTOM, rootContainer.id, ConstraintSet.BOTTOM)
                constraintSet.applyTo(rootContainer)
            }
        }.root
    }

    override fun onCancelable(): Boolean = arguments?.getBoolean(ARG_CANCELLABLE, true) ?: true

    abstract fun onCreateContent(inflater: LayoutInflater): View?

    protected open fun onConfigureButtons(): ButtonType {
        return arguments?.getSerializable(ARG_BUTTON_TYPE) as? ButtonType ?: ButtonType.DOUBLE

    }

    protected open fun onConfigureNegative(v: TextView) {
        val config = (arguments?.getSerializable(ARG_BUTTON_CONFIG) as? Config)
        v.setOnClickListener {
            onNegativeClick(v)
            listenerExcluded?.onDialogNegativeClick(v) ?: config?.cancelCallback?.invoke(v) ?: listener?.onDialogNegativeClick(v)
            dismiss()
        }
        config?.let {
            v.text = it.cancel
        }
    }

    protected open fun onConfigurePositive(v: TextView) {
        val config = (arguments?.getSerializable(ARG_BUTTON_CONFIG) as? Config)
        v.setOnClickListener {
            onPositiveClick(v)
            listenerExcluded?.onDialogPositiveClick(v) ?: config?.okCallback?.invoke(v) ?: listener?.onDialogPositiveClick(v)
            dismiss()
        }
        config?.let {
            v.text = it.ok
            it.actionOk?.let { action ->
                colorOf(
                    when (action) {
                        Action.RECOMMENDED -> R.attr.magicButtonActionRecommendedColor
                        Action.ALERT -> R.attr.magicButtonActionAlertColor
                        else -> R.attr.magicButtonActionNormalColor
                    }, 0
                )
                    .takeIf { c ->
                        c != 0
                    }?.let { actionColor ->
                        v.setTextColor(actionColor)
                    }
            }
        }
    }

    protected open fun onConfigureNeutral(v: TextView) {
        val config = (arguments?.getSerializable(ARG_BUTTON_CONFIG) as? Config)
        v.setOnClickListener {
            onNeutralClick(v)
            listenerExcluded?.onDialogNeutralClick(v) ?: config?.neutralCallback?.invoke(v) ?: listener?.onDialogNeutralClick(v)
            dismiss()
        }
        config?.let {
            v.text = it.neutral
        }
    }

    protected open fun onNegativeClick(v: TextView) {

    }

    protected open fun onPositiveClick(v: TextView) {

    }

    protected open fun onNeutralClick(v: TextView) {

    }

    @Deprecated("Use setter in builder instead")
    fun setOnDialogFragmentInteraction(listener: OnDialogFragmentInteraction): BaseCommonDialog {
        listenerExcluded = listener
        return this
    }

    private fun setInteraction(listener: OnDialogFragmentInteraction?) {
        listenerExcluded = listener
    }


    abstract class Builder<T : Builder<T, R>, R: BaseCommonDialog>(protected val context: Context) {

        protected val arguments = Bundle()
        private var interaction: OnDialogFragmentInteraction? = null

        fun style(@StyleRes resId: Int): T {
            arguments.putInt(ARG_STYLE, resId)
            return this as T
        }

        fun button(@LayoutRes layoutRes: Int): T {
            arguments.putInt(ARG_BUTTON_LAYOUT, layoutRes)
            return this as T
        }

        fun cancellable(cancellable: Boolean): T {
            arguments.putBoolean(ARG_CANCELLABLE, cancellable)
            return this as T
        }

        @JvmOverloads
        fun negative(button: String? = null, callback: (View) -> Unit = {}): T {
            arguments.putSerializable(ARG_BUTTON_CONFIG_NEGATIVE, Config.negative(context, button, callback))
            return this as T
        }

        @JvmOverloads
        fun negative(@StringRes button: Int, callback: (View) -> Unit = {}): T  = negative(context.getString(button), callback)

        @JvmOverloads
        fun positive(button: String? = null, action: Action? = null, callback: (View) -> Unit = {}): T {
            arguments.putSerializable(ARG_BUTTON_CONFIG_POSITIVE, Config.positive(context, button, action, callback))
            return this as T
        }

        @JvmOverloads
        fun positive(@StringRes button: Int, action: Action? = null, callback: (View) -> Unit = {}): T  = positive(context.getString(button), action, callback)

        @JvmOverloads
        fun neutral(button: String?, callback: (View) -> Unit = {}): T {
            arguments.putSerializable(ARG_BUTTON_CONFIG_NEUTRAL, Config.neutral(context, button, callback))
            return this as T
        }

        @JvmOverloads
        fun neutral(@StringRes button: Int, callback: (View) -> Unit = {}): T  = neutral(context.getString(button), callback)

        /**
         * OnDialogFragmentInteraction's callback is prior to the individual callback of negative, positive or neutral buttons if both exist.
         * And if so, individual ones will be shadowed.
         */
        fun interaction(interaction: OnDialogFragmentInteraction): T {
            this.interaction = interaction
            return this as T
        }

        protected open fun onPreBuild() {
            val config = Config.of(context)
            val neg = arguments.getSerializable(ARG_BUTTON_CONFIG_NEGATIVE) as? Config
            val pos = arguments.getSerializable(ARG_BUTTON_CONFIG_POSITIVE) as? Config
            val neu = arguments.getSerializable(ARG_BUTTON_CONFIG_NEUTRAL) as? Config
            arguments.putSerializable(
                ARG_BUTTON_CONFIG, Config(
                    neg?.cancel.takeIf { !it.isNullOrBlank() } ?: config.cancel,
                    pos?.ok.takeIf { !it.isNullOrBlank() } ?: config.ok,
                    neu?.neutral.takeIf { !it.isNullOrBlank() },
                    actionOk = pos?.actionOk ?: Action.RECOMMENDED, // TODO only positive supported
                    cancelCallback = neg?.cancelCallback ?: {},
                    okCallback = pos?.okCallback ?: {},
                    neutralCallback = neu?.neutralCallback ?: {}
                ))
        }

        protected abstract fun create(): R

        fun build(): R {
            onPreBuild()
            return create().apply {
                this.arguments = this@Builder.arguments
                this.setInteraction(this@Builder.interaction ?: return@apply)
            }
        }

    }


    companion object {
        private const val ARG_BUTTON_CONFIG = "button_config"
        private const val ARG_BUTTON_LAYOUT = "button_layout"
        private const val ARG_BUTTON_CONFIG_NEGATIVE = "button_config_negative"
        private const val ARG_BUTTON_CONFIG_POSITIVE = "button_config_positive"
        private const val ARG_BUTTON_CONFIG_NEUTRAL = "button_config_neutral"
        private const val ARG_CANCELLABLE = "cancellable"
        private const val ARG_STYLE = "style"

        const val ARG_BUTTON_TYPE = "button_type"
    }

}