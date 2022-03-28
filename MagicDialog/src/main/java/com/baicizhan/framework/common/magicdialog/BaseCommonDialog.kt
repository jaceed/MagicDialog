package com.baicizhan.framework.common.magicdialog

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.annotation.StyleRes
import androidx.constraintlayout.widget.ConstraintSet
import com.baicizhan.framework.common.magicdialog.databinding.FragmentDialogBaseCommonBinding
import com.baicizhan.framework.common.magicdialog.utils.colorOf
import com.baicizhan.framework.common.magicdialog.utils.intResAvailable
import com.baicizhan.framework.common.magicdialog.utils.resourceOf
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

                val top by lazy { findViewById<View>(R.id.magic_buttons_gap_top) }
                val bottom by lazy { findViewById<View>(R.id.magic_buttons_gap_bottom) }
                val between by lazy { findViewById<View>(R.id.magic_buttons_gap_between) }

                when (onConfigureButtons()) {
                    ButtonType.DOUBLE -> {
                        negativeButton.visible = true
                        positiveButton.visible = true
                        between?.visible = true
                    }
                    ButtonType.SINGLE_NEGATIVE -> {
                        negativeButton.visible = true
                        positiveButton.visible = false
                        between?.visible = false
                    }
                    ButtonType.SINGLE_POSITIVE -> {
                        negativeButton.visible = false
                        positiveButton.visible = true
                        between?.visible = false
                    }
                    else -> {
                        negativeButton.visible = false
                        positiveButton.visible = false
                        top?.visible = false
                        bottom?.visible = false
                    }
                }
                onConfigureNegative(negativeButton)
                onConfigurePositive(positiveButton)
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

    protected open fun onNegativeClick(v: TextView) {

    }

    protected open fun onPositiveClick(v: TextView) {

    }

    fun setOnDialogFragmentInteraction(listener: OnDialogFragmentInteraction): BaseCommonDialog {
        listenerExcluded = listener
        return this
    }


    abstract class Builder<T : Builder<T, R>, R: BaseCommonDialog>(protected val context: Context) {

        protected val arguments = Bundle()

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

        fun negative(button: String?): T {
            arguments.putSerializable(ARG_BUTTON_CONFIG_NEGATIVE, Config.negative(context, button))
            return this as T
        }

        @JvmOverloads
        fun positive(button: String? = null, action: Action? = null): T {
            arguments.putSerializable(ARG_BUTTON_CONFIG_POSITIVE, Config.positive(context, button, action))
            return this as T
        }

        protected open fun onPreBuild() {
            val config = Config.of(context)
            val neg = arguments.getSerializable(ARG_BUTTON_CONFIG_NEGATIVE) as? Config
            val pos = arguments.getSerializable(ARG_BUTTON_CONFIG_POSITIVE) as? Config
            arguments.putSerializable(
                ARG_BUTTON_CONFIG, Config(
                    neg?.cancel.takeIf { !it.isNullOrBlank() } ?: config.cancel,
                    pos?.ok.takeIf { !it.isNullOrBlank() } ?: config.ok,
                    actionOk = pos?.actionOk ?: Action.RECOMMENDED // TODO only positive supported
                ))
        }

        protected abstract fun create(): R

        fun build(): R {
            onPreBuild()
            return create().apply {
                this.arguments = this@Builder.arguments
            }
        }

    }


    companion object {
        private const val ARG_BUTTON_CONFIG = "button_config"
        private const val ARG_BUTTON_LAYOUT = "button_layout"
        private const val ARG_BUTTON_CONFIG_NEGATIVE = "button_config_negative"
        private const val ARG_BUTTON_CONFIG_POSITIVE = "button_config_positive"
        private const val ARG_CANCELLABLE = "cancellable"
        private const val ARG_STYLE = "style"

        const val ARG_BUTTON_TYPE = "button_type"
    }

}