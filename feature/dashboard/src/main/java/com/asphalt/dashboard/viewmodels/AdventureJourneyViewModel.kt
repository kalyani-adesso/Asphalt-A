package com.asphalt.dashboard.viewmodels

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.asphalt.android.model.dashboard.DashboardDomain
import com.asphalt.dashboard.constants.AdventureJourneyConstants
import com.asphalt.dashboard.sealedclasses.AdventureJourneyTimeFrameChoices
import com.asphalt.dashboard.sealedclasses.RideGraphLegend
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.asphalt.dashboard.mappers.toJourneyDataUIModel
import java.util.Calendar

class AdventureJourneyViewModel : ViewModel() {
    private val _totalRides = MutableStateFlow(0)
    private val _colorList = MutableStateFlow<List<Color>>(emptyList())
    private val _valueList = MutableStateFlow<List<Float>>(emptyList())
    val totalRides: StateFlow<Int> = _totalRides
    val colorList: StateFlow<List<Color>> = _colorList
    val valueList: StateFlow<List<Float>> = _valueList
    private var dashboardData = emptyList<DashboardDomain>()
    private var thisMonthData = emptyList<DashboardDomain>()
    private var lastMonthData = emptyList<DashboardDomain>()
    private var lastYearData = emptyList<DashboardDomain>()
    private var fourMonthsMonthData = emptyList<DashboardDomain>()
    private var sixMonthData = emptyList<DashboardDomain>()

    fun setDashboardData(data: List<DashboardDomain>) {
        dashboardData = data
        thisMonthData =
            dashboardData.filterAndSummarizeByTimeRange(AdventureJourneyTimeFrameChoices.ThisMonth)
        lastMonthData =
            dashboardData.filterAndSummarizeByTimeRange(AdventureJourneyTimeFrameChoices.LastMonth)
        lastYearData =
            dashboardData.filterAndSummarizeByTimeRange(AdventureJourneyTimeFrameChoices.LastYear)
        fourMonthsMonthData =
            dashboardData.filterAndSummarizeByTimeRange(AdventureJourneyTimeFrameChoices.LastFourMonths)
        sixMonthData =
            dashboardData.filterAndSummarizeByTimeRange(AdventureJourneyTimeFrameChoices.LastSixMonths)


    }

    fun List<DashboardDomain>.filterAndSummarizeByTimeRange(
        filter: AdventureJourneyTimeFrameChoices
    ): List<DashboardDomain> {

        val calendar = Calendar.getInstance()
        val todayMonth = calendar.get(Calendar.MONTH) + 1
        val todayYear = calendar.get(Calendar.YEAR)
        val lastMonthCalendar = Calendar.getInstance()
        lastMonthCalendar.add(Calendar.MONTH, -1)
        val lastMonthTarget = lastMonthCalendar.get(Calendar.MONTH) + 1
        val lastYearTarget = lastMonthCalendar.get(Calendar.YEAR)

        val cutoffTimeMillis: Long = when (filter) {

            AdventureJourneyTimeFrameChoices.ThisMonth -> {
                calendar.set(todayYear, todayMonth - 1, 1, 0, 0, 0)
                calendar.timeInMillis
            }

            AdventureJourneyTimeFrameChoices.LastMonth -> {
                calendar.add(Calendar.MONTH, -1)
                calendar.set(Calendar.DAY_OF_MONTH, 1)
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)
                calendar.timeInMillis
            }

            AdventureJourneyTimeFrameChoices.LastFourMonths -> {
                calendar.add(Calendar.MONTH, -3)
                calendar.set(Calendar.DAY_OF_MONTH, 1)
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)
                calendar.timeInMillis
            }

            AdventureJourneyTimeFrameChoices.LastSixMonths -> {
                calendar.add(Calendar.MONTH, -5)
                calendar.set(Calendar.DAY_OF_MONTH, 1)
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)
                calendar.timeInMillis
            }

            AdventureJourneyTimeFrameChoices.LastYear -> {
                calendar.add(Calendar.MONTH, -11)
                calendar.set(Calendar.DAY_OF_MONTH, 1)
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)
                calendar.timeInMillis
            }
        }

        val filteredDomains = this.filter { domain ->
            val domainMonth = domain.monthYear.month
            val domainYear = domain.monthYear.year
            val domainDateMillis =
                domain.perMonthData.minOfOrNull { it.endRideDate ?: Long.MAX_VALUE }
                    ?: Long.MAX_VALUE
            val meetsLowerBound = domainDateMillis >= cutoffTimeMillis

            when (filter) {

                AdventureJourneyTimeFrameChoices.LastMonth -> {
                    domainYear == lastYearTarget && domainMonth == lastMonthTarget
                }

                else -> meetsLowerBound
            }
        }

        return filteredDomains
    }


    fun fetchAdventureData(choiceId: Int) {
        val fetchAdventureData = when (choiceId) {
            AdventureJourneyConstants.CHOICE_LAST_YEAR -> lastYearData
            AdventureJourneyConstants.CHOICE_LAST_MONTH -> lastMonthData
            AdventureJourneyConstants.CHOICE_THIS_MONTH -> thisMonthData
            AdventureJourneyConstants.CHOICE_LAST_SIX_MONTHS -> sixMonthData
            AdventureJourneyConstants.CHOICE_LAST_FOUR_MONTHS -> fourMonthsMonthData
            else -> thisMonthData
        }.toJourneyDataUIModel()
        _colorList.value = fetchAdventureData.map { it.legend.color }
        _valueList.value = fetchAdventureData.map { it.value }
        _totalRides.value =
            fetchAdventureData.find { data -> data.legend == RideGraphLegend.TotalRides }?.value?.toInt()
                ?: 0
    }


}