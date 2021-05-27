package com.jacee.magicdialog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.github.jaceed.magicdialog.*
import com.github.jaceed.magicdialog.utils.dismiss
import com.github.jaceed.magicdialog.utils.show
import com.jacee.magicdialog.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ActivityMainBinding.inflate(layoutInflater).let {
            binding = it
            it.root
        })

        binding.promptDefault.setOnClickListener {
            PromptDialog.Builder(this)
                .title("标题")
                .message("测试一下")
                .build()
                .show(supportFragmentManager, "default")
        }

        binding.prompt.setOnClickListener {
            show(
                PromptDialog.Builder(this)
                    .title("标题")
                    .message("测试一下")
                    .negative("Cancel掉", Action.ALERT)
                    .positive("O了", Action.WARNING)
                    .cancellable(true)
                    .build()
                    .setOnDialogFragmentInteraction(object : OnDialogFragmentInteraction {
                        override fun onDialogNegativeClick(v: View) {
                            Toast.makeText(this@MainActivity, "取消", Toast.LENGTH_SHORT).show()
                        }

                        override fun onDialogPositiveClick(v: View) {
                            Toast.makeText(this@MainActivity, "确定", Toast.LENGTH_SHORT).show()
                        }

                    }), "prompt"
            )
        }

        binding.single.setOnClickListener {
            show(
                PromptDialog.Builder(this)
                    .title("标题")
                    .message("测试一下")
                    .type(ButtonType.SINGLE)
                    .build(), "single"
            )
        }

        binding.none.setOnClickListener {
            show(
                PromptDialog.Builder(this)
                    .title("标题")
                    .message("测试一下")
                    .cancellable(true)
                    .type(ButtonType.NONE)
                    .build(), "none"
            )
        }

        binding.loading.setOnClickListener {
            show(LoadingDialog(), "loading")
            window.decorView.postDelayed({
                dismiss("loading")
            }, 1_000)
        }
    }
}