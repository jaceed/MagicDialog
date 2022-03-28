package com.baicizhan.framework.common.magicdialog

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.annotation.StyleRes
import coil.load
import com.baicizhan.framework.common.magicdialog.utils.enumOf
import com.baicizhan.framework.common.magicdialog.utils.intResAvailable
import com.baicizhan.framework.common.magicdialog.utils.resourceOf
import com.github.jaceed.extender.view.content
import com.github.jaceed.extender.view.visible

/**
 * Created by Jacee.
 * Date: 2021.03.26
 */
class PromptDialog : BaseContentDialog() {

    override val themeRes: Int = R.style.MagicDefault_Prompt
    override val appearanceAttribute = R.attr.magicPromptAppearance
    override val facade: Int
        get() = ((arguments?.getSerializable(ARG_LOCATION) as? Location) ?: Location.values()[enumOf(R.attr.magicLocation, Location.BOTTOM.ordinal)]) facade
                ((arguments?.getSerializable(ARG_MATCH_STATE) as? State) ?: State.EXPANDED)
    override val animationRes: Int
        get() = arguments?.getInt(ARG_ANIMATION)?.takeIf { it != 0 } ?: R.style.PromptDialogAnimation

    private var layoutResource = R.layout.fragment_dialog_prompt

    override fun onCancelable(): Boolean = false

    override fun onCreateContent(inflater: LayoutInflater): View? {
        return inflater.inflate(arguments.intResAvailable(ARG_LAYOUT) ?: resourceOf(R.attr.magicPromptLayout, layoutResource), null, false).apply {
            findViewById<ImageView>(R.id.magic_prompt_pic)?.let { pic ->
                pic.visible = (arguments?.getParcelable(ARG_PIC) as? Bitmap)?.let {
                    pic.setImageBitmap(it)
                    true
                } ?: run {
                    arguments?.getParcelable<Uri>(ARG_PIC_URI)?.let {
                        pic.load(it)
                        true
                    } ?: false
                }
            }
            var paddingFlag = -2
            findViewById<TextView>(R.id.magic_prompt_title)?.content = arguments?.getString(ARG_TITLE)?.takeIf { it.isNotBlank() }?.also { paddingFlag++ }
            findViewById<TextView>(R.id.magic_prompt_message)?.content = arguments?.getString(ARG_MESSAGE)?.takeIf { it.isNotBlank() }?.also { paddingFlag++ }
            findViewById<View>(R.id.s2)?.visible = paddingFlag == 0
            findViewById<View>(R.id.s1)?.visible = paddingFlag != -2
            findViewById<View>(R.id.s3)?.visible = paddingFlag != -2
        }
    }


    class Builder(context: Context) : BaseContentDialog.Builder<Builder, PromptDialog>(context) {

        fun pic(uri: Uri): Builder {
            arguments.putParcelable(ARG_PIC, null)
            arguments.putParcelable(ARG_PIC_URI, uri)
            return this
        }

        fun pic(bitmap: Bitmap): Builder {
            arguments.putParcelable(ARG_PIC_URI, null)
            arguments.putParcelable(ARG_PIC, bitmap)
            return this
        }

        fun pic(@DrawableRes id: Int): Builder {
            arguments.putParcelable(ARG_PIC, null)
            arguments.putParcelable(ARG_PIC_URI, Uri.parse(
                "${ContentResolver.SCHEME_ANDROID_RESOURCE}://" +
                        "${context.resources.getResourcePackageName(id)}/" +
                        "${context.resources.getResourceTypeName(id)}/${context.resources.getResourceEntryName(id)}"
            ))
            return this
        }

        fun message(@StringRes message: Int): Builder {
            return message(context.getString(message))
        }

        fun message(message: String?): Builder {
            arguments.putString(ARG_MESSAGE, message)
            return this
        }

        fun layout(@LayoutRes layoutRes: Int): Builder {
            arguments.putInt(ARG_LAYOUT, layoutRes)
            return this
        }

        fun animation(@StyleRes animRes: Int): Builder {
            arguments.putInt(ARG_ANIMATION, animRes)
            return this
        }

        fun type(buttonType: ButtonType): Builder {
            arguments.putSerializable(ARG_BUTTON_TYPE, buttonType)
            return this
        }

        fun location(location: Location): Builder {
            arguments.putSerializable(ARG_LOCATION, location)
            return this
        }

        fun state(state: State): Builder {
            arguments.putSerializable(ARG_MATCH_STATE, state)
            return this
        }

        override fun create() = PromptDialog()

    }


    companion object {

        private const val ARG_PIC = "picture"
        private const val ARG_PIC_URI = "picture_uri"
        private const val ARG_MESSAGE = "message"
        private const val ARG_LAYOUT = "layout"
        private const val ARG_ANIMATION = "animation"

        private const val ARG_MATCH_STATE = "match_state"
        private const val ARG_LOCATION = "location"

    }


}