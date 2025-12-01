package com.asphalt.commonui.utils

import android.content.Context
import android.location.Address
import android.location.Geocoder
import com.asphalt.commonui.constants.Constants
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit
import kotlin.math.ln
import kotlin.math.pow

object Utils {

    fun getMonthYearFromCalendarInstance(calendar: Calendar): Pair<Int, Int> {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        return month to year
    }

    fun Calendar.toFullMonthYear(): String {
        val formatter = SimpleDateFormat("MMMM - yyyy", Locale.getDefault())
        return formatter.format(this.time)
    }
    fun Int.toCompressedString(): String {
        if (this < 1000) return this.toString()

        val suffix = charArrayOf('K', 'M', 'B', 'T')

        val level = (ln(this.toDouble()) / ln(1000.0)).toInt()

        val divisor = 1000.0.pow(level.toDouble()).toLong()

        val baseNumber = this / divisor

        val remainder = this % divisor

        val suffixChar = suffix[level - 1]

        return if (remainder > 0) {
            "$baseNumber$suffixChar+"
        } else {
            "$baseNumber$suffixChar"
        }
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
            val dateFormat = SimpleDateFormat(Constants.SERVER_TIME_FORMAT, Locale.getDefault())
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

    fun formatDateMillisToISO(timestamp: Long?): String {
        val dateFormat = SimpleDateFormat(Constants.SERVER_TIME_FORMAT, Locale.getDefault())
        return timestamp?.let { dateFormat.format(Date(timestamp)) } ?: ""
    }

    fun parseISODateToMillis(isoString: String): Long {
        return try {
            val dateFormat = SimpleDateFormat(Constants.SERVER_TIME_FORMAT, Locale.getDefault())
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

    fun getDate(timeInMills: Long, hour: Int, mins: Int, isAm: Boolean): Long {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timeInMills

        val hour = hour
        val minute = mins

        val hour24 = if (isAm) hour % 12 else (hour % 12) + 12
        calendar.set(Calendar.HOUR_OF_DAY, hour24)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        //calendar.set(Calendar.AM_PM, if (isAm) Calendar.AM else Calendar.PM)

        val updatedMillis = calendar.timeInMillis
        return updatedMillis;
    }

    fun getDateWithTime(millis: Long?): String {
        val sdf = SimpleDateFormat("EEE, MMM dd - hh:mm a", Locale.getDefault())
        if (millis != null)
            return sdf.format(Date(millis))
        else
            return ""
    }

    fun getDateWithOutTime(millis: Long?): String {
        val sdf = SimpleDateFormat("EEE, MMM dd", Locale.getDefault())
        if (millis != null)
            return sdf.format(Date(millis))
        else
            return ""
    }

    fun getTime(millis: Long?): String {
        if (millis == null) return ""

        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return sdf.format(Date(millis))
    }
}