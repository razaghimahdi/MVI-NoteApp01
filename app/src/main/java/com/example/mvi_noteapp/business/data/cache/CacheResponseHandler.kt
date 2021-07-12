package com.example.mvi_noteapp.business.data.cache

import com.example.mvi_noteapp.business.data.cache.CacheError.CACHE_DATA_NULL
import com.example.mvi_noteapp.business.domain.state.*


/**a class to wrap ot pare cacheResult to dataState*/

abstract class CacheResponseHandler <ViewState, Data>(
    private val response: CacheResult<Data?>,/**so it gonna take a cache result as input and that result going to parse to DataState(#1)*/
    private val stateEvent: StateEvent?
){
    suspend fun getResult(): DataState<ViewState>?/**(#1)*/ {

        return when(response){

            is CacheResult.GenericError -> {
                DataState.error(
                    response = Response(
                        message = "${stateEvent?.errorInfo()}\n\nReason: ${response.errorMessage}",
                        uiComponentType = UIComponentType.Dialog(),
                        messageType = MessageType.Error()
                    ),
                    stateEvent = stateEvent
                )
            }

            is CacheResult.Success -> {
                if(response.value == null){
                    DataState.error(
                        response = Response(
                            message = "${stateEvent?.errorInfo()}\n\nReason: ${CACHE_DATA_NULL}.",
                            uiComponentType = UIComponentType.Dialog(),
                            messageType = MessageType.Error()
                        ),
                        stateEvent = stateEvent
                    )
                }
                else{
                    handleSuccess(resultObj = response.value)
                }
            }

        }
    }

    abstract suspend fun handleSuccess(resultObj: Data): DataState<ViewState>?

}