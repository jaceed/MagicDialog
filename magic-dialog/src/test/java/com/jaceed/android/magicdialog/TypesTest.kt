package com.jaceed.android.magicdialog

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Created by Jacee.
 * Date: 2022.03.07
 */
class TypesTest {

    @Test
    fun state_test() {
        assertEquals(Location.BOTTOM facade State.EXPANDED, 0x0101)
        assertEquals(Location.CENTER facade State.EXPANDED, 0x0100)
        assertEquals(Location.BOTTOM facade State.FULL, 0x0201)
    }

    @Test
    fun location_test() {
        assertEquals(State.EXPANDED facade Location.BOTTOM, 0x0101)
        assertEquals(State.EXPANDED facade Location.CENTER, 0x0100)
        assertEquals(State.FULL facade Location.BOTTOM, 0x0201)
    }

    @Test
    fun state_and_location_test() {
        assertEquals(0x0101.state(), State.EXPANDED)
        assertEquals(0x0101.location(), Location.BOTTOM)
        assertEquals(0x0201.state(), State.FULL)
    }

    @Test(expected = ArrayIndexOutOfBoundsException::class)
    fun state_and_location_none_test() {
        assertEquals(0x0301.state(), State.EXPANDED)
    }

}