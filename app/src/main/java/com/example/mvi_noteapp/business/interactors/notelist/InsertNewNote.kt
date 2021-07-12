package com.example.mvi_noteapp.business.interactors.notelist

import com.example.mvi_noteapp.business.data.cache.CacheResponseHandler
import com.example.mvi_noteapp.business.data.cache.abstraction.NoteCacheDataSource
import com.example.mvi_noteapp.business.data.network.abstraction.NoteNetworkDataSource
import com.example.mvi_noteapp.business.data.util.safeApiCall
import com.example.mvi_noteapp.business.data.util.safeCacheCall
import com.example.mvi_noteapp.business.domain.model.Note
import com.example.mvi_noteapp.business.domain.model.NoteFactory
import com.example.mvi_noteapp.business.domain.state.*
import com.example.mvi_noteapp.framework.presentation.notelist.state.NoteListViewState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.*

class InsertNewNote(
    private val noteCacheDataSource: NoteCacheDataSource,
    private val noteNetworkDataSource: NoteNetworkDataSource,
    private val noteFactory: NoteFactory
) {



    fun insertNewNote(
        id: String? = null,
        title: String,
        stateEvent: StateEvent
    ): Flow<DataState<NoteListViewState>?> = flow {

        val newNote = noteFactory.createSingleNote(
            id = id ?: UUID.randomUUID().toString(),
            title = title,
            body = ""
        )
        val cacheResult = safeCacheCall(IO){
            noteCacheDataSource.insertNote(newNote)
        }

        val cacheResponse = object :  CacheResponseHandler<NoteListViewState, Long>(
            response = cacheResult,
            stateEvent=stateEvent
        ) {
            override suspend fun handleSuccess(resultObj: Long): DataState<NoteListViewState>? {

              return  if(resultObj > 0){
                    val viewState =
                        NoteListViewState(
                            newNote = newNote
                        )
                   DataState.data(
                        response = Response(
                            message = INSERT_NOTE_SUCCESS,
                            uiComponentType = UIComponentType.Toast(),
                            messageType = MessageType.Success()
                        ),
                        data = viewState,
                        stateEvent = stateEvent
                    )
                }
                else{
                    DataState.data(
                        response = Response(
                            message = INSERT_NOTE_FAILED,
                            uiComponentType = UIComponentType.Toast(),
                            messageType = MessageType.Error()
                        ),
                        data = null,
                        stateEvent = stateEvent
                    )
                }

            }
        }.getResult()

        emit(cacheResponse)

        updateNetwork(cacheResponse?.stateMessage?.response?.message, newNote)
    }

    private suspend fun updateNetwork(cacheResponse: String?, newNote: Note){
        if(cacheResponse.equals(INSERT_NOTE_SUCCESS)){

           safeApiCall(IO){
               noteNetworkDataSource.insertOrUpdateNote(newNote)
           }
        }
    }

    companion object{
        val INSERT_NOTE_SUCCESS = "Successfully inserted new note."
        val INSERT_NOTE_FAILED = "Failed to insert new note."
    }



}