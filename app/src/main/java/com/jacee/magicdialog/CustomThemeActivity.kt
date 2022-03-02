package com.jacee.magicdialog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.baicizhan.framework.common.magicdialog.*
import com.baicizhan.framework.common.magicdialog.utils.show
import com.jacee.magicdialog.databinding.ActivityCustomBinding

class CustomThemeActivity : AppCompatActivity(), OnDialogFragmentInteraction {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ActivityCustomBinding.inflate(layoutInflater).let {
            setContentView(it.root)
            it.prompt.setOnClickListener {
                show(
                    PromptDialog.Builder(this)
                        .title("自定义")
                        .message("所有颜色都自定义").build(), "custom"
                )
            }
            it.options.setOnClickListener {
                show(
                    OptionListDialog.Builder(this)
                        .options(arrayOf("a", "b", "c", "d"))
//                        .type(ButtonType.SINGLE_POSITIVE)
                        .build()
                )
            }
            it.optionsCheck.setOnClickListener {
                show(
                    OptionListDialog.Builder(this)
                        .title("选择器")
//                        .type(ButtonType.SINGLE_NEGATIVE)
                        .options(arrayOf("a", "b", "c", "d"), 2)
                        .build()
                )
            }
            it.wheel.setOnClickListener {
                WheelPickerDialog.Builder(this)
                    .wheels(arrayListOf("a", "b", "c", "d", "e", "f"))
//                    .type(ButtonType.SINGLE_POSITIVE)
                    .title("标题")
                    .positive("好吧")
                    .build()
                    .show(supportFragmentManager, "wheel")
            }
            it.date.setOnClickListener {
                DatePickerDialog.Builder(this)
                    .title("日期")
                    .dateTo("1990", "6", "3")
//                    .type(ButtonType.SINGLE_NEGATIVE)
                    .build()
                    .setOnDateListener { year, month, day ->
                        Toast.makeText(this, "$year, $month, $day", Toast.LENGTH_SHORT).show()
                    }
                    .show(supportFragmentManager, "date")
            }
        }
    }

    override fun onDialogNegativeClick(v: View) {
        Toast.makeText(this, "取消", Toast.LENGTH_LONG).show()
    }

    override fun onDialogPositiveClick(v: View) {
        Toast.makeText(this, "确定", Toast.LENGTH_LONG).show()
    }
}