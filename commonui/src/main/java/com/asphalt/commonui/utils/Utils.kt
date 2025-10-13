package com.asphalt.commonui.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object Utils {

    fun getMonthYearFromCalendarInstance(calendar: Calendar): Pair<Int, Int> {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        return month to year
    }


    fun isBeforeCurrentMonthAndYear(givenCalendar: Calendar): Boolean {
        val current = Calendar.getInstance()

        val givenYear = givenCalendar.get(Calendar.YEAR)
        val givenMonth = givenCalendar.get(Calendar.MONTH)

        val currentYear = current.get(Calendar.YEAR)
        val currentMonth = current.get(Calendar.MONTH)

        return (givenYear < currentYear) ||
                (givenYear == currentYear && givenMonth < currentMonth)
    }

    fun formatDateWithFormatter(format: String, date: Date?): String? {
        val formatter =
            SimpleDateFormat(format, Locale.getDefault())
        if (date != null) {
            return formatter.format(date)
        }
        return null
    }


    fun formatDateTime(input: String, inputFormat: String, outputFormat: String): String {
        return try {
            val inputFormat = SimpleDateFormat(inputFormat, Locale.getDefault())
            val outputFormat = SimpleDateFormat(outputFormat, Locale.getDefault())
            val date = inputFormat.parse(input)
            outputFormat.format(date!!)
        } catch (e: Exception) {
            e.printStackTrace()
            input
        }
    }

    fun nextMultipleOfFive(value: Int): Int {
        val remainder = value % 5
        return (5 - remainder) % 5
    }


}