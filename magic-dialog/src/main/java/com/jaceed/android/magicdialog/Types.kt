package com.jaceed.android.magicdialog

/**
 * Created by Jacee.
 * Date: 2021.05.27
 */

enum class Location {
    CENTER,
    BOTTOM
}

enum class State {
    WRAP,
    EXPANDED,
    FULL
}

private const val LOCATION_MASK = 0xFF
private const val STATE_MASK = 0xFF00

internal infix fun Location.facade(s: State): Int = ordinal or (s.ordinal shl 8)
internal infix fun State.facade(l: Location): Int = (ordinal shl 8) or l.ordinal
internal fun Int.location(): Location = Location.values()[(this and LOCATION_MASK)]
internal fun Int.state(): State = State.values()[(this and STATE_MASK) shr 8]