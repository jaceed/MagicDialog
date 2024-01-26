package com.jaceed.android.magicdialog

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.jaceed.extender.view.setOnProtectedClickListener
import com.github.jaceed.extender.view.visible
import com.jaceed.android.magicdialog.databinding.FragmentDialogCheckOptionBinding
import com.jaceed.android.magicdialog.databinding.ItemDialogCheckOptionBinding
import com.jaceed.android.magicdialog.utils.colorOf

/**
 * Created by Jacee.
 * Date: 2021.07.15
 */
class OptionListDialog : BaseOptionDialog() {

    private lateinit var options: Array<String>
    private var checkedIndex: Int = -1
    private var interaction: CheckOptionInteraction? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        options = arguments?.getStringArray(ARG_OPTIONS) ?: emptyArray()
        checkedIndex = arguments?.getInt(ARG_CHECKED, -1) ?: -1
    }

    override fun onCreateOptionView(inflater: LayoutInflater): View? {
        return FragmentDialogCheckOptionBinding.inflate(inflater).apply {
            optionList.adapter = OptionAdapter()
        }.root
    }

    override fun onConfigureButtons(): ButtonType = ButtonType.SINGLE_NEGATIVE

    fun setInteraction(interaction: CheckOptionInteraction): OptionListDialog {
        this.interaction = interaction
        return this
    }


    private inner class OptionHolder(binding: ItemDialogCheckOptionBinding) : RecyclerView.ViewHolder(binding.root) {
        val name: TextView = binding.option
        val checked: ImageView = binding.checked

        init {
            binding.root.setOnProtectedClickListener {
                interaction?.onCheckOption(adapterPosition)
                dismiss()
            }
            colorOf(R.attr.magicOptionItemCheckColor).takeIf { it != 0 }?.let {
                checked.imageTintList = ColorStateList.valueOf(it)
            }
        }
    }


    @SuppressLint("ResourceType")
    private inner class OptionAdapter : RecyclerView.Adapter<OptionHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionHolder {
            return OptionHolder(ItemDialogCheckOptionBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }

        override fun onBindViewHolder(holder: OptionHolder, position: Int) {
            holder.name.text = options[position]
            holder.checked.visible = checkedIndex == position
        }

        override fun getItemCount(): Int {
            return options.size
        }

    }

    class Builder(context: Context) : BaseContentDialog.Builder<Builder, OptionListDialog>(context) {

        @JvmOverloads
        fun options(list: Array<Int>, checked: Int = -1): Builder =
            options(list.map { res ->
                context.getString(res)
            }.toTypedArray(), checked)

        @JvmOverloads
        fun options(list: Array<String>, checked: Int = -1): Builder {
            if (checked != -1) {
            require(checked in list.indices)
            }
            arguments.putStringArray(ARG_OPTIONS, list)
            arguments.putInt(ARG_CHECKED, checked)
            return this
        }

        override fun create() = OptionListDialog()

    }


    fun interface CheckOptionInteraction {
        fun onCheckOption(index: Int)
    }

    companion object {
        private const val ARG_OPTIONS = "check_options"
        private const val ARG_CHECKED = "check_index"
    }

}