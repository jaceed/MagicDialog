package com.github.jaceed.magicdialog

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import com.github.jaceed.extender.view.content
import com.github.jaceed.magicdialog.databinding.FragmentDialogBaseOptionBinding

/**
 * Created by Jacee.
 * Date: 2021.07.15
 */
abstract class BaseOptionDialog: BaseCommonDialog() {

    override fun onLocation(): Int = Location.Bottom or Location.Full

    override fun onTheme(): Int = R.style.DialogThemeOptions

    final override fun onCreateContent(inflater: LayoutInflater): View? {
        return FragmentDialogBaseOptionBinding.inflate(inflater).apply {
            title.content = arguments?.getString(ARG_TITLE)?.takeIf { it.isNotBlank() }
            requireActivity().themeBy(R.attr.magicOptionStyle, R.styleable.MagicOptionChecked) { a ->
                a.getColor(R.styleable.MagicOptionChecked_magicOptionTitleColor, 0).takeIf { it != 0 }?.let { c -> title.setTextColor(c) }
                a.getDimension(R.styleable.MagicOptionChecked_magicOptionTitleSize, 0f).takeIf { it != 0f }?.let { d -> title.setTextSize(TypedValue.COMPLEX_UNIT_PX, d) }
                a.recycle()
            }

            onCreateOptionView(inflater)?.let {
                optionView.addView(it)
            }
        }.root
    }

    abstract fun onCreateOptionView(inflater: LayoutInflater): View?


    abstract class Builder<T>(context: Context) : BaseCommonDialog.Builder<T>(context) {

        fun title(title: String?): Builder<T> {
            arguments.putString(ARG_TITLE, title)
            return this
        }

    }

}