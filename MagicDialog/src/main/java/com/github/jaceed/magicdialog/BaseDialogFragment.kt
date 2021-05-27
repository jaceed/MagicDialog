package com.github.jaceed.magicdialog

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager

/**
 * Created by Jacee.
 * Date: 2019.05.16
 */
abstract class BaseDialogFragment : DialogFragment() {

    private var dismissListener: DialogInterface.OnDismissListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, onTheme())
        isCancelable = onCancelable()
    }

    override fun show(manager: FragmentManager, tag: String?) {
        try {
            if (manager.findFragmentByTag(tag) != null) {
                Log.d(TAG, "show: already added")
                return
            }
            manager.beginTransaction().remove(this).commitAllowingStateLoss()
            manager.beginTransaction().add(this, tag).commitAllowingStateLoss()
        } catch (e: Exception) {
            Log.e(TAG, "show: $e")
        }
    }

    override fun dismiss() {
        try {
            dismissAllowingStateLoss()
        } catch (e: Exception) {
            Log.e(TAG, "dismiss: $e")
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        dismissListener?.onDismiss(dialog)
    }

    protected open fun onCancelable(): Boolean = true

    protected open fun onTheme(): Int = R.style.DialogThemeCommon

    fun setOnDismissListener(l: DialogInterface.OnDismissListener): BaseDialogFragment {
        dismissListener = l
        return this
    }

    companion object {
        private val TAG = BaseDialogFragment::class.java.simpleName
    }

}