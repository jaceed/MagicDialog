package com.jacee.magicdialog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.github.jaceed.magicdialog.ButtonType
import com.github.jaceed.magicdialog.DatePickerDialog
import com.github.jaceed.magicdialog.OptionListDialog
import com.github.jaceed.magicdialog.WheelPickerDialog

class OptionsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_options)
    }

    fun optionsChecked(view: View) {
        OptionListDialog.Builder(this)
            .options((15..60 step 15).map { "$it 分钟" }.toTypedArray(), 0)
            .title("定时关闭")
            .type(ButtonType.SINGLE_NEGATIVE)
            .build()
            .show(supportFragmentManager, "checked")
//            .setInteraction {
//                Toast.makeText(this, "选择了 $it", Toast.LENGTH_SHORT).show()
//            }

    }

    fun datePicker(view: View) {
        DatePickerDialog.Builder(this)
            .dateTo("1990", "6", "3")
            .type(ButtonType.SINGLE_NEGATIVE)
            .build()
            .setOnDateListener { year, month, day ->
                Toast.makeText(this, "$year, $month, $day", Toast.LENGTH_SHORT).show()
            }
            .show(supportFragmentManager, "date")
    }

    fun wheelPicker(view: View) {
        WheelPickerDialog.Builder(this)
            .wheels(arrayListOf("a", "b", "c", "d", "e", "f"))
            .type(ButtonType.SINGLE_POSITIVE)
            .positive("好吧")
            .build()
            .show(supportFragmentManager, "wheel")
    }
}