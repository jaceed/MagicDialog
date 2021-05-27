package com.github.jaceed.magicdialog

import android.content.Context
import java.io.Serializable

/**
 * Created by Jacee.
 * Date: 2021.03.26
 */
internal data class Config(
    val cancel: CharSequence,
    val ok: CharSequence,
    val cancelAction: Action,
    val okAction: Action
) : Serializable {

    companion object {

        fun of(context: Context) =
            Config(context.getString(R.string.dialog_btn_cancel), context.getString(R.string.dialog_btn_ok), Action.NORMAL, Action.ALERT)

        fun positive(context: Context, ok: CharSequence?, action: Action = Action.ALERT) =
            Config(context.getString(R.string.dialog_btn_cancel), ok ?: context.getString(R.string.dialog_btn_ok), Action.NORMAL, action)

        fun negative(context: Context, cancel: CharSequence?, action: Action = Action.NORMAL) =
            Config(cancel ?: context.getString(R.string.dialog_btn_cancel), context.getString(R.string.dialog_btn_ok), action, Action.ALERT)

    }

}

