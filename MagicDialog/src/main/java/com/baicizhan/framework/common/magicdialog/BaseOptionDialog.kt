package com.baicizhan.framework.common.magicdialog

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import com.baicizhan.framework.common.magicdialog.databinding.FragmentDialogBaseOptionBinding
import com.github.jaceed.extender.view.content
import com.github.jaceed.extender.view.visible

/**
 * Created by Jacee.
 * Date: 2021.07.15
 */
abstract class BaseOptionDialog: BaseCommonDialog() {

    override fun onLocation(): Int = BOTTOM

    override fun onMatchState(): Int = FULL

    override fun onTheme(): Int = R.style.DialogThemeCommon_Options

    override fun onAnimation(): Int = R.style.OptionDialogAnimation

    final override fun onCreateContent(inflater: LayoutInflater): View? {
        return FragmentDialogBaseOptionBinding.inflate(inflater).apply {
            title.content = arguments?.getString(ARG_TITLE)?.takeIf { it.isNotBlank() }
            topSpacing.visible = !title.visible
            requireActivity().themeBy(R.attr.magicOptionStyle, R.styleable.MagicOptions) { a ->
                a.getColor(R.styleable.MagicOptions_magicOptionTitleColor, 0).takeIf { it != 0 }?.let { c -> title.setTextColor(c) }
                a.getDimension(R.styleable.MagicOptions_magicOptionTitleSize, 0f).takeIf { it != 0f }?.let { d -> title.setTextSize(TypedValue.COMPLEX_UNIT_PX, d) }
                a.recycle()
            }
            onCreateOptionView(inflater)?.let {
                optionView.addView(it)
            }
        }.root
    }

    protected abstract fun onCreateOptionView(inflater: LayoutInflater): View?

}