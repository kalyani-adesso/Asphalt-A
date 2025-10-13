package com.asphalt.dashboard.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asphalt.commonui.utils.Utils
import com.asphalt.dashboard.data.RidePerMonthDto
import com.asphalt.dashboard.data.RideStatData
import com.asphalt.dashboard.repository.PerMonthRideStatsRepo
import com.asphalt.dashboard.sealedclasses.RideStatType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Calendar

class PerMonthRideStatsViewModel(val rideStatsRepo: PerMonthRideStatsRepo) : ViewModel() {
    private val _perMonthStats =
        MutableStateFlow(getDefaultStats())
    val perMonthStats: StateFlow<List<RideStatData>> = _perMonthStats
    private val _calendar = MutableStateFlow<Calendar>(Calendar.getInstance())
    val calendar: StateFlow<Calendar> = _calendar

    init {
        getRideStatsByDate()
    }

    fun getRideStatsByDate() {
        viewModelScope.launch {
            val (month, year) = Utils.getMonthYearFromCalendarInstance(_calendar.value)
            val rideStats = rideStatsRepo.getRideStats(
                month,
                year
            )
            _perMonthStats.value = rideStats
        }
    }

    private fun incrementOrDecrementMonth(amount: Int) {
        _calendar.value = (_calendar.value.clone() as Calendar).apply {
            add(Calendar.MONTH, amount)
        }
    }

    fun loadPreviousMonth() {
        incrementOrDecrementMonth(-1)
        getRideStatsByDate()
    }

    fun loadNextMonth() {
        incrementOrDecrementMonth(1)
        getRideStatsByDate()
    }

    private fun getDefaultStats(): List<RideStatData> {
        return listOf(
            RideStatData(RideStatType.TotalRides, 0),
            RideStatData(RideStatType.TotalKms, 0),
            RideStatData(RideStatType.Locations, 0)
        )

    }


}