package com.jacee.magicdialog

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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
                    .negative("Cancel掉")
                    .positive("O了")
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

        binding.center.setOnClickListener {
            show(
                PromptDialog.Builder(this)
//                    .location(Location.Center) // Not supported
                    .cancellable(true)
                    .build(), "ce"
            )
        }

        binding.bottomExpanded.setOnClickListener {
            show(
                PromptDialog.Builder(this)
                    .location(Location.Bottom or Location.Expanded)
                    .title("标题")
                    .message("测试一下")
                    .build(), "b"
            )
        }

        binding.bottomFull.setOnClickListener {
            show(
                PromptDialog.Builder(this)
                    .location(Location.Bottom or Location.Full)
                    .title("标题")
                    .message("测试一下")
                    .build(), "bf"
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.add(0, 0, 0, "主题")
        menu?.add(0, 1, 0, "选项")
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            0 -> {
                startActivity(Intent(this, CustomThemeActivity::class.java))
            }
            1 -> {
                startActivity(Intent(this, OptionsActivity::class.java))
            }
            else -> {
                return false
            }
        }
        return super.onOptionsItemSelected(item)
    }
}