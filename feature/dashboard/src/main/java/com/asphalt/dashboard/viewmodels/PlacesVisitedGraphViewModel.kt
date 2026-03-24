package com.asphalt.dashboard.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asphalt.android.model.dashboard.DashboardDomain
import com.asphalt.android.viewmodels.AndroidUserVM
import com.asphalt.commonui.constants.Constants
import com.asphalt.commonui.utils.Utils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.asphalt.dashboard.mappers.fetchPlaceVisitedGraphData
import com.asphalt.dashboard.mappers.toPlaceVisitedGraphUIModel
import java.util.Calendar

class PlacesVisitedGraphViewModel(val androidUserVM: AndroidUserVM) : ViewModel() {

    private val _startDate = MutableStateFlow<Calendar?>(null)
    private val calendarOffset = Constants.NO_OF_MONTHS_PLACE_VISITED - 1
    private val _xValuesList = MutableStateFlow<List<String>>(emptyList())
    private val _yValueList = MutableStateFlow<List<Int>>(emptyList())
    private val _isForwardArrowEnabled = MutableStateFlow(false)
    private val _isBackArrowEnabled = MutableStateFlow(false)
    val startDate: StateFlow<Calendar?> = _startDate
    private val _endDate = MutableStateFlow<Calendar?>(null)
    val endDate: StateFlow<Calendar?> = _endDate
    val xValuesList: StateFlow<List<String>> = _xValuesList
    val yValueList: StateFlow<List<Int>> = _yValueList
    val isForwardArrowEnabled: StateFlow<Boolean> = _isForwardArrowEnabled
    val isBackArrowEnabled: StateFlow<Boolean> = _isBackArrowEnabled
    private val dashboardDomainList =
        MutableStateFlow(emptyList<DashboardDomain>())
    private val currentUid: String
        get() = androidUserVM.getCurrentUserUID()


    fun populateGraph(dashboardData: List<DashboardDomain>) {
        dashboardDomainList.value = dashboardData
        _endDate.value = Calendar.getInstance()
        _startDate.value = (_endDate.value?.clone() as Calendar).apply {
            add(
                Calendar.MONTH,
                -calendarOffset
            )
        }
        fetchPlacesVisitedGraphData()
    }

    fun setForwardArrowDisabledOrEnabled(inputCalendar: Calendar) {
        val current = Calendar.getInstance()
        (inputCalendar.get(Calendar.YEAR) < current.get(Calendar.YEAR) ||
                (inputCalendar.get(Calendar.YEAR) == current.get(Calendar.YEAR) &&
                        inputCalendar.get(Calendar.MONTH) < current.get(Calendar.MONTH))).also {
            _isForwardArrowEnabled.value = it
        }
    }

    fun setBackArrowDisabledOrEnabled(inputCalendar: Calendar, accountCreationDateMillis: Long) {
        val creationCalendar = Calendar.getInstance().apply {
            timeInMillis = accountCreationDateMillis
        }

        (inputCalendar.get(Calendar.YEAR) > creationCalendar.get(Calendar.YEAR) ||
                (inputCalendar.get(Calendar.YEAR) == creationCalendar.get(Calendar.YEAR) &&
                        inputCalendar.get(Calendar.MONTH) > creationCalendar.get(Calendar.MONTH))).also {

            _isBackArrowEnabled.value = it
        }
    }

    fun fetchDataPreviousDateRange() {
        _endDate.value =
            (_startDate.value?.clone() as Calendar).apply {
                add(Calendar.MONTH, -1)
            }
        _startDate.value = (_endDate.value?.clone() as Calendar).apply {
            add(Calendar.MONTH, -calendarOffset)
        }
        fetchPlacesVisitedGraphData()
    }

    fun fetchDataNextDateRange() {
        _startDate.value =
            (_endDate.value?.clone() as Calendar).apply {
                add(Calendar.MONTH, 1)
            }
        _endDate.value = (_startDate.value?.clone() as Calendar).apply {
            add(Calendar.MONTH, calendarOffset)
        }
        fetchPlacesVisitedGraphData()
    }

    fun fetchPlacesVisitedGraphData() {
        _endDate.value?.let { setForwardArrowDisabledOrEnabled(it) }
        _startDate.value?.let {
            setBackArrowDisabledOrEnabled(
                it,
                androidUserVM.getUser(currentUid)?.accountCreationDate ?: 0
            )
        }
        viewModelScope.launch {
            val graphDtoList = _startDate.value?.let { startDate ->
                _endDate.value?.let { endDate ->

                    dashboardDomainList.value.fetchPlaceVisitedGraphData(
                        Utils.getMonthYearFromCalendarInstance(
                            startDate
                        ), Utils.getMonthYearFromCalendarInstance(endDate)
                    ).toPlaceVisitedGraphUIModel()
                }
            }

            _xValuesList.value = graphDtoList?.map { it.month } ?: emptyList()
            _yValueList.value = graphDtoList?.map { it.count } ?: emptyList()
        }
    }


}