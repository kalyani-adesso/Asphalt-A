package com.asphalt.joinaride.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asphalt.android.model.APIResult
import com.asphalt.android.repository.rides.RidesRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
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

    private val _uiEvent = MutableSharedFlow<UiEvent>()   // one-time events (toast)
    val uiEvent: SharedFlow<UiEvent> = _uiEvent
    fun setRating(r: Int) {
        if (r in 0..5) _rating.value = r
    }
    fun setComments(value: String) {
        _comments.value = value
    }
    fun submitFeedback(rideId: String, userId: String) {
        //call api
        viewModelScope.launch {
            // simulate API / database call
            delay(1000)
           val result = ridesRepository.rateYourRide(rideId = rideId,
                userId = userId,
                stars = _rating.value,
               comments = _comments.value
           )
            // emit success event
            _uiEvent.emit(UiEvent.ShowToast("Feedback submitted successfully!"))
            _ratingState.value = result
        }
        _isSubmitted.value = true
    }
    sealed class UiEvent {
        data class ShowToast(val message: String) : UiEvent()
    }
}