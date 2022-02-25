package com.jacee.magicdialog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.baicizhan.framework.common.magicdialog.ButtonType
import com.baicizhan.framework.common.magicdialog.OptionListDialog
import com.baicizhan.framework.common.magicdialog.OnDialogFragmentInteraction
import com.baicizhan.framework.common.magicdialog.PromptDialog
import com.baicizhan.framework.common.magicdialog.utils.show
import com.jacee.magicdialog.databinding.ActivityCustomBinding

class CustomThemeActivity : AppCompatActivity(), OnDialogFragmentInteraction {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ActivityCustomBinding.inflate(layoutInflater).let {
            setContentView(it.root)
            it.custom.setOnClickListener {
                show(
                    PromptDialog.Builder(this)
                        .title("自定义")
                        .message("所有颜色都自定义").build(), "custom"
                )
            }
        }
    }

    override fun onDialogNegativeClick(v: View) {
        Toast.makeText(this, "取消", Toast.LENGTH_LONG).show()
    }

    override fun onDialogPositiveClick(v: View) {
        Toast.makeText(this, "确定", Toast.LENGTH_LONG).show()
        OptionListDialog.Builder(this)
            .options((15..60 step 15).map { "$it 分钟" }.toTypedArray(), 0)
            .title("定时关闭")
            .type(ButtonType.SINGLE_POSITIVE)
            .build()
            .show(supportFragmentManager, "checked")
    }
}