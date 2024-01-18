package com.jacee.magicdialog

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jacee.magicdialog.databinding.ActivityClassicDialogBinding
import com.jacee.magicdialog.dialog.TestDialog

/**
 * Created by Jacee.
 * Date: 2024.01.18
 */
class ClassicDialogActivity : AppCompatActivity() {

    private lateinit var binding: ActivityClassicDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(ActivityClassicDialogBinding.inflate(layoutInflater).apply {
            binding = this
            test.setOnClickListener {
                TestDialog(this@ClassicDialogActivity)
                    .show()
            }
            test2.setOnClickListener {
                TestDialog(this@ClassicDialogActivity.applicationContext) // TODO 还是不得行的，必须Activity的context
                    .show()
            }
        }.root)

    }

}