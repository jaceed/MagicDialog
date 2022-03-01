package com.baicizhan.framework.common.magicdialog

import android.content.res.TypedArray
import android.graphics.drawable.InsetDrawable
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import androidx.annotation.AttrRes
import androidx.annotation.StyleableRes
import androidx.core.content.res.ResourcesCompat
import com.baicizhan.framework.common.magicdialog.utils.colorOf


/**
 * Created by Jacee.
 * Date: 2019.02.14
 */
abstract class BaseDialog : BaseDialogFragment() {

    protected data class Theme(
        var backgroundColor: Int = 0,
        var backgroundMargin: Int = 0,
        var backgroundMarginBottom: Int = 0,
        var titleColor: Int = 0,
        var titleSize: Int = 0
    )
    
    protected fun themeOf(@AttrRes attrId: Int, @StyleableRes attrs: IntArray, result: (TypedArray) -> Unit) {
        val type = TypedValue()
        fun update(a: TypedArray) {
            result(a)
            a.recycle()
        }
        requireDialog().context.theme.obtainStyledAttributes(attrs).let {
            update(it)
        }
        requireDialog().context.theme.resolveAttribute(attrId, type, true).takeIf { it }?.let {
            if (type.type == TypedValue.TYPE_REFERENCE) {
                requireDialog().context.theme.obtainStyledAttributes(type.data, attrs).let {
                    update(it)
                }
            }
        }
        requireActivity().theme.obtainStyledAttributes(attrs).let {
            update(it)
        }
        requireActivity().theme.resolveAttribute(attrId, type, true).takeIf { it }?.let {
            if (type.type == TypedValue.TYPE_REFERENCE) {
                requireDialog().context.theme.obtainStyledAttributes(type.data, attrs).let {
                    update(it)
                }
            }
        }
    }

    protected val themeData by lazy {
        Theme().apply {
            val (attrId, attrs, attrRes) = onStyle() ?: return@apply
            check(attrRes.size == 5)
            themeOf(attrId, attrs) { a ->
                a.getColor(attrRes[0], 0).takeIf { it != 0 }?.let {
                    backgroundColor = it
                }
                a.getDimensionPixelSize(attrRes[1], 0).takeIf { it != 0 }?.let {
                    backgroundMargin = it
                }
                a.getDimensionPixelSize(attrRes[2], 0).takeIf { it != 0 }?.let {
                    backgroundMarginBottom = it
                }
                a.getColor(attrRes[3], 0).takeIf { it != 0 }?.let {
                    titleColor = it
                }
                a.getDimensionPixelSize(attrRes[4], 0).takeIf { it != 0 }?.let {
                    titleSize = it
                }
            }
        }
    }

    private val magicBackgroundColorDefault by lazy {
        themeData.backgroundColor.takeIf { it != 0 } ?: colorOf(R.attr.colorSurface, 0)
    }

    protected val magicOnSurfaceColorDefault by lazy {
        colorOf(R.attr.colorOnSurface, 0)
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

        val bg = if (onLocation() == BOTTOM && onMatchState() != WRAP && themeData.backgroundMargin == 0 && themeData.backgroundMarginBottom == 0)
            backgroundDrawableBottom else InsetDrawable(
            backgroundDrawable, themeData.backgroundMargin, 0, themeData.backgroundMargin, themeData.backgroundMarginBottom
        )
        window.setBackgroundDrawable(bg)
    }

    @Location
    protected open fun onLocation(): Int = CENTER

    @MatchState
    protected open fun onMatchState(): Int = WRAP

    protected open fun onStyle(): StyleParams? = null

}