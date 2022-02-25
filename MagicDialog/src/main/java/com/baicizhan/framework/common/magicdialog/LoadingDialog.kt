package com.baicizhan.framework.common.magicdialog

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.baicizhan.framework.common.magicdialog.utils.colorOf

/**
 * Created by Jacee.
 * Date: 2021.04.06
 */
class LoadingDialog: BaseDialog() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dialog_loading, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<ProgressBar>(R.id.progressBar).indeterminateTintList =
            ColorStateList.valueOf(colorOf(R.attr.colorPrimary, 0).takeIf { it != 0 } ?: return)
    }

}