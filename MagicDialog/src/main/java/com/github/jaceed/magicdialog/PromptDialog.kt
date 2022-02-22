package com.github.jaceed.magicdialog

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import com.github.jaceed.extender.view.content
import com.github.jaceed.extender.view.visible
import com.github.jaceed.magicdialog.databinding.FragmentDialogPromptBinding

/**
 * Created by Jacee.
 * Date: 2021.03.26
 */
class PromptDialog : BaseCommonDialog() {

    override fun onTheme(): Int = R.style.DialogThemePrompt

    override fun onLocation(): Int {
        val sp = super.onLocation()
        return arguments?.getInt(ARG_LOCATION, sp) ?: sp
    }

    override fun onCreateContent(inflater: LayoutInflater): View? {
        return FragmentDialogPromptBinding.inflate(inflater).apply {
            pic.visible = (arguments?.getParcelable(ARG_PIC) as? Bitmap)?.let {
                pic.setImageBitmap(it)
                true
            } ?: run {
                /*arguments?.getParcelable<Uri>(ARG_PIC_URI)?.let {
                    PicParser.load(it)
                        .into(pic)
                    true
                } ?:*/ false
            }

            requireActivity().themeBy(R.attr.magicPromptStyle, R.styleable.MagicPrompt) { a ->
                a.getColor(R.styleable.MagicPrompt_magicTitleColor, 0).takeIf { it != 0 }?.let { c -> title.setTextColor(c) }
                a.getDimension(R.styleable.MagicPrompt_magicTitleSize, 0f).takeIf { it != 0f }?.let { d -> title.setTextSize(TypedValue.COMPLEX_UNIT_PX, d) }
                a.getColor(R.styleable.MagicPrompt_magicMessageColor, 0).takeIf { it != 0 }?.let { c -> message.setTextColor(c) }
                a.getDimension(R.styleable.MagicPrompt_magicMessageSize, 0f).takeIf { it != 0f }?.let { d -> message.setTextSize(TypedValue.COMPLEX_UNIT_PX, d) }
            }

            title.content = arguments?.getString(ARG_TITLE)?.takeIf { it.isNotBlank() }
            message.content = arguments?.getString(ARG_MESSAGE)?.takeIf { it.isNotBlank() }
            s2.visible = title.visible && message.visible
        }.root
    }


    class Builder(context: Context) : BaseCommonDialog.Builder<PromptDialog>(context) {

        fun pic(uri: Uri): Builder {
            arguments.putParcelable(ARG_PIC, null)
            arguments.putParcelable(ARG_PIC_URI, uri)
            return this
        }

        fun bitmap(bitmap: Bitmap): Builder {
            arguments.putParcelable(ARG_PIC_URI, null)
            arguments.putParcelable(ARG_PIC, bitmap)
            return this
        }

        fun title(title: String?): Builder {
            arguments.putString(ARG_TITLE, title)
            return this
        }

        fun message(message: String?): Builder {
            arguments.putString(ARG_MESSAGE, message)
            return this
        }

        fun location(location: Int): Builder {
            if (location and Location.Expanded == 0 && location and Location.Full == 0) {
                throw RuntimeException("Prompt dialog has to be expanded or full")
            }
            arguments.putInt(ARG_LOCATION, location)
            return this
        }

        override fun create() = PromptDialog().apply {
            this.arguments = this@Builder.arguments
        }

    }


    companion object {

        private const val ARG_PIC = "picture"
        private const val ARG_PIC_URI = "picture"
        private const val ARG_MESSAGE = "message"
        private const val ARG_LOCATION = "location"

    }


}