package com.baicizhan.framework.common.magicdialog.utils

import android.content.res.Resources
import android.os.Bundle
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.fragment.app.DialogFragment

/**
 * Created by Jacee.
 * Date: 2021.04.23
 */

fun Bundle?.intResAvailable(key: String): Int? = if (this != null) getInt(key).takeIf { it != 0 } else null


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

internal fun Resources.Theme.colorOf(@AttrRes attr: Int, default: Int = 0) =
    if (resolveAttribute(attr, typedValue, true) && typedValue.type >= TypedValue.TYPE_FIRST_COLOR_INT && typedValue.type <= TypedValue.TYPE_LAST_COLOR_INT)
        typedValue.data else default

internal fun Resources.Theme.resourceOf(@AttrRes attr: Int, default: Int = 0) =
    if (resolveAttribute(attr, typedValue, true) && typedValue.type == TypedValue.TYPE_STRING) //todo layout居然是string?
        typedValue.resourceId else default

internal fun Resources.Theme.enumOf(@AttrRes attr: Int, default: Int = 0) =
    if (resolveAttribute(attr, typedValue, true) && typedValue.type >= TypedValue.TYPE_FIRST_INT && typedValue.type<= TypedValue.TYPE_LAST_INT)
        typedValue.data else default

internal fun DialogFragment.dimenOf(@AttrRes attr: Int, default: Int = 0) =
    requireDialog().context.theme.dimenOf(attr, default)

internal fun DialogFragment.colorOf(@AttrRes attr: Int, default: Int = 0) =
    requireDialog().context.theme.colorOf(attr, default)

internal fun DialogFragment.resourceOf(@AttrRes attr: Int, default: Int = 0) =
    requireDialog().context.theme.resourceOf(attr, default)

internal fun DialogFragment.enumOf(@AttrRes attr: Int, default: Int = 0) =
    requireDialog().context.theme.enumOf(attr, default)