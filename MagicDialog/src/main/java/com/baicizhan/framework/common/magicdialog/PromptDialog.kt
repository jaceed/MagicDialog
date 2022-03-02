package com.baicizhan.framework.common.magicdialog

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.TypedValue
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

    override fun onLocation(): Int  = arguments?.getInt(ARG_LOCATION, -1).takeIf { it != -1 } ?: BOTTOM

    override fun onMatchState(): Int = arguments?.getInt(ARG_MATCH_STATE, -1).takeIf { it != -1 } ?: EXPANDED

    override fun onStyle(): StyleParams = StyleParams(R.attr.magicPromptStyle, R.styleable.PromptAppearance, intArrayOf(
        R.styleable.PromptAppearance_magicBackground,
        R.styleable.PromptAppearance_magicBackgroundMargin,
        R.styleable.PromptAppearance_magicBackgroundMarginBottom,
        R.styleable.PromptAppearance_magicTitleColor,
        R.styleable.PromptAppearance_magicTitleSize,
    ))

    override fun onAnimation(): Int {
        return R.style.PromptDialogAnimation
    }

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

            (themeData.titleColor.takeIf { it != 0 } ?: magicOnSurfaceColorDefault.takeIf { it != 0 })?.let {
                title.setTextColor(it)
            }
            themeData.titleSize.takeIf { it != 0 }?.let {
                title.setTextSize(TypedValue.COMPLEX_UNIT_PX, it.toFloat())
            }

            var messageColor = 0
            var messageSize = 0

            themeOf(R.attr.magicPromptStyle, R.styleable.PromptAppearance) { a ->
                a.getColor(R.styleable.PromptAppearance_magicMessageColor, 0).takeIf { it != 0 }?.let {
                    messageColor = it
                }
                a.getDimensionPixelSize(R.styleable.PromptAppearance_magicMessageSize, 0).takeIf { it != 0 }?.let {
                    messageSize = it
                }
            }

            if (messageColor != 0) message.setTextColor(messageColor)
            if (messageSize != 0) message.setTextSize(TypedValue.COMPLEX_UNIT_PX, messageSize.toFloat())

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

        override fun create() = PromptDialog().apply {
            this.arguments = this@Builder.arguments
        }

    }


    companion object {

        private const val ARG_PIC = "picture"
        private const val ARG_PIC_URI = "picture_uri"
        private const val ARG_MESSAGE = "message"

    }


}