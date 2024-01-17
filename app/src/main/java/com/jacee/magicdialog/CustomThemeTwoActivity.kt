package com.jacee.magicdialog

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jaceed.android.magicdialog.OptionListDialog
import com.jaceed.android.magicdialog.PromptDialog
import com.jaceed.android.magicdialog.State
import com.jaceed.android.magicdialog.utils.show
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
                        .state(State.FULL)
                        .build(), "custom"
                )
            }

            it.prompAnim.setOnClickListener {
                show(
                    PromptDialog.Builder(this)
                        .title("prompt anim")
                        .build()
                )
            }

            it.optionsAnim.setOnClickListener {
                show(
                    OptionListDialog.Builder(this)
                        .title("options anim")
                        .options(arrayOf("a", "b", "c"))
                        .build()
                )
            }
        }

    }
}