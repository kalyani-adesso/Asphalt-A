package com.asphalt.joinaride.viewmodel

import androidx.compose.material3.Text
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asphalt.android.model.APIResult
import com.asphalt.android.repository.rides.RidesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RatingViewModel(val ridesRepository: RidesRepository) : ViewModel() {
    //rating from 0..5
    private val _rating = MutableStateFlow(0)
    val rating : StateFlow<Int> = _rating

    // Comments
    private val _comments = MutableStateFlow("")
    val comments: StateFlow<String> = _comments

    private val _isSubmitted = MutableStateFlow(false)
    val isSumitted : StateFlow<Boolean> = _isSubmitted

    private val _ratingState = MutableStateFlow<APIResult<Unit>?>(null)
    val ratingState: StateFlow<APIResult<Unit>?> = _ratingState

    fun setRating(r: Int) {
        if (r in 0..5) _rating.value = r
    }

    fun setComments(value: String) {
        _comments.value = value
    }

    fun submit(rideId: String, userId: String) {
        //call api
        viewModelScope.launch {
           val result = ridesRepository.rateYourRide(rideId = rideId,
                userId = userId,
                stars = _rating.value,
               comments = _comments.value
           )
            _ratingState.value = result
        }
        _isSubmitted.value = true
    }
    fun reset() {
        _rating.value = 0
        _isSubmitted.value = false
    }
}