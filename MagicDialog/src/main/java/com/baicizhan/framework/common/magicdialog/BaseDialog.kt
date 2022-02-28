package com.baicizhan.framework.common.magicdialog

import android.graphics.drawable.InsetDrawable
import android.view.Gravity
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import com.baicizhan.framework.common.magicdialog.utils.colorOf
import com.baicizhan.framework.common.magicdialog.utils.colorOr
import com.baicizhan.framework.common.magicdialog.utils.dimenOf

/**
 * Created by Jacee.
 * Date: 2019.02.14
 */
abstract class BaseDialog : BaseDialogFragment() {

    private val magicBackgroundColorDefault by lazy {
        colorOr(R.attr.magicBackground, R.attr.colorSurface, 0)
    }

    protected val magicOnSurfaceColorDefault by lazy {
        colorOf(R.attr.colorOnSurface, 0)
    }

    private val insets by lazy {
        Pair(
            dimenOf(R.attr.magicBackgroundMargin),
            dimenOf(R.attr.magicBackgroundMarginBottom)
        )
    }

    private val backgroundDrawable by lazy {
        ResourcesCompat.getDrawable(resources, R.drawable.bg_magic_round_all, null)?.apply {
            setTint(magicBackgroundColorDefault.takeIf { it != 0 } ?: return@apply)
        }
    }

    private val backgroundDrawableBottom by lazy {
        ResourcesCompat.getDrawable(resources, R.drawable.bg_magic_round_top, null)?.apply {
            setTint(magicBackgroundColorDefault.takeIf { it != 0 } ?: return@apply)
        }
    }

    override fun onStart() {
        super.onStart()
        val window = dialog?.window ?: return
        val attrs = window.attributes
        attrs.gravity = if (onLocation() == BOTTOM) Gravity.BOTTOM else Gravity.CENTER
        attrs.height = ViewGroup.LayoutParams.WRAP_CONTENT

        when (onMatchState()) {
            WRAP -> {
                attrs.width = ViewGroup.LayoutParams.WRAP_CONTENT
            }
            EXPANDED -> {
                attrs.width = ViewGroup.LayoutParams.MATCH_PARENT
            }
            else -> {
                attrs.width = ViewGroup.LayoutParams.MATCH_PARENT
            }
        }
        window.attributes = attrs

        val bg = if (onLocation() == BOTTOM && onMatchState() != WRAP && insets.first == 0 && insets.second == 0)
            backgroundDrawableBottom else InsetDrawable(
            backgroundDrawable, insets.first, 0, insets.first, insets.second - 2
        )
        window.setBackgroundDrawable(bg)
    }

    @Location
    protected open fun onLocation(): Int = CENTER

    @MatchState
    protected open fun onMatchState(): Int = WRAP

}