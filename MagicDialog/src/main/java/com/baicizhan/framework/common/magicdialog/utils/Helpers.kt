@file:JvmName("Magics")
package com.baicizhan.framework.common.magicdialog.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.baicizhan.framework.common.magicdialog.BaseDialogFragment

/**
 * Created by Jacee.
 * Date: 2021.05.26
 */

private const val DEFAULT_DIALOG_TAG = "com.baicizhan.framework.common.magicdialog.tag"

fun FragmentActivity.show(dialog: BaseDialogFragment, tag: String = DEFAULT_DIALOG_TAG) {
    dialog.show(supportFragmentManager, tag)
}

fun FragmentActivity.dismiss(tag: String = DEFAULT_DIALOG_TAG) {
    supportFragmentManager.findFragmentByTag(tag)?.let {
        supportFragmentManager.beginTransaction()
            .remove(it)
            .commitAllowingStateLoss()
    }
}

fun Fragment.show(dialog: BaseDialogFragment, tag: String = DEFAULT_DIALOG_TAG) {
    dialog.show(childFragmentManager, tag)
}

fun Fragment.dismiss(tag: String = DEFAULT_DIALOG_TAG) {
    childFragmentManager.findFragmentByTag(tag)?.let {
        childFragmentManager.beginTransaction()
            .remove(it)
            .commitAllowingStateLoss()
    }
}