package com.ceac.mvvmapp.data.util

sealed class NetworkResult<out T> {
    data class Success<T>(val data: T): NetworkResult<T>()
    data class ApiError(val code: Int, val message: String?, val body: String?): NetworkResult<Nothing>()
    object NetworkError: NetworkResult<Nothing>()
    data class UnknownError(val throwable: Throwable): NetworkResult<Nothing>()
}
