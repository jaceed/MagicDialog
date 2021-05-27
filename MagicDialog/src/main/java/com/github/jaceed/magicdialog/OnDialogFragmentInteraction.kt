package com.github.jaceed.magicdialog

import android.view.View

interface OnDialogFragmentInteraction {
    fun onDialogNegativeClick(v: View)
    fun onDialogPositiveClick(v: View)
}