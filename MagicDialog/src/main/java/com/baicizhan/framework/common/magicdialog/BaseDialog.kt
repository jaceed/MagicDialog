package com.baicizhan.framework.common.magicdialog

import android.app.Dialog
import android.graphics.drawable.InsetDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import androidx.annotation.AttrRes
import androidx.core.content.res.ResourcesCompat
import com.baicizhan.framework.common.magicdialog.utils.dimenOf
import com.baicizhan.framework.common.magicdialog.utils.styleOf


/**
 * Created by Jacee.
 * Date: 2019.02.14
 */
abstract class BaseDialog : BaseDialogFragment() {

    protected open val minWidthEnabled = false
    @AttrRes
    protected open val appearanceAttribute = 0

    protected open val facade = Location.CENTER facade State.WRAP

    private val backgroundDrawable by lazy {
        ResourcesCompat.getDrawable(resources, R.drawable.bg_magic_round_all, requireDialog().context.theme)
    }

    private val backgroundDrawableBottom by lazy {
        ResourcesCompat.getDrawable(resources, R.drawable.bg_magic_round_top, requireDialog().context.theme)
    }

    protected open fun onAppearanceStyle(): Int? =
        appearanceAttribute.takeIf { it != 0 }?.let {
            dialog?.context?.theme?.styleOf(it)
        }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).also { dialog ->
            dialog.context.theme.applyStyle(onAppearanceStyle() ?: return@also, true)
        }
    }

    override fun onStart() {
        super.onStart()
        val window = dialog?.window ?: return
        val attrs = window.attributes
        val location = facade.location()
        val state = facade.state()
        attrs.gravity = if (location == Location.BOTTOM) Gravity.BOTTOM else Gravity.CENTER
        attrs.height = ViewGroup.LayoutParams.WRAP_CONTENT

        val bg = when (state) {
            State.WRAP -> {
                attrs.width = with(dimenOf(R.attr.magicMinWidth, 0)) {
                    if (this > 0 && minWidthEnabled) this else ViewGroup.LayoutParams.WRAP_CONTENT
                }
                backgroundDrawable
            }
            State.EXPANDED -> {
                attrs.width = ViewGroup.LayoutParams.MATCH_PARENT
                val margin = dimenOf(R.attr.magicBackgroundMargin)
                val marginBottom = dimenOf(R.attr.magicBackgroundMarginBottom)
                if (margin == 0 && marginBottom == 0)
                    backgroundDrawable
                else
                    InsetDrawable(backgroundDrawable, margin, 0, margin, marginBottom)
            }
            else -> {
                attrs.width = ViewGroup.LayoutParams.MATCH_PARENT
                if (location == Location.BOTTOM) backgroundDrawableBottom else backgroundDrawable
            }
        }
        window.attributes = attrs
        window.setBackgroundDrawable(bg)
    }

}