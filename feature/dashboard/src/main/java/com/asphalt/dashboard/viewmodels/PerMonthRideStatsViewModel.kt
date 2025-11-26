package com.asphalt.dashboard.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asphalt.android.model.dashboard.DashboardDomain
import com.asphalt.commonui.utils.Utils
import com.asphalt.dashboard.data.DashboardSummaryUI
import com.asphalt.dashboard.data.RideStatDataUIModel
import com.asphalt.dashboard.sealedclasses.RideStatType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import mappers.toDashboardSummaryUI
import mappers.toRideStatUiModel
import java.util.Calendar

class PerMonthRideStatsViewModel() : ViewModel() {
    private val _perMonthStats =
        MutableStateFlow(getDefaultStats())
    val perMonthStats: StateFlow<List<RideStatDataUIModel>> = _perMonthStats
    private val _calendar = MutableStateFlow<Calendar>(Calendar.getInstance())
    val calendar: StateFlow<Calendar> = _calendar
    private var dashboardSummaryUI = emptyList<DashboardSummaryUI>()


    fun getRideStatsByDate() {
        viewModelScope.launch {
            val (month, year) = Utils.getMonthYearFromCalendarInstance(_calendar.value)
            val perMonthRideData = dashboardSummaryUI.find {
                year == it.monthYear.year
                        && month == it.monthYear.month
            }.toRideStatUiModel()
            _perMonthStats.value = perMonthRideData
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

    private fun getDefaultStats(): List<RideStatDataUIModel> {
        return listOf(
            RideStatDataUIModel(RideStatType.TotalRides, 0),
            RideStatDataUIModel(RideStatType.TotalKms, 0),
            RideStatDataUIModel(RideStatType.Locations, 0)
        )

    }

    fun setSummaryData(dashboardSummary: List<DashboardDomain>) {
        dashboardSummaryUI = dashboardSummary.toDashboardSummaryUI()
    }


}