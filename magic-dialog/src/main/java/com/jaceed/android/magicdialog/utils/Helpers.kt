@file:JvmName("Magics")
package com.jaceed.android.magicdialog.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.jaceed.android.magicdialog.BaseDialogFragment
import com.jaceed.android.magicdialog.PromptDialog

/**
 * Created by Jacee.
 * Date: 2021.05.26
 */

private const val DEFAULT_DIALOG_TAG = "com.jaceed.android.magicdialog.tag"

@JvmOverloads
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

@JvmOverloads
fun Fragment.show(dialog: BaseDialogFragment, tag: String = DEFAULT_DIALOG_TAG): Result<Boolean> {
    if (!isAdded)
        return Result.failure(Throwable("Not added yet!"))
    dialog.show(childFragmentManager, tag)
    return Result.success(true)
}

fun Fragment.dismiss(tag: String = DEFAULT_DIALOG_TAG) {
    if (!isAdded)
        return
    childFragmentManager.findFragmentByTag(tag)?.let {
        childFragmentManager.beginTransaction()
            .remove(it)
            .commitAllowingStateLoss()
    }
}

val Fragment.PromptBuilder: PromptDialog.Builder
    get() = PromptDialog.Builder(requireContext())

val FragmentActivity.PromptBuilder: PromptDialog.Builder
    get() = PromptDialog.Builder(this)

fun Fragment.prompt(tag: String = DEFAULT_DIALOG_TAG, onReference: ((PromptDialog) -> Unit)? = null, builder: PromptDialog.Builder.() -> Unit) = show(
    PromptBuilder.apply {
        builder()
    }.build().also { onReference?.invoke(it) }, tag
)

fun FragmentActivity.prompt(tag: String = DEFAULT_DIALOG_TAG, onReference: ((PromptDialog) -> Unit)? = null, builder: PromptDialog.Builder.() -> Unit) = show(
    PromptBuilder.apply {
        builder()
    }.build().also { onReference?.invoke(it) }, tag
)



