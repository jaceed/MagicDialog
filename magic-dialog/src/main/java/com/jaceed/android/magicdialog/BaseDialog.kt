package com.jaceed.android.magicdialog

import android.app.Dialog
import android.content.Context
import android.graphics.Point
import android.graphics.drawable.InsetDrawable
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import androidx.annotation.AttrRes
import androidx.core.content.res.ResourcesCompat
import com.jaceed.android.magicdialog.R
import com.jaceed.android.magicdialog.utils.dimenOf
import com.jaceed.android.magicdialog.utils.styleOf


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

    protected open fun onAppearanceStyle(context: Context): Int? =
        appearanceAttribute.takeIf { it != 0 }?.let {
            context.theme?.styleOf(it)
        }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            context.theme.applyStyle(onAppearanceStyle(context) ?: return@apply, true)
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
                attrs.width = calculateWidth()
                val margin = dimenOf(R.attr.magicBackgroundMargin)
                val marginBottom = dimenOf(R.attr.magicBackgroundMarginBottom)
                if (margin == 0 && marginBottom == 0 || attrs.width > 0) {
                    backgroundDrawable
                } else {
                    InsetDrawable(backgroundDrawable, margin, 0, margin, marginBottom)
                }
            }
            else -> {
                attrs.width = calculateWidth()
                if (location == Location.BOTTOM) backgroundDrawableBottom else backgroundDrawable
            }
        }
        window.attributes = attrs
        window.setBackgroundDrawable(bg)
    }


    private fun calculateWidth(): Int {
        val screen = getScreenSize()
        return with(dimenOf(R.attr.magicMaxWidth, 0)) {
            if (this > 0 && this < screen.x) this else ViewGroup.LayoutParams.MATCH_PARENT
        }
    }

    private fun getScreenSize() = Point().apply {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            context?.display?.getRealSize(this)
        } else {
            dialog?.window?.windowManager?.defaultDisplay?.getRealSize(this)
        }
    }

}