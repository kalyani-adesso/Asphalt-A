package com.asphalt.commonui.util

import junit.framework.TestCase.assertEquals
import org.junit.Test

class LocationUtilsTest {
    private val delta = 0.1 // acceptable error in km

    @Test
    fun distance_between_same_coordinates_should_be_zero() {
        val distance = LocationUtils.getDistance(
            12.9716, 77.5946,
            12.9716, 77.5946
        )
        assertEquals(0.0, distance, delta)
    }

    @Test
    fun `distance between Bangalore and Chennai`() {
        // Bangalore
        val lat1 = 12.9716
        val lon1 = 77.5946

        // Chennai
        val lat2 = 13.0827
        val lon2 = 80.2707

        val distance = LocationUtils.getDistance(lat1, lon1, lat2, lon2)

        // Approx distance ≈ 290 km
        assertEquals(290.0, distance, 5.0)
    }
    @Test
    fun `distance between New York and London`() {
        // New York
        val lat1 = 40.7128
        val lon1 = -74.0060

        // London
        val lat2 = 51.5074
        val lon2 = -0.1278

        val distance = LocationUtils.getDistance(lat1, lon1, lat2, lon2)

        // Approx distance ≈ 5570 km
        assertEquals(5570.0, distance, 20.0)
    }

    @Test
    fun `distance should be symmetric`() {
        val lat1 = 34.0522
        val lon1 = -118.2437 // Los Angeles

        val lat2 = 37.7749
        val lon2 = -122.4194 // San Francisco

        val d1 = LocationUtils.getDistance(lat1, lon1, lat2, lon2)
        val d2 = LocationUtils.getDistance(lat2, lon2, lat1, lon1)

        assertEquals(d1, d2, delta)
    }

    @Test
    fun `distance across equator`() {
        val lat1 = -1.0
        val lon1 = 30.0

        val lat2 = 1.0
        val lon2 = 30.0

        val distance = LocationUtils.getDistance(lat1, lon1, lat2, lon2)

        // 1 degree latitude ≈ 111 km, so 2 degrees ≈ 222 km
        assertEquals(222.0, distance, 5.0)
    }

    @Test
    fun `distance across prime meridian`() {
        val lat1 = 51.0
        val lon1 = -0.1

        val lat2 = 51.0
        val lon2 = 0.1

        val distance = LocationUtils.getDistance(lat1, lon1, lat2, lon2)

        // Small distance, around 14 km
        assertEquals(14.0, distance, 2.0)
    }

}