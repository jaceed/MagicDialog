package com.baicizhan.framework.common.magicdialog

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.StringRes
import coil.load
import com.baicizhan.framework.common.magicdialog.databinding.FragmentDialogPromptBinding
import com.github.jaceed.extender.view.content
import com.github.jaceed.extender.view.visible

/**
 * Created by Jacee.
 * Date: 2021.03.26
 */
class PromptDialog : BaseCommonDialog() {

    override val themeRes: Int = R.style.MagicDefault_Prompt
    override val appearance = R.attr.magicPromptAppearance
    override val location: Int
        get() = arguments?.getInt(ARG_LOCATION, -1).takeIf { it != -1 } ?: BOTTOM
    override val matchState: Int
        get() = arguments?.getInt(ARG_MATCH_STATE, -1).takeIf { it != -1 } ?: EXPANDED
    override val animationRes: Int = R.style.PromptDialogAnimation

    override fun onCreateContent(inflater: LayoutInflater): View? {
        return FragmentDialogPromptBinding.inflate(inflater).apply {
            pic.visible = (arguments?.getParcelable(ARG_PIC) as? Bitmap)?.let {
                pic.setImageBitmap(it)
                true
            } ?: run {
                arguments?.getParcelable<Uri>(ARG_PIC_URI)?.let {
                    pic.load(it)
                    true
                } ?: false
            }
            title.content = arguments?.getString(ARG_TITLE)?.takeIf { it.isNotBlank() }
            message.content = arguments?.getString(ARG_MESSAGE)?.takeIf { it.isNotBlank() }
            s2.visible = title.visible && message.visible
        }.root
    }


    class Builder(context: Context) : BaseCommonDialog.Builder<Builder, PromptDialog>(context) {

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

        fun message(@StringRes message: Int): Builder {
            return message(context.getString(message))
        }

        fun message(message: String?): Builder {
            arguments.putString(ARG_MESSAGE, message)
            return this
        }

        fun type(buttonType: ButtonType): Builder {
            arguments.putSerializable(ARG_BUTTON_TYPE, buttonType)
            return this
        }

        fun location(@Location location: Int): Builder {
            arguments.putInt(ARG_LOCATION, location)
            return this
        }

        fun match(@MatchState match: Int): Builder{
            arguments.putInt(ARG_MATCH_STATE, match)
            return this
        }

        override fun create() = PromptDialog().apply {
            this.arguments = this@Builder.arguments
        }

    }


    companion object {

        private const val ARG_PIC = "picture"
        private const val ARG_PIC_URI = "picture_uri"
        private const val ARG_MESSAGE = "message"

        private const val ARG_MATCH_STATE = "match_state"
        private const val ARG_LOCATION = "location"

    }


}