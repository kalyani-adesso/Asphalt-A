package com.asphalt.registration.repository

sealed class RepositoryResult<out T> {

    data class Success<T>(val data: T) : RepositoryResult<T>()
    data class Error<T>(val message: String) : RepositoryResult<Nothing>()
}