package com.asphalt.android

import com.asphalt.android.model.APIResult

inline fun <T, R> APIResult<T>.mapApiResult(
    transform: (T) -> R
): APIResult<R> {
    return when (this) {
        is APIResult.Success -> APIResult.Success(transform(data))
        is APIResult.Error -> APIResult.Error(exception, code)
    }
}