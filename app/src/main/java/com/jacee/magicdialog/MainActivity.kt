package com.jacee.magicdialog

import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.baicizhan.framework.common.magicdialog.*
import com.baicizhan.framework.common.magicdialog.utils.dismiss
import com.baicizhan.framework.common.magicdialog.utils.show
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
                    .type(ButtonType.SINGLE_NEGATIVE)
                    .build(), "single"
            )
        }

        binding.singlePositive.setOnClickListener {
            show(
                PromptDialog.Builder(this)
                    .title("标题")
                    .message("测试一下")
                    .type(ButtonType.SINGLE_POSITIVE)
                    .build(), "single positive"
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
                    .title("居中")
                    .location(CENTER)
                    .cancellable(true)
                    .build(), "ce"
            )
        }

        binding.centerWrap.setOnClickListener {
            show(
                PromptDialog.Builder(this)
                    .title("居中")
                    .message("内容适配")
                    .location(CENTER)
                    .match(WRAP)
                    .cancellable(true)
                    .build(), "ce"
            )
        }

        binding.bottomFull.setOnClickListener {
            show(
                PromptDialog.Builder(this)
                    .location(BOTTOM)
                    .match(FULL)
                    .cancellable(false)
                    .title("标题")
                    .message("测试一下")
                    .build(), "bf"
            )
        }

        binding.picture.setOnClickListener {
            show(
                PromptDialog.Builder(this)
                    .title("带图")
                    .pic(
                        Uri.parse(
                            "${ContentResolver.SCHEME_ANDROID_RESOURCE}://" +
                                    "${resources.getResourcePackageName(R.drawable.test_bg_pic_top)}/" +
                                    "${resources.getResourceTypeName(R.drawable.test_bg_pic_top)}/${resources.getResourceEntryName(R.drawable.test_bg_pic_top)}"
                        )
                    )
                    .build()
            )
        }

        binding.pictureMsg.setOnClickListener {
            show(
                PromptDialog.Builder(this)
                    .message("消息一下")
                    .pic(
                        Uri.parse(
                            "${ContentResolver.SCHEME_ANDROID_RESOURCE}://" +
                                    "${resources.getResourcePackageName(R.drawable.test_bg_pic_top)}/" +
                                    "${resources.getResourceTypeName(R.drawable.test_bg_pic_top)}/${resources.getResourceEntryName(R.drawable.test_bg_pic_top)}"
                        )
                    )
                    .build()
            )
        }

        binding.pictureTitleMsg.setOnClickListener {
            show(
                PromptDialog.Builder(this)
                    .title("带图")
                    .message("消息一下")
                    .bitmap(resources.getDrawable(R.mipmap.ic_launcher, null).let {
                        val bitmap = Bitmap.createBitmap(it.intrinsicWidth, it.intrinsicHeight, Bitmap.Config.ARGB_8888)
                        Canvas(bitmap).apply {
                            it.setBounds(0, 0, width, height)
                            it.draw(this)
                        }
                        bitmap
                    })
                    .build()
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.add(0, 0, 0, "主题")
        menu?.add(0, 1, 0, "选项")
        menu?.add(0, 2, 0, "测试")
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
            2 -> {
                show(
                    TestDialog.Builder(this).build()
                )
            }
            else -> {
                return false
            }
        }
        return super.onOptionsItemSelected(item)
    }
}