package com.asphalt.commonui.util

import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

object LocationUtils {
    fun getDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {

        val R = 6371.0 // Radius of the Earth in km

        val lat1Rad = Math.toRadians(lat1)

        val lon1Rad = Math.toRadians(lon1)

        val lat2Rad = Math.toRadians(lat2)

        val lon2Rad = Math.toRadians(lon2)

        val dLat = lat2Rad - lat1Rad

        val dLon = lon2Rad - lon1Rad

        val a = sin(dLat / 2).pow(2.0) + cos(lat1Rad) * cos(lat2Rad) * sin(dLon / 2).pow(2.0)

        val c = 2 * asin(sqrt(a))

        val distance = R * c // distance in kilometers

        return distance

    }

}