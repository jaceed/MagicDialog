package com.github.jaceed.magicdialog

import android.content.Context
import java.io.Serializable

/**
 * Created by Jacee.
 * Date: 2021.03.26
 */
data class Config(
    val cancel: CharSequence,
    val ok: CharSequence
) : Serializable {

    companion object {

        fun of(context: Context) =
            Config(context.getString(R.string.dialog_btn_cancel), context.getString(R.string.dialog_btn_ok))

        fun positive(context: Context, ok: CharSequence?) =
            Config(context.getString(R.string.dialog_btn_cancel), ok ?: context.getString(R.string.dialog_btn_ok))

        fun negative(context: Context, cancel: CharSequence?) =
            Config(cancel ?: context.getString(R.string.dialog_btn_cancel), context.getString(R.string.dialog_btn_ok))

    }

}

