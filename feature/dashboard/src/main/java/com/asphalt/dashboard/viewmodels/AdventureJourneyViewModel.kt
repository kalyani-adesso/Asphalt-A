package com.asphalt.dashboard.viewmodels

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.asphalt.android.model.dashboard.DashboardDomain
import com.asphalt.commonui.utils.Utils
import com.asphalt.dashboard.repository.AdventureJourneyRepo
import com.asphalt.dashboard.sealedclasses.RideGraphLegend
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Calendar

class AdventureJourneyViewModel(val adventureJourneyRepo: AdventureJourneyRepo) : ViewModel() {
    private val _totalRides = MutableStateFlow(0)
    private val _colorList = MutableStateFlow<List<Color>>(emptyList())
    private val _valueList = MutableStateFlow<List<Float>>(emptyList())
    val totalRides: StateFlow<Int> = _totalRides
    val colorList: StateFlow<List<Color>> = _colorList
    val valueList: StateFlow<List<Float>> = _valueList
    private var dashboardData = emptyList<DashboardDomain>()
    private val currentMonthYear = Utils.getMonthYearFromCalendarInstance(Calendar.getInstance())
    fun setDashboardData(data: List<DashboardDomain>) {
        dashboardData = data
    }

    suspend fun fetchAdventureData(choiceId: Int) {
        val fetchAdventureData = adventureJourneyRepo.fetchAdventureData(choiceId)
        _colorList.value = fetchAdventureData.map { it.legend.color }
        _valueList.value = fetchAdventureData.map { it.value }
        _totalRides.value =
            fetchAdventureData.find { data -> data.legend == RideGraphLegend.TotalRides }?.value?.toInt()
                ?: 0
    }


}