package com.baicizhan.framework.common.magicdialog.utils

import android.content.res.Resources
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.fragment.app.DialogFragment

/**
 * Created by Jacee.
 * Date: 2021.04.23
 */

private val typedValue by lazy {
    TypedValue()
}

internal fun Resources.Theme.dimenOf(@AttrRes attr: Int, default: Int = 0) =
    if (resolveAttribute(attr, typedValue, true) && typedValue.type == TypedValue.TYPE_DIMENSION)
        typedValue.getDimension(resources.displayMetrics).toInt() else default

internal fun Resources.Theme.styleOf(@AttrRes attr: Int): Int? {
    return (if (resolveAttribute(attr, typedValue, true) && typedValue.type == TypedValue.TYPE_REFERENCE) typedValue.data else 0).takeIf { it != 0 }
}

internal fun Resources.Theme.styleOf(@AttrRes attr: Int, result: (Int) -> Unit) {
    result(styleOf(attr) ?: return)
}

internal fun Resources.Theme.styleOf(@AttrRes attr: Int, @AttrRes attrDefault: Int, result: (Int) -> Unit) {
    result(styleOf(attr) ?: styleOf(attrDefault) ?: return)
}

internal fun DialogFragment.dimenOf(@AttrRes attr: Int, default: Int = 0) =
    requireDialog().context.theme.dimenOf(attr, default)