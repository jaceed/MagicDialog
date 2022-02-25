package com.baicizhan.framework.common.magicdialog

import androidx.annotation.IntDef

/**
 * Created by Jacee.
 * Date: 2021.05.27
 */

@IntDef(CENTER, BOTTOM)
@Retention(AnnotationRetention.SOURCE)
annotation class Location

const val CENTER = 0
const val BOTTOM = 1

@IntDef(WRAP, EXPANDED, FULL)
@Retention(AnnotationRetention.SOURCE)
annotation class MatchState

const val WRAP = 0
const val EXPANDED = 1
const val FULL = 1 shl 1

