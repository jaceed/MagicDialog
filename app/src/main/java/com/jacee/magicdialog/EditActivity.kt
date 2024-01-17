package com.jacee.magicdialog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.jaceed.android.magicdialog.EditDialog
import com.jaceed.android.magicdialog.utils.show
import com.jacee.magicdialog.databinding.ActivityEditBinding

class EditActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityEditBinding.inflate(layoutInflater).apply {
            setContentView(root)
        }

        binding.defaultEdit.setOnClickListener {
            show(
                EditDialog.Builder(this)
                    .title("默认编辑")
                    .hint("测试提示")
                    .text("测试默认one text")
                    .build()
            )
        }

        binding.noIme.setOnClickListener {
            show(
                EditDialog.Builder(this)
                    .title("不自动显示输入法")
                    .ime(false)
                    .build()
            )
        }

        binding.getConfirm.setOnClickListener {
            show(
                EditDialog.Builder(this)
                    .title("获取文本")
                    .edited {
                        Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                    }
                    .build()
            )
        }

        binding.limit.setOnClickListener {
            show(
                EditDialog.Builder(this)
                    .title("限制")
                    .limit(10)
                    .edited {
                        Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                    }
                    .build()
            )
        }
    }
}