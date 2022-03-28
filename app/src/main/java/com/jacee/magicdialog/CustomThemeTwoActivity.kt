package com.jacee.magicdialog

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.baicizhan.framework.common.magicdialog.PromptDialog
import com.baicizhan.framework.common.magicdialog.utils.show
import com.jacee.magicdialog.databinding.ActivityCustomThemeTwoBinding

class CustomThemeTwoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ActivityCustomThemeTwoBinding.inflate(layoutInflater).let {
            setContentView(it.root)

            it.first.setOnClickListener {
                show(
                    PromptDialog.Builder(this)
                        .title("自定义")
                        .build(), "custom"
                )
            }
        }

    }
}