package com.baicizhan.framework.common.magicdialog

import android.content.Context
import androidx.annotation.StringRes

/**
 * Created by Jacee.
 * Date: 2022.03.04
 */
abstract class BaseContentDialog : BaseCommonDialog() {

    abstract class Builder<T : Builder<T, R>, R : BaseContentDialog>(context: Context) : BaseCommonDialog.Builder<T, R>(context) {

        fun title(@StringRes title: Int): T {
            return title(context.getString(title))
        }

        fun title(title: String?): T {
            arguments.putString(ARG_TITLE, title)
            return this as T
        }

    }

}