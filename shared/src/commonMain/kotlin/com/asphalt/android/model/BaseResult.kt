package com.asphalt.android.model

// In your domain or common module (e.g., com.asphalt.android.domain)
sealed interface APIResult<out T> {

    /**
     * Represents a successful outcome with data of type T.
     * @param data The non-null result of the operation.
     */
    data class Success<out T>(val data: T) : APIResult<T>

    /**
     * Represents a failed outcome.
     * @param exception The Throwable representing the failure (e.g., IOException, ClientRequestException).
     * @param code The optional HTTP status code if the failure was network-related (e.g., 404, 500).
     */
    data class Error(val exception: Throwable, val code: Int? = null) : APIResult<Nothing>
}