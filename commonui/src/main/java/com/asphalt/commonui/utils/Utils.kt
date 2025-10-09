package com.asphalt.commonui.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object Utils {
    fun getCalendarInstanceForMonthYear(month: Int, year: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month - 1)
    }

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


    fun formatDateTime(input: String): String {
        return try {
            val inputFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            val outputFormat = SimpleDateFormat("EEE, dd MMM yyyy - hh:mm a", Locale.getDefault())
            val date = inputFormat.parse(input)
            outputFormat.format(date!!)
        } catch (e: Exception) {
            e.printStackTrace()
            input
        }
    }


}