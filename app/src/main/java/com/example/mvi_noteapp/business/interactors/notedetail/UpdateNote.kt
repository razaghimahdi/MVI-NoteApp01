package com.example.mvi_noteapp.business.interactors.notedetail

import com.example.mvi_noteapp.business.data.cache.CacheResponseHandler
import com.example.mvi_noteapp.business.data.cache.abstraction.NoteCacheDataSource
import com.example.mvi_noteapp.business.data.network.abstraction.NoteNetworkDataSource
import com.example.mvi_noteapp.business.data.util.safeApiCall
import com.example.mvi_noteapp.business.data.util.safeCacheCall
import com.example.mvi_noteapp.business.domain.model.Note
import com.example.mvi_noteapp.business.domain.state.*
import com.example.mvi_noteapp.framework.presentation.notedetail.state.NoteDetailViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UpdateNote(
    private val noteCacheDataSource: NoteCacheDataSource,
    private val noteNetworkDataSource: NoteNetworkDataSource
) {


    fun updateNote(
        note:Note,
        stateEvent: StateEvent
    ):Flow<DataState<NoteDetailViewState>?> = flow {

        val cacheResult = safeCacheCall(IO){
            noteCacheDataSource.updateNote(
                primary = note.id,
                newTitle = note.title,
                newBody = note.body
            )
        }

        val response = object: CacheResponseHandler<NoteDetailViewState,Int>(
            response=cacheResult,
            stateEvent=stateEvent
        ){
            override suspend fun handleSuccess(resultObj: Int): DataState<NoteDetailViewState>? {
                return if (resultObj > 0){
                    DataState.data(
                        response = Response(
                            message = UPDATE_NOTE_SUCCESS,
                            uiComponentType = UIComponentType.Toast(),
                            messageType = MessageType.Success()
                        )
                    ,data = null,
                        stateEvent = stateEvent
                    )
                }else{

                    DataState.data(
                        response = Response(
                            message = UPDATE_NOTE_FAILED,
                            uiComponentType = UIComponentType.Toast(),
                            messageType = MessageType.Error()
                        )
                        ,data = null,
                        stateEvent = stateEvent
                    )
                }
            }
        }.getResult()


        emit(response)

        updateNetwork(response?.stateMessage?.response?.message,note)

    }



    private suspend fun updateNetwork(response: String?, note: Note) {
        if(response.equals(UPDATE_NOTE_SUCCESS)){

            safeApiCall(Dispatchers.IO){
                noteNetworkDataSource.insertOrUpdateNote(note)
            }
        }
    }


    companion object{
        val UPDATE_NOTE_SUCCESS = "Successfully updated note."
        val UPDATE_NOTE_FAILED = "Failed to update note."
        val UPDATE_NOTE_FAILED_PK = "Update failed. Note is missing primary key."

    }


}