package com.baicizhan.framework.common.magicdialog

import android.app.Dialog
import android.graphics.drawable.InsetDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import com.baicizhan.framework.common.magicdialog.utils.dimenOf
import com.baicizhan.framework.common.magicdialog.utils.styleOf


/**
 * Created by Jacee.
 * Date: 2019.02.14
 */
abstract class BaseDialog : BaseDialogFragment() {

    open val minWidthEnabled = false
    open val appearance = 0

    @Location
    open val location = CENTER

    @MatchState
    open val matchState = WRAP

    private val backgroundDrawable by lazy {
        ResourcesCompat.getDrawable(resources, R.drawable.bg_magic_round_all, requireDialog().context.theme)
    }

    private val backgroundDrawableBottom by lazy {
        ResourcesCompat.getDrawable(resources, R.drawable.bg_magic_round_top, requireDialog().context.theme)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).also { dialog ->
            dialog.context.theme.apply {
                styleOf(appearance.takeIf { it != 0 } ?: return@also) { attr ->
                    applyStyle(attr, true)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val window = dialog?.window ?: return
        val attrs = window.attributes

        attrs.gravity = if (location == BOTTOM) Gravity.BOTTOM else Gravity.CENTER
        attrs.height = ViewGroup.LayoutParams.WRAP_CONTENT

        val bg = when (matchState) {
            WRAP -> {
                attrs.width = with(dimenOf(R.attr.magicMinWidth, 0)) {
                    if (this > 0 && minWidthEnabled) this else ViewGroup.LayoutParams.WRAP_CONTENT
                }
                backgroundDrawable
            }
            EXPANDED -> {
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
                if (location == BOTTOM) backgroundDrawableBottom else backgroundDrawable
            }
        }
        window.attributes = attrs
        window.setBackgroundDrawable(bg)
    }

}