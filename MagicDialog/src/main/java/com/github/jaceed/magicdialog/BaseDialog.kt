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

    enum class Location {
        CENTER,
        CENTER_EXPANDED,
        BOTTOM,
        BOTTOM_FULL
    }

    private val insetSize: Int by lazy {
        resources.getDimensionPixelSize(R.dimen.dialog_content_border_margin)
    }

    protected val magicBackgroundColorDefault by lazy {
        requireActivity().colorOf(R.attr.magicBackground, ResourcesCompat.getColor(resources, R.color.magicBackground, null))
    }

    private val backgroundDrawable by lazy {
        ResourcesCompat.getDrawable(resources, R.drawable.bg_inset_common_dialog, null)?.apply {
            setTint(magicBackgroundColorDefault)
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setBackgroundDrawable(backgroundDrawable)
        when (onLocation()) {
            Location.BOTTOM -> {
                val params = dialog?.window?.attributes
                dialog?.window?.attributes = params?.also {
                    it.gravity = Gravity.BOTTOM
                    it.width = ViewGroup.LayoutParams.MATCH_PARENT
                    it.height = ViewGroup.LayoutParams.WRAP_CONTENT
                }
            }
            Location.BOTTOM_FULL -> {
                val params = dialog?.window?.attributes
                dialog?.window?.attributes = params?.also {
                    it.gravity = Gravity.BOTTOM
                    it.width = ViewGroup.LayoutParams.MATCH_PARENT
                    it.height = ViewGroup.LayoutParams.WRAP_CONTENT
                }
            }
            Location.CENTER_EXPANDED -> {
                // Center alert type, force to not cancelable
                isCancelable = false

                val params = dialog?.window?.attributes
                dialog?.window?.attributes = params?.also {
                    it.width = ViewGroup.LayoutParams.MATCH_PARENT
                    it.height = ViewGroup.LayoutParams.WRAP_CONTENT
                }
            }
            Location.CENTER -> {
                // Center alert type, force to not cancelable
                isCancelable = false
            }
        }
    }

    protected open fun onLocation(): Location = Location.CENTER_EXPANDED

}