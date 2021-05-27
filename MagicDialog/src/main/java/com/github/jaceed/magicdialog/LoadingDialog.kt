package com.github.jaceed.magicdialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by Jacee.
 * Date: 2021.04.06
 */
class LoadingDialog: BaseDialog() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dialog_loading, container, false)
    }

    override fun onLocation(): Location = Location.CENTER

}