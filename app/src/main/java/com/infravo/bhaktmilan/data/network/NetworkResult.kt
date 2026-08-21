package com.infravo.bhaktmilan.data.network

sealed class NetworkResult<out T> {

    data class Success<T>(
        val data: T
    ) : NetworkResult<T>()

    data class Error(
        val code: Int? = null,
        val message: String
    ) : NetworkResult<Nothing>()

    data object Loading : NetworkResult<Nothing>()
}