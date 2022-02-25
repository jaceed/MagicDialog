package com.github.jaceed.magicdialog

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import java.time.YearMonth
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by Jacee.
 * Date: 2021.04.06
 */

private const val LOADING_TAG = "magic_loading"

private fun setLoading(manager: FragmentManager, loading: Boolean) {
    manager.findFragmentByTag(LOADING_TAG).let {
        if (it?.isAdded != true && loading) {
            LoadingDialog().show(manager, LOADING_TAG)
        } else if (it != null && !loading) {
            (it as? LoadingDialog)?.dismiss()
        }
    }
}

private fun isLoading(manager: FragmentManager): Boolean = (manager.findFragmentByTag(LOADING_TAG) as? LoadingDialog)?.let {
    it.isAdded && it.isVisible
} ?: false

fun AppCompatActivity.isLoading(): Boolean= isLoading(supportFragmentManager)

fun AppCompatActivity.setLoading(loading: Boolean) {
    setLoading(supportFragmentManager, loading)
}

fun Fragment.setLoading(loading: Boolean) {
    setLoading(childFragmentManager, loading)
}

fun years(since: Long): ArrayList<String> {
    val now = Calendar.getInstance().get(Calendar.YEAR)
    return ArrayList<String>(150).apply {
        for (i in since..now) {
            add(i.toString())
        }
    }
}

fun months(): ArrayList<String> {
    return ArrayList<String>(12).apply {
        for (i in 1..12) {
            add(i.toString())
        }
    }
}

fun days(year: Int, month: Int): ArrayList<String> {
    val daysCount = YearMonth.of(year, month).lengthOfMonth()
    return ArrayList<String>(12).apply {
        for (i in 1..daysCount) {
            add(i.toString())
        }
    }
}

fun main(args: Array<String>) {
    val years = years(1900)
    println("last: ${years[years.size - 1]},  size: ${years.size}")
}