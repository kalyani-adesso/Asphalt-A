package com.asphalt.commonui.util

import com.asphalt.commonui.constants.Constants
import com.asphalt.commonui.utils.Utils
import com.asphalt.commonui.utils.Utils.toFullMonthYear
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.test.Test
import kotlin.test.assertNull

class DateAndTimeTest {
    @Test
    fun getMonthYearFromCalendarInstance_returnsCorrectMonthYear() {
        val calendar = Calendar.getInstance()
        calendar.set(2025, Calendar.JANUARY, 1)

        val (month, year) = Utils.getMonthYearFromCalendarInstance(calendar)

        assertEquals(1, month)
        assertEquals(2025, year)
    }
//
    @Test
    fun toFullMonthYear_formatsCorrectly() {
        val calendar = Calendar.getInstance()
        calendar.set(2024, Calendar.DECEMBER, 10)

        val result = calendar.toFullMonthYear()

        assertEquals("December - 2024", result)
    }

    @Test
    fun isBeforeCurrentMonthAndYear_returnsTrueForPast() {
        val past = Calendar.getInstance()
        past.add(Calendar.MONTH, -1)

        assertTrue(Utils.isBeforeCurrentMonthAndYear(past))
    }

    @Test
    fun isBeforeCurrentMonthAndYear_returnsFalseForCurrent() {
        val current = Calendar.getInstance()

        assertFalse(Utils.isBeforeCurrentMonthAndYear(current))
    }
    @Test
    fun formatDateWithFormatter_validDate() {
        val date = Date(0)
        val result = Utils.formatDateWithFormatter("yyyy-MM-dd", date)

        assertEquals("1970-01-01", result)
    }

    @Test
    fun formatDateWithFormatter_nullDate() {
        val result = Utils.formatDateWithFormatter("yyyy-MM-dd", null)

        assertNull(result)
    }

    @Test
    fun formatDateTime_convertsCorrectly() {
        val input = "2025-01-01"
        val result = Utils.formatDateTime(
            input,
            "yyyy-MM-dd",
            "MMM dd, yyyy"
        )

        assertEquals("Jan 01, 2025", result)
    }
    @Test
    fun formatDateTime_invalidInput_returnsOriginal() {
        val input = "invalid-date"
        val result = Utils.formatDateTime(input, "yyyy-MM-dd", "MMM dd")

        assertEquals(input, result)
    }
    @Test
    fun formatRelativeTime_secondsAgo() {
        val sdf = SimpleDateFormat(Constants.SERVER_TIME_FORMAT, Locale.getDefault())
        sdf.timeZone = TimeZone.getTimeZone("UTC")

        val past = Date(System.currentTimeMillis() - 30_000)
        val input = sdf.format(past)

        val result = Utils.formatRelativeTime(input)

        assertEquals("30 seconds ago", result)
    }

    @Test
    fun formatRelativeTime_futureDate_formatsDate() {
        val sdf = SimpleDateFormat(Constants.SERVER_TIME_FORMAT, Locale.getDefault())
        sdf.timeZone = TimeZone.getTimeZone("UTC")

        val future = Date(System.currentTimeMillis() + 10_000)
        val input = sdf.format(future)

        val result = Utils.formatRelativeTime(input)

        assertTrue(result.contains(","))
    }

    @Test
    fun nextMultipleOfFive_returnsCorrectRemainder() {
        assertEquals(0, Utils.nextMultipleOfFive(10))
        assertEquals(2, Utils.nextMultipleOfFive(3))
        assertEquals(4, Utils.nextMultipleOfFive(1))
    }

    @Test
    fun convertMillisToFormattedDate_validMillis() {
        val result = Utils.convertMillisToFormattedDate(1767242259555L)
        assertEquals("Jan 01, 2026", result)
    }

    @Test
    fun convertMillisToFormattedDate_nullMillis() {
        assertEquals("", Utils.convertMillisToFormattedDate(null))
    }

    @Test
    fun getDateWithTime_formatsCorrectly() {
        val result = Utils.getDateWithTime(1767242259555L)
        assertTrue(result.contains("Thu, Jan 01 - 10:07 am"))
    }
    @Test
    fun getDateWithOutTime_formatsCorrectly() {
        val result = Utils.getDateWithOutTime(1767242259555L)
        assertTrue(result.contains("Thu, Jan 01"))
    }
    @Test
    fun getTime_formatsCorrectly() {
        val result = Utils.getTime(1767242259555L)
        assertTrue(result.contains("10:07 am"))
    }

}