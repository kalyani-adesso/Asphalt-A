package com.asphalt.joinaride.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asphalt.android.model.APIResult
import com.asphalt.android.repository.rides.RidesRepository
import com.asphalt.android.viewmodels.AndroidUserVM
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.java.KoinJavaComponent.inject
import kotlin.getValue

class RatingViewModel(val ridesRepository: RidesRepository) : ViewModel(), KoinComponent {
    //rating from 0..5
    private val _rating = MutableStateFlow(0)
    val rating : StateFlow<Int> = _rating
    val androidUserVM: AndroidUserVM by inject()

    private val currentUid = androidUserVM.userState.value?.uid

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
    fun submitRating(rideId: String, userId: String) {
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

    fun updateRateStatus(rideId: String,stars: Int) {
        viewModelScope.launch {
            val res = ridesRepository.updateRatings(rideID = rideId, userID = currentUid ?: "", stars = _rating.value)
            Log.d("TAG", "updateRateStatus: ${res.toString()}")
        }
    }
    sealed class UiEvent {
        data class ShowToast(val message: String) : UiEvent()
    }
}