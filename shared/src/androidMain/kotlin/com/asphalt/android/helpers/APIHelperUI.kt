package com.asphalt.android.helpers

import com.asphalt.android.constants.APIConstants.GENERIC_ERROR_MSG
import com.asphalt.android.model.APIResult
import com.asphalt.commonui.UIState
import com.asphalt.commonui.UIStateHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

object APIHelperUI {
    private val NO_RESPONSE_ERROR = APIResult.Error(Exception("Data not found!"))
    inline fun <reified T> handleApiResult(
        apiResult: APIResult<T>,
        scope: CoroutineScope,
        showError: Boolean = true,
        onSuccess: (data: T) -> Unit
    ) {
        when (apiResult) {
            is APIResult.Error -> {
                val uIStateError =
                    UIState.Error(apiResult.exception.message ?: GENERIC_ERROR_MSG)
                if (showError)
                    scope.launch {
                        UIStateHandler.sendEvent(uIStateError)
                    }

            }

            is APIResult.Success -> apiResult.data?.let {
                onSuccess.invoke(it)
            }
        }
    }

    suspend fun <T> runWithLoader(apiCall: suspend () -> APIResult<T>?): APIResult<T> {
        UIStateHandler.sendEvent(UIState.Loading)
        return try {
            apiCall() ?: NO_RESPONSE_ERROR
        } finally {
            UIStateHandler.sendEvent(UIState.DismissLoader)
        }
    }
}
