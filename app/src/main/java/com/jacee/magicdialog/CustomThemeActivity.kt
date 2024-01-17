package com.jacee.magicdialog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.jaceed.android.magicdialog.utils.show
import com.jacee.magicdialog.databinding.ActivityCustomBinding
import com.jaceed.android.magicdialog.Action
import com.jaceed.android.magicdialog.DatePickerDialog
import com.jaceed.android.magicdialog.Location
import com.jaceed.android.magicdialog.OnDialogFragmentInteraction
import com.jaceed.android.magicdialog.OptionListDialog
import com.jaceed.android.magicdialog.PromptDialog
import com.jaceed.android.magicdialog.WheelPickerDialog

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

            it.customButtons.setOnClickListener {
                PromptDialog.Builder(this)
                    .title("按键定义")
                    .button(R.layout.custom_buttons)
                    .build().show(supportFragmentManager, "custom buttons")
            }

            it.customActionRecommended.setOnClickListener {
                show(
                    PromptDialog.Builder(this)
                        .title("custom action")
                        .cancellable(false)
                        .location(Location.BOTTOM)
                        .button(R.layout.custom_buttons)
                        .positive(action = Action.ALERT)
                        .build(), "layout"
                )
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