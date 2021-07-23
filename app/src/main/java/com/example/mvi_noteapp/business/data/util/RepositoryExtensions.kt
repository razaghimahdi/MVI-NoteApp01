package com.example.mvi_noteapp.business.data.util

import android.util.Log
import com.example.mvi_noteapp.business.data.cache.CacheConstant.CACHE_TIMEOUT
import com.example.mvi_noteapp.business.data.cache.CacheError.CACHE_ERROR_TIMEOUT
import com.example.mvi_noteapp.business.data.cache.CacheError.CACHE_ERROR_UNKNOWN
import com.example.mvi_noteapp.business.data.cache.CacheResult
import com.example.mvi_noteapp.business.data.network.ApiResult
import com.example.mvi_noteapp.business.data.network.NetworkConstants.NETWORK_TIMEOUT
import com.example.mvi_noteapp.business.data.network.NetworkErrors.NETWORK_ERROR_TIMEOUT
import com.example.mvi_noteapp.business.data.network.NetworkErrors.NETWORK_ERROR_UNKNOWN
import com.example.mvi_noteapp.business.data.util.GenericErrors.ERROR_UNKNOWN
import com.example.mvi_noteapp.util.cLog
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.io.IOException

/**
 * Reference: https://medium.com/@douglas.iacovelli/how-to-handle-errors-with-retrofit-and-coroutines-33e7492a912
 */


/**these functions basically are the same, one of them is just error handler for cache request,
 * the other one is for retrofit,both tasks are the same */

private val TAG="appdebug"

suspend fun <T> safeApiCall(
    dispatcher: CoroutineDispatcher,
    apiCall: suspend () -> T?
): ApiResult<T?> {
    return withContext(dispatcher) {
        try {
            // throws TimeoutCancellationException
            withTimeout(NETWORK_TIMEOUT){
                Log.i(TAG, "####### safeApiCall Success")
                ApiResult.Success(apiCall.invoke())
            }
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
            cLog(throwable.message)
            when (throwable) {
                is TimeoutCancellationException -> {
                    val code = 408 // timeout error code
                    Log.i(TAG, "####### safeApiCall Throwable: TimeoutCancellationException,code:$code")
                    ApiResult.GenericError(code, NETWORK_ERROR_TIMEOUT)
                }
                is IOException -> {
                    Log.i(TAG, "####### safeApiCall Throwable: IOException")
                    ApiResult.NetworkError
                }
                is HttpException -> {
                    val code = throwable.code()
                    Log.i(TAG, "####### safeApiCall Throwable: HttpException,code:$code")
                    val errorResponse = convertErrorBody(throwable)
                    ApiResult.GenericError(
                        code,
                        errorResponse
                    )
                }
                else -> {
                    Log.i(TAG, "####### safeApiCall Throwable: else...")
                    ApiResult.GenericError(
                        null,
                        NETWORK_ERROR_UNKNOWN
                    )
                }
            }
        }
    }
}

suspend fun <T> safeCacheCall(
    dispatcher: CoroutineDispatcher,
    cacheCall: suspend () -> T?
): CacheResult<T?> {
    return withContext(dispatcher) {
        try {
            // throws TimeoutCancellationException
            withTimeout(CACHE_TIMEOUT){
                CacheResult.Success(cacheCall.invoke())
            }
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
            cLog(throwable.message)
            when (throwable) {

                is TimeoutCancellationException -> {
                    CacheResult.GenericError(CACHE_ERROR_TIMEOUT)
                }
                else -> {
                    CacheResult.GenericError(CACHE_ERROR_UNKNOWN)
                }
            }
        }
    }
}

private fun convertErrorBody(throwable: HttpException): String? {
    return try {
        throwable.response()?.errorBody()?.string()
    } catch (exception: Exception) {
        ERROR_UNKNOWN
    }
}











