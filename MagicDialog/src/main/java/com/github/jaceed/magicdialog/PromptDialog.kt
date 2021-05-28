package com.github.jaceed.magicdialog

import android.content.Context
import android.os.Bundle
import android.widget.TextView

/**
 * Created by Jacee.
 * Date: 2021.03.26
 */
class PromptDialog : BaseCommonDialog() {

    override fun onStart() {
        super.onStart()
        isCancelable = (arguments?.getSerializable(ARG_CANCELLABLE) as? Boolean) ?: false
    }

    override fun onConfigureTitle(v: TextView): Boolean {
        v.text = arguments?.getString(ARG_TITLE)?.takeIf { it.isNotBlank() } ?: return false
        return true
    }

    override fun onConfigureMessage(v: TextView): Boolean {
        v.text = arguments?.getString(ARG_MESSAGE)?.takeIf { it.isNotBlank() } ?: return false
        return true
    }

    override fun onConfigureButtons(): ButtonType {
        return arguments?.getSerializable(ARG_BUTTON_TYPE) as? ButtonType ?: ButtonType.DOUBLE
    }

    override fun onConfigureNegative(v: TextView): Action {
        val action = super.onConfigureNegative(v)
        return (arguments?.getSerializable(ARG_BUTTON_CONFIG) as? Config)?.let {
            v.text = it.cancel
            it.cancelAction
        } ?: action
    }

    override fun onConfigurePositive(v: TextView): Action {
        val action = super.onConfigurePositive(v)
        return (arguments?.getSerializable(ARG_BUTTON_CONFIG) as? Config)?.let {
            v.text = it.ok
            it.okAction
        } ?: action
    }

    override fun onLocation(): Int {
        val sp = super.onLocation()
        return arguments?.getInt(ARG_LOCATION, sp) ?: sp
    }

    class Builder(private val context: Context) {

        private val arguments = Bundle()

        fun title(title: String?): Builder {
            arguments.putString(ARG_TITLE, title)
            return this
        }

        fun message(message: String?): Builder {
            arguments.putString(ARG_MESSAGE, message)
            return this
        }

        fun negative(button: String?, action: Action = Action.NORMAL): Builder {
            arguments.putSerializable(ARG_BUTTON_CONFIG_NEGATIVE, Config.negative(context, button, action))
            return this
        }

        fun positive(button: String?, action: Action = Action.ALERT): Builder {
            arguments.putSerializable(ARG_BUTTON_CONFIG_POSITIVE, Config.positive(context, button, action))
            return this
        }

        fun type(buttonType: ButtonType): Builder {
            arguments.putSerializable(ARG_BUTTON_TYPE, buttonType)
            return this
        }

        fun cancellable(cancellable: Boolean): Builder {
            arguments.putSerializable(ARG_CANCELLABLE, cancellable)
            return this
        }

        fun location(location: Int): Builder {
            if (location and Location.Expanded == 0 && location and Location.Full == 0) {
                throw RuntimeException("Prompt dialog has to be expanded or full")
            }
            arguments.putInt(ARG_LOCATION, location)
            return this
        }

        fun build(): PromptDialog {
            val config = (arguments.getSerializable(ARG_BUTTON_CONFIG) as? Config) ?: Config.of(context)
            val neg = arguments.getSerializable(ARG_BUTTON_CONFIG_NEGATIVE) as? Config
            val pos = arguments.getSerializable(ARG_BUTTON_CONFIG_POSITIVE) as? Config
            arguments.putSerializable(ARG_BUTTON_CONFIG, Config(
                neg?.cancel.takeIf { !it.isNullOrBlank() } ?: config.cancel,
                pos?.ok.takeIf { !it.isNullOrBlank() } ?: config.ok,
                neg?.cancelAction ?: config.cancelAction,
                pos?.okAction ?: config.okAction
            ))
            return newDialog(arguments)
        }

    }

    companion object {

        private const val ARG_TITLE = "title"
        private const val ARG_MESSAGE = "message"
        private const val ARG_BUTTON_CONFIG = "button_config"
        private const val ARG_BUTTON_CONFIG_NEGATIVE = "button_config_negative"
        private const val ARG_BUTTON_CONFIG_POSITIVE = "button_config_positive"
        private const val ARG_BUTTON_TYPE = "button_type"
        private const val ARG_CANCELLABLE = "cancellable"
        private const val ARG_LOCATION = "location"

        private fun newDialog(args: Bundle) = PromptDialog().apply {
            arguments = args
        }

    }


}