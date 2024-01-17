package com.jaceed.android.magicdialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.annotation.StyleRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentManager.FragmentLifecycleCallbacks
import com.jaceed.android.magicdialog.R
import com.jaceed.android.magicdialog.utils.styleOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withTimeout
import java.io.Closeable
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume

/**
 * Created by Jacee.
 * Date: 2019.05.16
 */
abstract class BaseDialogFragment : DialogFragment() {

    private var dismissListener: DialogInterface.OnDismissListener? = null
    protected open val themeRes = R.style.MagicDefault
    @StyleRes
    protected open val animationRes = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, themeRes)
        isCancelable = onCancelable()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            context.theme.apply {
                styleOf(R.attr.magicAppearance) {
                    applyStyle(it, true)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setWindowAnimations(animationRes.takeIf { it != 0 } ?: return)
    }

    override fun show(manager: FragmentManager, tag: String?) {
        try {
            manager.magicDialogScope.launch {
                lock.withLock {
                    actuallyShow(manager, tag)
                }
            }
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

    private suspend fun actuallyShow(manager: FragmentManager, tag: String?) = withTimeout(100) {
        suspendCancellableCoroutine<Boolean> { c ->
            if (manager.findFragmentByTag(tag) != null) {
                Log.d(TAG, "actuallyShow: already added")
                c.resume(false)
                return@suspendCancellableCoroutine
            }
            val cb = object : FragmentLifecycleCallbacks() {
                override fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context) {
                    super.onFragmentAttached(fm, f, context)
                     Log.d(TAG, "$f attached")
                    c.resume(this@BaseDialogFragment == f)
                    manager.unregisterFragmentLifecycleCallbacks(this)
                }
            }
            manager.registerFragmentLifecycleCallbacks(cb, false)
            manager.beginTransaction().add(this@BaseDialogFragment, tag).commitAllowingStateLoss()
        }
    }

    protected open fun onCancelable(): Boolean = true

    fun setOnDismissListener(l: DialogInterface.OnDismissListener): BaseDialogFragment {
        dismissListener = l
        return this
    }

    companion object {
        private const val TAG = "BaseDialogFragment"
        private val lock = Mutex()
    }


    private val FragmentManager.magicDialogScope: CoroutineScope by lazy {
        CloseableCoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    }

    private class CloseableCoroutineScope(context: CoroutineContext) : Closeable, CoroutineScope {
        override val coroutineContext: CoroutineContext = context

        override fun close() {
            coroutineContext.cancel()
        }
    }

}