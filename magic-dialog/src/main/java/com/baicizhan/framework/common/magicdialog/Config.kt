package com.baicizhan.framework.common.magicdialog

import android.content.Context
import android.view.View
import java.io.Serializable

/**
 * Created by Jacee.
 * Date: 2021.03.26
 */
internal data class Config(
    val cancel: CharSequence,
    val ok: CharSequence,
    val neutral: CharSequence? = null,
    val actionCancel: Action? = null,
    val actionOk: Action? = null,
    val actionNeutral: Action? = null,
    val cancelCallback: (View) -> Unit = {},
    val okCallback: (View) -> Unit = {},
    val neutralCallback: (View) -> Unit = {}
) : Serializable {

    companion object {

        fun of(context: Context) =
            Config(context.getString(R.string.dialog_btn_cancel), context.getString(R.string.dialog_btn_ok))

        fun positive(context: Context, ok: CharSequence?, action: Action?, okCallback: (View) -> Unit) =
            Config(context.getString(R.string.dialog_btn_cancel), ok ?: context.getString(R.string.dialog_btn_ok), actionOk = action, okCallback = okCallback)

        fun negative(context: Context, cancel: CharSequence?, cancelCallback: (View) -> Unit) =
            Config(cancel ?: context.getString(R.string.dialog_btn_cancel), context.getString(R.string.dialog_btn_ok), cancelCallback = cancelCallback)

        fun neutral(context: Context, neutral: CharSequence?, neutralCallback: (View) -> Unit) =
            Config(context.getString(R.string.dialog_btn_cancel), context.getString(R.string.dialog_btn_ok), neutral, neutralCallback = neutralCallback)

    }

}

