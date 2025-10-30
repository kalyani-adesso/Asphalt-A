package com.asphalt.commonui

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

sealed interface UIState {
    object Loading : UIState
    object DismissLoader : UIState
    data class Error(val errorMsg: String, val type: BannerType = BannerType.ERROR) : UIState
    data class SUCCESS(val successMsg: String, val type: BannerType = BannerType.SUCCESS) : UIState
}

object UIStateHandler {
    private val _event = MutableSharedFlow<UIState>()
    val event = _event.asSharedFlow()
    suspend fun sendEvent(state: UIState) {
        _event.emit(state)
    }


}