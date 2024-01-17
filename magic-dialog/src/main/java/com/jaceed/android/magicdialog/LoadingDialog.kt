package com.jaceed.android.magicdialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jaceed.android.magicdialog.R

/**
 * Created by Jacee.
 * Date: 2021.04.06
 */
class LoadingDialog: BaseDialog() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dialog_loading, container, false)
    }

}