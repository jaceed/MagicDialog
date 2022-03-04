package com.baicizhan.framework.common.magicdialog

import android.view.LayoutInflater
import android.view.View
import com.baicizhan.framework.common.magicdialog.databinding.FragmentDialogBaseOptionBinding
import com.github.jaceed.extender.view.content
import com.github.jaceed.extender.view.visible

/**
 * Created by Jacee.
 * Date: 2021.07.15
 */
abstract class BaseOptionDialog: BaseContentDialog() {

    override val themeRes: Int = R.style.MagicDefault_Options
    override val appearanceAttribute = R.attr.magicOptionsAppearance
    override val location: Int = BOTTOM
    override val matchState: Int = FULL
    override val animationRes: Int = R.style.OptionDialogAnimation

    final override fun onCreateContent(inflater: LayoutInflater): View? {
        return FragmentDialogBaseOptionBinding.inflate(inflater).apply {
            title.content = arguments?.getString(ARG_TITLE)?.takeIf { it.isNotBlank() }
            topSpacing.visible = !title.visible
            onCreateOptionView(inflater)?.let {
                optionView.addView(it)
            }
        }.root
    }

    protected abstract fun onCreateOptionView(inflater: LayoutInflater): View?

}