package com.stairwaytodev.project.utils

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

class RoomUtilsUnitTest {

    @Test
    fun fillInZeroInFrontNumberThatIsLessThan10() {
        val number = 9
        val expectedString = "08"
        val actualString = StrUtils.fillInZeroInFront(number)
        assertEquals(expectedString, actualString)
    }

    @Test
    fun convertTimeAndDateAsString_testLocaleUS() {
        val time = "14:32"
        val date = "9/6/2021"

        Locale.setDefault(Locale.US)//09/06/21 14:32
        var expectedString = "6/9/21 2:32 PM"
        var actualString = DateFormat.convertTimeAndDateAsString(time, date)
        assertEquals(expectedString, actualString)

        Locale.setDefault(Locale.UK)
        expectedString = "09/06/21 14:32"
        actualString = DateFormat.convertTimeAndDateAsString(time, date)
        assertEquals(expectedString, actualString)
    }

    @Test
    //!!! To run this test:
    //!!! 1. add package android.text in src/test,
    //!!! 2. goto "https://android.googlesource.com/platform/frameworks/base/+/refs/heads/master/core/java/android/text/TextUtils.java"
    //!!! 3. and copy the method "public static String[] split(String text, String expression)" to manually mock TextUtils.
    //!!! cause: "Method split in android.text.TextUtils not mocked. See http://g.co/androidstudio/not-mocked for details."
    fun convertTimeAsStringLocale_hoursNotStartingWithZero() {
        val time = "14:02"

        Locale.setDefault(Locale.US)
        var expectedTime = "2:02 PM"
        var actualTime = DateFormat.convertTimeAsStringLocale(time)
        assertEquals(expectedTime, actualTime)

        Locale.setDefault(Locale.CHINA)
        expectedTime = "2:02 下午"
        actualTime = DateFormat.convertTimeAsStringLocale(time)
        assertEquals(expectedTime, actualTime)
    }

    @Test
    //!!! To run this test:
    //!!! 1. add package android.text in src/test,
    //!!! 2. goto "https://android.googlesource.com/platform/frameworks/base/+/refs/heads/master/core/java/android/text/TextUtils.java"
    //!!! 3. and copy the method "public static String[] split(String text, String expression)" to manually mock TextUtils.
    //!!! cause: "Method split in android.text.TextUtils not mocked. See http://g.co/androidstudio/not-mocked for details."
    fun convertTimeAsStringLocale_hoursStartingWithZero() {
        val time = "04:02"

        Locale.setDefault(Locale.US)
        var expectedTime = "4:02 AM"
        var actualTime = DateFormat.convertTimeAsStringLocale(time)
        assertEquals(expectedTime, actualTime)

        Locale.setDefault(Locale.JAPANESE)
        expectedTime = "4:02 午前"
        actualTime = DateFormat.convertTimeAsStringLocale(time)
        assertEquals(expectedTime, actualTime)
    }

    @Test
    fun convertDateAsStringLocale() {
        Locale.setDefault(Locale.CHINA)
        val date = "07/06/2021"
        var expectedDate = "21-6-7"
        var actualDate = DateFormat.convertDateAsStringLocale(date)
        assertEquals(expectedDate, actualDate)

        Locale.setDefault(Locale.US)
        expectedDate = "6/7/21"
        actualDate = DateFormat.convertDateAsStringLocale(date)
        assertEquals(expectedDate, actualDate)

        Locale.setDefault(Locale.UK)
        expectedDate = "07/06/21"
        actualDate = DateFormat.convertDateAsStringLocale(date)
        assertEquals(expectedDate, actualDate)
    }
}