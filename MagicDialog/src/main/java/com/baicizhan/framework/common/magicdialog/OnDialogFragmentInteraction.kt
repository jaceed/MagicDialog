package com.baicizhan.framework.common.magicdialog

import android.view.View

interface OnDialogFragmentInteraction {
    fun onDialogNegativeClick(v: View)
    fun onDialogPositiveClick(v: View)
    fun onDialogNeutralClick(v: View) {}
}