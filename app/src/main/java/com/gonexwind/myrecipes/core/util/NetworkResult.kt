package com.gonexwind.myrecipes.core.util

sealed class NetworkResult<T>(
    val data: T? = null,
    val message: String? = null
) {

    class Error<T>(message: String?, data: T? = null) : NetworkResult<T>(data, message)
    class Success<T>(data: T) : NetworkResult<T>(data)
    class Loading<T> : NetworkResult<T>()
}
