package com.github.jaceed.magicdialog

import android.view.Gravity
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import com.github.jaceed.magicdialog.utils.colorOf

/**
 * Created by Jacee.
 * Date: 2019.02.14
 */
abstract class BaseDialog : BaseDialogFragment() {

    private val insetSize: Int by lazy {
        resources.getDimensionPixelSize(R.dimen.dialog_content_border_margin)
    }

    protected val magicBackgroundColorDefault by lazy {
        colorOf(R.attr.magicBackground, ResourcesCompat.getColor(resources, R.color.magicBackground, null))
    }

    private val backgroundDrawable by lazy {
        ResourcesCompat.getDrawable(resources, R.drawable.bg_inset_common_dialog, null)?.apply {
            setTint(magicBackgroundColorDefault)
        }
    }

    private val backgroundDrawableFull by lazy {
        ResourcesCompat.getDrawable(resources, R.drawable.bg_common_dialog, null)?.apply {
            setTint(magicBackgroundColorDefault)
        }
    }

    override fun onStart() {
        super.onStart()
        val window = dialog?.window ?: return
        val attrs = window.attributes
        val location = onLocation()

        var bg = backgroundDrawable
        var w = ViewGroup.LayoutParams.WRAP_CONTENT
        if (location and Location.Expanded == Location.Expanded) {
            w = ViewGroup.LayoutParams.MATCH_PARENT
        } else if (location and Location.Full == Location.Full) {
            w = ViewGroup.LayoutParams.MATCH_PARENT
            bg = backgroundDrawableFull
        }

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