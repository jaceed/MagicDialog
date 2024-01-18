package com.jaceed.android.magicdialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.annotation.StyleRes
import androidx.fragment.app.FragmentManager
import com.jaceed.android.magicdialog.utils.styleOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import java.io.Closeable
import kotlin.coroutines.CoroutineContext

/**
 * Created by Jacee.
 * Date: 2024.01.17
 */
abstract class FoundationDialog(context: Context, themeResId: Int = R.style.MagicDefault) : Dialog(context, themeResId) {


    @StyleRes
    protected open val animationRes = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCancelable(onCancelable())
        context.theme.apply {
            styleOf(R.attr.magicAppearance) {
                applyStyle(it, true)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        window?.setWindowAnimations(animationRes.takeIf { it != 0 } ?: return)
    }


    protected open fun onCancelable(): Boolean = true


    companion object {
        private val TAG = FoundationDialog::class.java.simpleName
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