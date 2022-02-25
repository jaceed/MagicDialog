package com.github.jaceed.magicdialog

import android.content.Context
import android.util.Log
import com.github.jaceed.extender.standard.available
import com.github.jaceed.magicdialog.databinding.FragmentDialogWheelPickerBinding
import java.util.*
import kotlin.collections.ArrayList
import kotlin.contracts.ExperimentalContracts
import kotlin.math.min

/**
 * Created by Jacee.
 * Date: 2021.07.15
 */
class DatePickerDialog : WheelPickerDialog() {

    private var daysRefresher: () -> Unit = {}
    private var onDateListener: OnDateListener? = null
    private var initForDay = false

    @OptIn(ExperimentalContracts::class)
    override fun onCreateWheels(binding: FragmentDialogWheelPickerBinding) {
        val dateTo = arguments?.getStringArray(ARG_DATE_DEFAULT) ?: emptyArray()
        years(YEAR_START).let {
            binding.left.content = it
            binding.left.setSelectedItemPosition(
                indexOfItem(it, dateTo.getOrNull(PICKER_INDEX_LEFT), it.size - 1 + YEAR_OFFSET_DEFAULT)
                    .also { pos -> selectedLeft = pos }, false
            )
        }
        months().let {
            binding.center.content = it
            binding.center.setSelectedItemPosition(
                indexOfItem(it, dateTo.getOrNull(PICKER_INDEX_CENTER))
                    .also { pos -> selectedCenter = pos }, false
            )
        }
        daysRefresher = {
            days(
                Integer.parseInt((binding.left.content as ArrayList<String>)[selectedLeft]),
                Integer.parseInt((binding.center.content as ArrayList<String>)[selectedCenter])
            ).let {
                binding.right.content = it
                // update index
                if (!initForDay) {
                    initForDay = true
                    binding.right.setSelectedItemPosition(
                        indexOfItem(it, dateTo.getOrNull(PICKER_INDEX_RIGHT))
                            .also { pos -> selectedRight = pos }, false
                    )
                } else {
                    selectedRight = min(it.size - 1, selectedRight).also {
                        Log.d(TAG, "update right index from $selectedRight to $it")
                    }
                }
            }
        }
        daysRefresher()

        setOnPickInteraction { left, center, right ->
            onDateListener?.onDateSelect(
                (binding.left.content as ArrayList<String>)[left],
                (binding.center.content as ArrayList<String>)[center],
                (binding.right.content as ArrayList<String>)[right],
            )
        }
    }

    override fun onItemSelectedChanged(pickerIndex: Int, position: Int) {
        Log.d(TAG, "onItemSelectedChanged: $pickerIndex, $position")
        if (pickerIndex != PICKER_INDEX_RIGHT) {
            refresh()
        }
    }


    private fun refresh() {
        daysRefresher()
    }

    @OptIn(ExperimentalContracts::class)
    private fun indexOfItem(list: ArrayList<String>, value: String?, defaultIndex: Int = 0) =
        value.available { d ->
            list.indexOf(d).let { i -> if (i == -1) 0 else i }
        } ?: defaultIndex


    fun setOnDateListener(onDateListener: OnDateListener): DatePickerDialog {
        this.onDateListener = onDateListener
        return this
    }


    fun interface OnDateListener {
        fun onDateSelect(year: String, month: String, day: String)
    }


    class Builder(context: Context) : BaseCommonDialog.Builder<Builder, DatePickerDialog>(context)  {

        fun dateTo(time: Long): Builder {
            Calendar.getInstance().apply {
                timeInMillis = time
                dateTo(get(Calendar.YEAR).toString(), get(Calendar.MONTH).plus(1).toString(), get(Calendar.DAY_OF_MONTH).toString())
            }
            return this
        }

        fun dateTo(year: String = "", month: String = "", day: String = ""): Builder {
            arguments.putStringArray(ARG_DATE_DEFAULT, arrayOf(year, month, day))
            return this
        }

        override fun create() = DatePickerDialog().apply {
            this.arguments = this@Builder.arguments
        }

    }


    companion object {
        private val TAG = DatePickerDialog::class.java.simpleName
        private const val YEAR_START = 1900L
        private const val YEAR_OFFSET_DEFAULT = -20

        private const val ARG_DATE_DEFAULT = "date_default"
    }

}