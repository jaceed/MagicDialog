package com.jaceed.android.magicdialog

/**
 * Created by Jacee.
 * Date: 2021.03.26
 */
enum class ButtonType(val flag: Int) {
    NONE(0),
    SINGLE_NEGATIVE(BUTTON_NEGATIVE),
    SINGLE_POSITIVE(BUTTON_POSITIVE),
    DOUBLE(BUTTON_NEGATIVE or BUTTON_POSITIVE),
    TRIPLE(BUTTON_NEGATIVE or BUTTON_NEUTRAL or BUTTON_POSITIVE)
}

internal const val BUTTON_NEGATIVE = 1 shl 2
internal const val BUTTON_NEUTRAL = 1 shl 1
internal const val BUTTON_POSITIVE = 1