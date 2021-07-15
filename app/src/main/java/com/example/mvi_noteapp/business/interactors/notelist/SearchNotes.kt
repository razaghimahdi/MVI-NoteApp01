package com.example.mvi_noteapp.business.interactors.notelist

import com.example.mvi_noteapp.business.data.cache.CacheResponseHandler
import com.example.mvi_noteapp.business.data.cache.abstraction.NoteCacheDataSource
import com.example.mvi_noteapp.business.data.util.safeCacheCall
import com.example.mvi_noteapp.business.domain.model.Note
import com.example.mvi_noteapp.business.domain.state.*
import com.example.mvi_noteapp.framework.presentation.notelist.state.NoteListViewState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchNotes (
    private val noteCacheDataSource: NoteCacheDataSource/**the reason that we have only NoteCacheDataSource and not NoteNetworkDataSource
    is because searching not required network the way we do search it is always hits the cache*/
        ){


    fun searchNotes(
        query:String,
        filterAndOrder:String,
        page:Int,
        stateEvent:StateEvent
    ): Flow<DataState<NoteListViewState>?> =flow{
        var updatedPage=page
        if (page<=0){
            updatedPage=1
        }

        val cacheResult= safeCacheCall(IO){
            noteCacheDataSource.searchNotes(
                query=query,
                filterAndOrder = filterAndOrder,
                page=updatedPage
            )
        }


        val response = object : CacheResponseHandler<NoteListViewState, List<Note>>(
            response = cacheResult,
            stateEvent=stateEvent
        ) {
            override suspend fun handleSuccess(resultObj: List<Note>): DataState<NoteListViewState>? {

                var message:String?= SEARCH_NOTES_SUCCESS
                var uiComponentType:UIComponentType=UIComponentType.None()
                if (resultObj.size==0){
                    message= SEARCH_NOTES_NO_MATCHING_RESULTS
                    var uiComponentType:UIComponentType=UIComponentType.Toast()
                }
                return DataState.data(
                    response = Response(
                        message=message,
                        uiComponentType=uiComponentType,
                        messageType = MessageType.Success()
                    ),
                    data = NoteListViewState(
                        noteList = ArrayList(resultObj)
                    ),
                    stateEvent=stateEvent
                )

            }

        }.getResult()


        emit(response)


    }


    companion object{
        val SEARCH_NOTES_SUCCESS = "Successfully retrieved list of notes."
        val SEARCH_NOTES_NO_MATCHING_RESULTS = "There are no notes that match that query."
        val SEARCH_NOTES_FAILED = "Failed to retrieve the list of notes."

    }

}