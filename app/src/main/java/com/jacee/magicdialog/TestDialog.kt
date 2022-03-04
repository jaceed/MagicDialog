package com.jacee.magicdialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.baicizhan.framework.common.magicdialog.BaseCommonDialog
import com.jacee.magicdialog.databinding.FragmentDialogTestBinding

/**
 * Created by Jacee.
 * Date: 2022.03.02
 */
class TestDialog: BaseCommonDialog() {

    override fun onCreateContent(inflater: LayoutInflater): View? {
        return FragmentDialogTestBinding.inflate(inflater).root
    }

    class Builder(context: Context) : BaseCommonDialog.Builder<Builder, TestDialog>(context) {

        override fun create(): TestDialog {
            return TestDialog()
        }
    }

}