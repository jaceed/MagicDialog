package com.baicizhan.framework.common.magicdialog.utils

import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

/**
 * Created by Jacee.
 * Date: 2021.04.23
 */

private val typedValue by lazy {
    TypedValue()
}

internal fun FragmentActivity.colorBy(@AttrRes attr: Int, res: ((color: Int) -> Unit)? = null) {
    theme.resolveAttribute(attr, typedValue, true).let {
        if (it) {
            res?.invoke(typedValue.data)
        }
    }
}

internal fun FragmentActivity.colorOf(@AttrRes attr: Int, @ColorInt default: Int = 0): Int {
    return theme.resolveAttribute(attr, typedValue, true).let {
        if (it) typedValue.data else default
    }
}

internal fun FragmentActivity.colorOr(@AttrRes attr: Int, @AttrRes attrDefault: Int, @ColorInt elseColor: Int = 0): Int {
    return theme.resolveAttribute(attr, typedValue, true).let { res ->
        if (res) typedValue.data else run {
            theme.resolveAttribute(attrDefault, typedValue, true).let { other ->
                if (other) typedValue.data else elseColor
            }
        }
    }
}

internal fun Fragment.colorBy(@AttrRes attr: Int, res: ((color: Int) -> Unit)? = null) {
    requireActivity().colorBy(attr, res)
}

internal fun Fragment.colorOf(@AttrRes attr: Int, @ColorInt default: Int = 0): Int {
    return requireActivity().colorOf(attr, default)
}

internal fun Fragment.colorOr(@AttrRes attr: Int, @AttrRes attrDefault: Int, @ColorInt elseColor: Int = 0): Int {
    return requireActivity().colorOr(attr, attrDefault, elseColor)
}