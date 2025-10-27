package com.asphalt.commonui.utils

import android.content.Context
import android.location.Address
import android.location.Geocoder
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit

object Utils {

    fun getMonthYearFromCalendarInstance(calendar: Calendar): Pair<Int, Int> {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        return month to year
    }

    fun getMonthAbbr(calendar: Calendar): String {
        val shortMonthList = listOf(
            "Jan", "Feb", "Mar", "Apr", "May", "Jun",
            "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
        )

        return shortMonthList[calendar.get(Calendar.MONTH)]
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

    fun formatRelativeTime(pastTimestamp: String): String {
        return try {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            dateFormat.timeZone = TimeZone.getTimeZone("UTC")

            val pastDate = dateFormat.parse(pastTimestamp)

            val now = Date()
            val diff = now.time - pastDate!!.time

            if (diff < 0) return SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(
                pastDate
            )

            val seconds = TimeUnit.MILLISECONDS.toSeconds(diff)
            val minutes = TimeUnit.MILLISECONDS.toMinutes(diff)
            val hours = TimeUnit.MILLISECONDS.toHours(diff)
            val days = TimeUnit.MILLISECONDS.toDays(diff)

            val weeks = days / 7
            val months = days / 30 // Approximate month length
            val years = days / 365 // Approximate year length


            return when {
                seconds < 60 -> formatUnit(seconds, "second")
                minutes < 60 -> formatUnit(minutes, "minute")
                hours < 24 -> formatUnit(hours, "hour")
                days < 7 -> formatUnit(days, "day")
                weeks < 4 -> formatUnit(weeks, "week")

                months < 1 -> formatUnit(weeks, "week")
                months < 12 -> formatUnit(months, "month")

                years < 10 -> formatUnit(years, "year")
                else -> SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(pastDate)
            }

        } catch (e: Exception) {
            e.printStackTrace()
            pastTimestamp
        }
    }

    private fun formatUnit(value: Long, unit: String): String {
        return if (value == 1L) {
            "$value $unit ago"
        } else {
            "$value ${unit}s ago"
        }
    }

    fun nextMultipleOfFive(value: Int): Int {
        val remainder = value % 5
        return (5 - remainder) % 5
    }

    fun convertMillisToFormattedDate(
        millis: Long?,
        pattern: String = "MMM dd, yyyy"
    ): String {
        if (millis == null || millis <= 0) return ""

        val format = SimpleDateFormat(pattern, Locale.getDefault())

        return format.format(Date(millis))
    }

    fun formatClientMillisToISO(timestamp: Long): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)

        dateFormat.timeZone = TimeZone.getTimeZone("UTC")

        return dateFormat.format(Date(timestamp))
    }

    fun parseISODateToMillis(isoString: String): Long {
        return try {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
            dateFormat.timeZone = TimeZone.getTimeZone("UTC")

            dateFormat.parse(isoString)?.time ?: 0L
        } catch (e: Exception) {
            e.printStackTrace()
            0L
        }
    }


    @Suppress("DEPRECATION")
    fun getLocationRegion(context: Context, latitude: Double, longitude: Double): String {

        val geocoder = Geocoder(context, Locale.getDefault())
        val resultList: List<Address>?

        return try {
            resultList = geocoder.getFromLocation(latitude, longitude, 1)

            if (resultList.isNullOrEmpty()) {
                ""
            } else {
                val address: Address = resultList[0]

                val region = address.subLocality ?: address.locality ?: address.subAdminArea ?: ""

                val country = address.countryName ?: ""

                if (region.isNotEmpty() && country.isNotEmpty()) {
                    "$region, $country"
                } else region.ifEmpty {
                    country.ifEmpty {
                        ""
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            "Geocoding error"
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            "Invalid coordinates"
        }
    }

}