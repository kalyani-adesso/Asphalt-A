package com.asphalt.joinaride.viewmodel

import androidx.compose.material3.Text
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class RatingViewModel : ViewModel() {
    //rating from 0..5
    private val _rating = MutableStateFlow(3)
    val rating : StateFlow<Int> = _rating

    private val _isSubmitted = MutableStateFlow(false)
    val isSumitted : StateFlow<Boolean> = _isSubmitted

    fun setRating(r: Int) {
        if (r in 0..5) _rating.value = r
    }

    fun submit() {
        //call api
        _isSubmitted.value = true
    }
    fun reset() {
        _rating.value = 0
        _isSubmitted.value = false
    }
}