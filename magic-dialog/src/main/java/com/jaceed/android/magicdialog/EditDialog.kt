package com.jaceed.android.magicdialog

import android.content.ContentResolver
import android.content.Context
import android.content.res.ColorStateList
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.core.widget.addTextChangedListener
import coil.load
import com.jaceed.android.magicdialog.R
import com.jaceed.android.magicdialog.utils.colorOf
import com.jaceed.android.magicdialog.utils.intResAvailable
import com.jaceed.android.magicdialog.utils.resourceOf
import com.jaceed.android.magicdialog.utils.styleOf
import com.github.jaceed.extender.standard.available
import com.github.jaceed.extender.view.content
import com.github.jaceed.extender.view.setOnProtectedClickListener
import com.github.jaceed.extender.view.visible
import java.lang.Integer.min

/**
 * Created by Jacee.
 * Date: 2022.05.25
 */
class EditDialog : BaseContentDialog() {

    override val themeRes: Int = R.style.MagicDefault_Edit
    override val appearanceAttribute = R.attr.magicEditAppearance
    override val facade: Int
        get() = Location.CENTER facade State.EXPANDED
    override val animationRes: Int
        get() = styleOf(R.attr.magicAnimation) ?: R.style.EditDialogAnimation

    private var layoutResource = R.layout.fragment_dialog_edit

    private val imm by lazy {
        requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    private val buttonDisabledColor by lazy {
        colorOf(R.attr.magicButtonActionRecommendedColorDisabled)
    }

    private lateinit var edit: EditText
    private var positiveButton: TextView? = null
    private var onEditListener: OnEditListener? = null

    private fun refreshButtons() {
        positiveButton?.isEnabled = !edit.editableText?.toString().isNullOrBlank()
    }

    private fun setOnEditListener(listener: OnEditListener?) {
        onEditListener = listener
    }

    override fun onCreateContent(inflater: LayoutInflater): View? {
        return inflater.inflate(arguments.intResAvailable(ARG_LAYOUT) ?: resourceOf(R.attr.magicEditLayout, layoutResource), null, false).apply {
            findViewById<TextView>(R.id.magic_prompt_title)?.content = arguments?.getString(ARG_TITLE)?.takeIf { it.isNotBlank() }
            edit = findViewById(R.id.magic_edit_text) ?: throw RuntimeException("No EditText available!")
            val clearBtn = findViewById<ImageView>(R.id.magic_edit_clear)
            val max = arguments?.getInt(ARG_MAX_INPUT, Int.MAX_VALUE) ?: Int.MAX_VALUE
            edit.addTextChangedListener {
                val r = (it?.toString() ?: "").let { e ->
                    e.substring(0, min(e.length, max))
                }
                if (r != it?.toString()) {
                    edit.setText(r)
                    edit.setSelection(r.length)
                    return@addTextChangedListener
                }
                clearBtn?.visible = r.isNotBlank()
                refreshButtons()
            }
            clearBtn?.let { clear ->
                arguments?.getParcelable<Uri>(ARG_CLEAR)?.let {
                    clear.load(it)
                }

                clear.setOnProtectedClickListener {
                    edit.text = null
                }
            }

            arguments?.getString(ARG_HINT).available {
                edit.hint = it
            }
            arguments?.getString(ARG_TEXT).available {
                it.substring(0, min(it.length, max)).let { r ->
                    edit.setText(r)
                    edit.setSelection(r.length)
                }
            }
            if (arguments?.getBoolean(ARG_AUTO_IME, true) == true)
                postDelayed({
                    if (!isAdded || !isShown) return@postDelayed
                    edit.requestFocus()
                    imm.showSoftInput(edit, InputMethodManager.SHOW_IMPLICIT)
                }, 150)
        }
    }

    override fun onConfigurePositive(v: TextView) {
        super.onConfigurePositive(v)
        positiveButton = v
        buttonDisabledColor.takeIf { it != 0 }?.let {
            positiveButton?.setTextColor(
                ColorStateList(
                    arrayOf(intArrayOf(-android.R.attr.state_enabled), intArrayOf()),
                    intArrayOf(it, v.currentTextColor)
                )
            )
        }
        refreshButtons()
    }

    override fun onPositiveClick(v: TextView) {
        super.onPositiveClick(v)
        onEditListener?.onConfirm(edit.editableText?.toString() ?: "")
    }

    class Builder(context: Context) : BaseContentDialog.Builder<Builder, EditDialog>(context) {

        private var listener: OnEditListener? = null

        fun layout(@LayoutRes layoutRes: Int): Builder {
            arguments.putInt(ARG_LAYOUT, layoutRes)
            return this
        }

        fun hint(@StringRes hintId: Int): Builder = hint(context.getString(hintId))

        fun hint(hint: CharSequence): Builder {
            arguments.putCharSequence(ARG_HINT, hint)
            return this
        }

        fun text(@StringRes textId: Int): Builder = text(context.getString(textId))

        fun text(hint: CharSequence): Builder {
            arguments.putCharSequence(ARG_TEXT, hint)
            return this
        }

        fun clear(@DrawableRes id: Int): Builder {
            arguments.putParcelable(
                ARG_CLEAR, Uri.parse(
                    "${ContentResolver.SCHEME_ANDROID_RESOURCE}://" +
                            "${context.resources.getResourcePackageName(id)}/" +
                            "${context.resources.getResourceTypeName(id)}/${context.resources.getResourceEntryName(id)}"
                )
            )
            return this
        }

        fun ime(auto: Boolean) : Builder {
            arguments.putBoolean(ARG_AUTO_IME, auto)
            return this
        }

        fun limit(max: Int): Builder {
            arguments.putInt(ARG_MAX_INPUT, max)
            return this
        }

        fun edited(listener: (String) -> Unit): Builder {
            this.listener = object : OnEditListener {
                override fun onConfirm(text: String) {
                    listener(text)
                }
            }
            return this
        }

        fun edited(listener: OnEditListener): Builder {
            this.listener = listener
            return this
        }

        override fun create(): EditDialog = EditDialog().apply {
            setOnEditListener(listener)
        }

    }

    interface OnEditListener {
        fun onConfirm(text: String)
    }


    companion object {
        private const val ARG_LAYOUT = "layout"
        private const val ARG_HINT = "hint"
        private const val ARG_TEXT = "text"
        private const val ARG_CLEAR = "clear"
        private const val ARG_AUTO_IME = "auto_ime"
        private const val ARG_MAX_INPUT = "max_input"
    }
}