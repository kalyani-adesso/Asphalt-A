package com.asphalt.dashboard.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asphalt.commonui.constants.Constants
import com.asphalt.commonui.utils.Utils
import com.asphalt.dashboard.data.PlacesVisitedGraphDto
import com.asphalt.dashboard.data.PlacesVisitedGraphData
import com.asphalt.dashboard.repository.PlaceVisitedGraphRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.DateFormatSymbols
import java.util.Calendar

class PlacesVisitedGraphViewModel(val placeVisitedGraphRepo: PlaceVisitedGraphRepo) : ViewModel() {

    private val _startDate = MutableStateFlow<Calendar?>(null)
    private val calendarOffset = Constants.NO_OF_MONTHS_PLACE_VISITED - 1
    private val _xValuesList = MutableStateFlow<List<String>>(emptyList())
    private val _yValueList = MutableStateFlow<List<Int>>(emptyList())
    private val _isArrowEnabled = MutableStateFlow(false)
    val startDate: StateFlow<Calendar?> = _startDate
    private val _endDate = MutableStateFlow<Calendar?>(null)
    val endDate: StateFlow<Calendar?> = _endDate
    val xValuesList: StateFlow<List<String>> = _xValuesList
    val yValueList: StateFlow<List<Int>> = _yValueList
    val isArrowEnabled: StateFlow<Boolean> = _isArrowEnabled


    init {
        _endDate.value = Calendar.getInstance()
        _startDate.value = (_endDate.value?.clone() as Calendar).apply {
            add(
                Calendar.MONTH,
                -calendarOffset
            )
        }
        fetchPlacesVisitedGraphData()
    }

    fun setArrowDisabledOrEnabled(inputCalendar: Calendar) {
        val current = Calendar.getInstance()
        inputCalendar.get(Calendar.YEAR) < current.get(Calendar.YEAR) ||
                (inputCalendar.get(Calendar.YEAR) == current.get(Calendar.YEAR) &&
                        inputCalendar.get(Calendar.MONTH) < current.get(Calendar.MONTH)).also {
                    _isArrowEnabled.value = it
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
        _endDate.value?.let { setArrowDisabledOrEnabled(it) }
        viewModelScope.launch {
            val graphDtoList = _startDate.value?.let { startDate ->
                _endDate.value?.let { endDate ->

                    placeVisitedGraphRepo.fetchPlaceVisitedGraphData(
                        Utils.getMonthYearFromCalendarInstance(
                            startDate
                        ), Utils.getMonthYearFromCalendarInstance(endDate)
                    )
                }
            }

            _xValuesList.value = graphDtoList?.map { it.month } ?: emptyList()
            _yValueList.value = graphDtoList?.map { it.count } ?: emptyList()
        }
    }



}