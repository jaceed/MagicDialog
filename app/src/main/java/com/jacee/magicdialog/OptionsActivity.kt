package com.jacee.magicdialog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.github.jaceed.magicdialog.ButtonType
import com.github.jaceed.magicdialog.CheckOptionDialog

class OptionsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_options)
    }

    fun optionsChecked(view: View) {
        CheckOptionDialog.Builder(this)
            .options((15..60 step 15).map { "$it 分钟" }.toTypedArray(), 0)
            .title("定时关闭")
            .type(ButtonType.SINGLE)
            .build()
            .show(supportFragmentManager, "checked")
//            .setInteraction {
//                Toast.makeText(this, "选择了 $it", Toast.LENGTH_SHORT).show()
//            }

    }
}