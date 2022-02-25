package com.baicizhan.framework.common.magicdialog

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.aigestudio.wheelpicker.WheelPicker
import com.github.jaceed.extender.view.visible
import com.baicizhan.framework.common.magicdialog.databinding.FragmentDialogWheelPickerBinding

/**
 * Created by Jacee.
 * Date: 2021.07.15
 */
open class WheelPickerDialog : BaseOptionDialog() {

    protected var selectedLeft: Int = 0
    protected var selectedCenter: Int = 0
    protected var selectedRight: Int = 0

    protected var interaction: OnPickInteraction? = null

    override fun onCreateOptionView(inflater: LayoutInflater): View {
        return FragmentDialogWheelPickerBinding.inflate(inflater).apply {
            onCreateWheels(this)
            onSetWheelsListener(this)
        }.root
    }

    override fun onConfigureButtons(): ButtonType = ButtonType.DOUBLE

    override fun onPositiveClick(v: TextView) {
        interaction?.onPicked(selectedLeft, selectedCenter, selectedRight)
    }

    protected open fun onCreateWheels(binding: FragmentDialogWheelPickerBinding) {
        binding.left.content = arguments?.getStringArrayList(ARG_WHEELS_LEFT)
        binding.center.content = arguments?.getStringArrayList(ARG_WHEELS_CENTER)
        binding.right.content = arguments?.getStringArrayList(ARG_WHEELS_RIGHT)
    }

    private fun onSetWheelsListener(binding: FragmentDialogWheelPickerBinding) {
        if (binding.left.visible) {
            binding.left.setOnItemSelectedListener { _, _, position ->
                selectedLeft = position
                Log.d(TAG, "left: $position -> [$selectedLeft, $selectedCenter, $selectedRight]")
                onItemSelectedChanged(PICKER_INDEX_LEFT, position)
            }
        }
        if (binding.center.visible) {
            binding.center.setOnItemSelectedListener { _, _, position ->
                selectedCenter = position
                Log.d(TAG, "center: $position -> [$selectedLeft, $selectedCenter, $selectedRight]")
                onItemSelectedChanged(PICKER_INDEX_CENTER, position)
            }
        }
        if (binding.right.visible) {
            binding.right.setOnItemSelectedListener { _, _, position ->
                selectedRight = position
                Log.d(TAG, "right: $position -> [$selectedLeft, $selectedCenter, $selectedRight]")
                onItemSelectedChanged(PICKER_INDEX_RIGHT, position)
            }
        }
    }

    protected open fun onItemSelectedChanged(pickerIndex: Int, position: Int) {

    }

    protected var WheelPicker.content: ArrayList<*>?
        get() = data as? ArrayList<*>?
        set(value) {
            visible = value != null
            data = value?.toList() ?: return
        }


    fun setOnPickInteraction(interaction: OnPickInteraction): WheelPickerDialog {
        this.interaction = interaction
        return this
    }


    class Builder(context: Context) : BaseCommonDialog.Builder<Builder, WheelPickerDialog>(context) {

        fun wheels(vararg list: ArrayList<String>): Builder {
            require(list.size in 1..3)
            list.getOrNull(PICKER_INDEX_LEFT)?.let {
                arguments.putStringArrayList(ARG_WHEELS_LEFT, it)
            }
            list.getOrNull(PICKER_INDEX_CENTER)?.let {
                arguments.putStringArrayList(ARG_WHEELS_CENTER, it)
            }
            list.getOrNull(PICKER_INDEX_RIGHT)?.let {
                arguments.putStringArrayList(ARG_WHEELS_RIGHT, it)
            }
            return this
        }

        override fun create() = WheelPickerDialog().apply {
            this.arguments = this@Builder.arguments
        }

    }


    fun interface OnPickInteraction {
        fun onPicked(left: Int, center: Int, right: Int)
    }


    companion object {
        private val TAG = WheelPickerDialog::class.java.simpleName

        private const val ARG_WHEELS_LEFT = "wheels_left"
        private const val ARG_WHEELS_CENTER = "wheels_center"
        private const val ARG_WHEELS_RIGHT = "wheels_right"

        const val PICKER_INDEX_LEFT = 0
        const val PICKER_INDEX_CENTER = 1
        const val PICKER_INDEX_RIGHT = 2

    }

}