package com.github.jaceed.magicdialog

/**
 * Created by Jacee.
 * Date: 2021.05.27
 */
object Location { // TODO 这个要改，位置和inset混一起了
    const val Center = 1
    const val Bottom = 1 shl 1
    const val Expanded = 1 shl 2
    const val Full = 1 shl 3

    internal const val MATCH_MASK = Expanded or Full
}
