package com.example.mvi_noteapp.business.interactors.notelist

import com.example.mvi_noteapp.business.data.cache.CacheResponseHandler
import com.example.mvi_noteapp.business.data.cache.abstraction.NoteCacheDataSource
import com.example.mvi_noteapp.business.data.util.safeCacheCall
import com.example.mvi_noteapp.business.domain.state.*
import com.example.mvi_noteapp.framework.presentation.notelist.state.NoteListViewState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


/**this class is for paging*/

class GetNumNotes(
    private val noteCacheDataSource: NoteCacheDataSource
) {


    fun getNumNotes(
        stateEvent: StateEvent
    ):Flow<DataState<NoteListViewState>?> = flow {

        val cacheResult = safeCacheCall(IO){
            noteCacheDataSource.getNumNotes()
        }


        val response=object:CacheResponseHandler<NoteListViewState,Int>(
            response=cacheResult,
            stateEvent=stateEvent
        ){
            override suspend fun handleSuccess(resultObj: Int): DataState<NoteListViewState>? {

                val viewState = NoteListViewState(
                    numNotesInCache = resultObj
                )

                return DataState.data(
                    response = Response(
                        message = GET_NUM_NOTES_SUCCESS,
                        uiComponentType = UIComponentType.None(),
                        messageType = MessageType.Success()
                    ),
                    data = viewState,
                    stateEvent=stateEvent
                )


            }
        }.getResult()

        emit(response)



    }


    companion object{
        val GET_NUM_NOTES_SUCCESS = "Successfully retrieved the number of notes from the cache."
        val GET_NUM_NOTES_FAILED = "Failed to get the number of notes from the cache."
    }


}