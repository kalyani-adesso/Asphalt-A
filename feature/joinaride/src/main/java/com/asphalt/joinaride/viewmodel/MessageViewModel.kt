package com.asphalt.joinaride.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asphalt.android.model.APIResult
import com.asphalt.android.model.message.MessageRoot
import com.asphalt.android.repository.rides.RidesRepository
import com.asphalt.joinaride.message.MessageDriverEvent
import com.asphalt.joinaride.message.MessageDriverUiState
import com.asphalt.joinaride.repository.IdRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MessageViewModel : ViewModel() {

}