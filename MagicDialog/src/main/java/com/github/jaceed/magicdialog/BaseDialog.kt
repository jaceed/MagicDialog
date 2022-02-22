package com.github.jaceed.magicdialog

import android.view.Gravity
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import com.github.jaceed.magicdialog.utils.colorOr

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
        val location = onLocation()

        val w = if (location and Location.MATCH_MASK != 0) ViewGroup.LayoutParams.MATCH_PARENT else ViewGroup.LayoutParams.WRAP_CONTENT
        val bg = if (location and Location.Full == Location.Full) backgroundDrawableExpanded else backgroundDrawable

        if (location and Location.Bottom == Location.Bottom) {
            attrs.gravity = Gravity.BOTTOM
        }

        attrs.width = w
        attrs.height = ViewGroup.LayoutParams.WRAP_CONTENT
        window.attributes = attrs
        window.setBackgroundDrawable(bg)
    }

    protected open fun onLocation(): Int = Location.Center or Location.Expanded

}