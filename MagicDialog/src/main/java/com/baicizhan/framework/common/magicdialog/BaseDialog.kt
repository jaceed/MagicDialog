package com.baicizhan.framework.common.magicdialog

import android.view.Gravity
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import com.baicizhan.framework.common.magicdialog.utils.colorOr

/**
 * Created by Jacee.
 * Date: 2019.02.14
 */
abstract class BaseDialog : BaseDialogFragment() {

    private val insetSize: Int by lazy {
        resources.getDimensionPixelSize(R.dimen.dialog_content_border_margin)
    }

    private val magicBackgroundColorDefault by lazy {
        colorOr(R.attr.magicBackground, R.attr.colorSurface, ResourcesCompat.getColor(resources, R.color.magicBackground, null))
    }

    private val backgroundDrawable by lazy {
        ResourcesCompat.getDrawable(resources, R.drawable.bg_magic_inset_common_dialog, null)?.apply {
            setTint(magicBackgroundColorDefault)
        }
    }

    private val backgroundDrawableExpanded by lazy {
        ResourcesCompat.getDrawable(resources, R.drawable.bg_magic_common_dialog, null)?.apply {
            setTint(magicBackgroundColorDefault)
        }
    }

    override fun onStart() {
        super.onStart()
        val window = dialog?.window ?: return
        val attrs = window.attributes
        attrs.gravity = if (onLocation() == BOTTOM) Gravity.BOTTOM else Gravity.CENTER
        attrs.height = ViewGroup.LayoutParams.WRAP_CONTENT

        val bg = when (onMatchState()) {
            WRAP -> {
                attrs.width = ViewGroup.LayoutParams.WRAP_CONTENT
                backgroundDrawable
            }
            EXPANDED -> {
                attrs.width = ViewGroup.LayoutParams.MATCH_PARENT
                backgroundDrawable
            }
            else -> {
                attrs.width = ViewGroup.LayoutParams.MATCH_PARENT
                backgroundDrawableExpanded
            }
        }
        window.attributes = attrs
        window.setBackgroundDrawable(bg)
    }

    @Location
    protected open fun onLocation(): Int = CENTER

    @MatchState
    protected open fun onMatchState(): Int = WRAP

}