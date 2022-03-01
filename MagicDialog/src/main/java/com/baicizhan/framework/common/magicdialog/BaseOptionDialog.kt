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

    override fun onAnimation(): Int = R.style.OptionDialogAnimation

    final override fun onCreateContent(inflater: LayoutInflater): View? {
        return FragmentDialogBaseOptionBinding.inflate(inflater).apply {
            title.content = arguments?.getString(ARG_TITLE)?.takeIf { it.isNotBlank() }
            topSpacing.visible = !title.visible

            (themeData.titleColor.takeIf { it != 0 } ?: magicOnSurfaceColorDefault.takeIf { it != 0 })?.let {
                title.setTextColor(it)
            }
            themeData.titleSize.takeIf { it != 0 }?.let {
                title.setTextSize(TypedValue.COMPLEX_UNIT_PX, it.toFloat())
            }

            onCreateOptionView(inflater)?.let {
                optionView.addView(it)
            }
        }.root
    }

    override fun onStyle(): StyleParams? = StyleParams(
        R.attr.magicOptionStyle, R.styleable.OptionAppearance, intArrayOf(
            R.styleable.OptionAppearance_magicBackground,
            R.styleable.OptionAppearance_magicBackgroundMargin,
            R.styleable.OptionAppearance_magicBackgroundMarginBottom,
            R.styleable.OptionAppearance_magicTitleColor,
            R.styleable.OptionAppearance_magicTitleSize
        )
    )

    protected abstract fun onCreateOptionView(inflater: LayoutInflater): View?

}