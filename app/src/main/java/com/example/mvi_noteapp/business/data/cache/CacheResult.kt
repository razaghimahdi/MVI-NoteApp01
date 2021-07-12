package com.example.mvi_noteapp.business.data.cache


/**a wrapper class for result we get from cache,when we make request to the cache*/

sealed class CacheResult <out T> {

    data class Success<out T>(val value: T) :CacheResult<T>()

    data class GenericError(
        val errorMessage:String? = null
    ):CacheResult<Nothing>()

}