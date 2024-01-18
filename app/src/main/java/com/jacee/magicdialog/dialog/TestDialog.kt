package com.jacee.magicdialog.dialog

import android.content.Context
import android.os.Bundle
import com.jacee.magicdialog.R
import com.jaceed.android.magicdialog.FoundationDialog

/**
 * Created by Jacee.
 * Date: 2024.01.18
 */
class TestDialog(context: Context, themeResId: Int = R.style.MagicDefault) : FoundationDialog(context, themeResId) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_test_classic)
    }

}