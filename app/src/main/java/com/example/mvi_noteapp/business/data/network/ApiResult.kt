package com.example.mvi_noteapp.business.data.network

sealed class ApiResult<out T> {

    /**a wrapper class for result we get from network,when we make request to the network*/

    data class Success<out T>(val value: T): ApiResult<T>()

    data class GenericError(
        val code: Int? = null,/**there is an http request then there is a code*/
        val errorMessage: String? = null
    ): ApiResult<Nothing>()

    object NetworkError: ApiResult<Nothing>()
}