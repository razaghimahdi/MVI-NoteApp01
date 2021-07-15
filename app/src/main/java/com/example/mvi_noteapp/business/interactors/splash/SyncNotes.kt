package com.example.mvi_noteapp.business.interactors.splash

import com.example.mvi_noteapp.business.data.cache.CacheResponseHandler
import com.example.mvi_noteapp.business.data.cache.abstraction.NoteCacheDataSource
import com.example.mvi_noteapp.business.data.network.ApiResponseHandler
import com.example.mvi_noteapp.business.data.network.abstraction.NoteNetworkDataSource
import com.example.mvi_noteapp.business.data.util.safeApiCall
import com.example.mvi_noteapp.business.data.util.safeCacheCall
import com.example.mvi_noteapp.business.domain.model.Note
import com.example.mvi_noteapp.business.domain.state.DataState
import com.example.mvi_noteapp.business.domain.util.DateUtil
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SyncNotes(
    private val noteCacheDataSource: NoteCacheDataSource,
    private val noteNetworkDataSource: NoteNetworkDataSource
) {

    suspend fun syncNotes() {
        val cacheNotesList=getCachedNotes()

        syncNetworkNotesWithCachedNotes(cacheNotesList as ArrayList<Note>)
    }


    private suspend fun getCachedNotes():List<Note>{/**we just want data*/

        val cacheResult = safeCacheCall(IO){
            noteCacheDataSource.getAllNotes()
        }

        val response = object : CacheResponseHandler<List<Note>, List<Note>>(
            response=cacheResult,
            stateEvent = null
        ) {
            override suspend fun handleSuccess(resultObj: List<Note>): DataState<List<Note>>? {
                return DataState.data(
                    response = null,
                    data = resultObj,
                    stateEvent = null
                )
            }
        }.getResult()


        return response?.data?:ArrayList()
    }


    // get all notes from network
    // if they do not exist in cache, insert them
    // if they do exist in cache, make sure they are up to date
    // while looping, remove notes from the cachedNotes list. If any remain, it means they
    // should be in the network but aren't. So insert them.
    private suspend fun syncNetworkNotesWithCachedNotes(
        cachedNotes:ArrayList<Note>
    )= withContext(IO){

        val networkResult = safeApiCall(IO){
            noteNetworkDataSource.getAllNotes()
        }

        val response = object:ApiResponseHandler<List<Note>,List<Note>>(
            response = networkResult,
            stateEvent = null
        ){
            override suspend fun handleSuccess(resultObj: List<Note>): DataState<List<Note>>? {
                return DataState.data(
                    response = null,
                    data = resultObj,
                    stateEvent = null
                )
            }
        }.getResult()

        val noteList=response?.data?:ArrayList()

        val job = launch {
            for (note in noteList){
                noteCacheDataSource.searchNoteById(note.id)?.let { cachedNote->
                    cachedNotes.remove(cachedNote)
                    checkIfCachedNoteRequiresUpdate(cachedNote,note)
                }?:noteCacheDataSource.insertNote(note)
            }
        }

        job.join()// by this code, we says wait until for loop gets done


        // insert remaining into network
        for (cachedNote in cachedNotes){
            safeApiCall(IO){
                noteNetworkDataSource.insertOrUpdateNote(cachedNote)
            }
        }



    }

    private suspend fun checkIfCachedNoteRequiresUpdate(cachedNote: Note, networkNote: Note) {

        val cachedUpdateAt = cachedNote.updated_at
        val networkUpdateAt = networkNote.updated_at

        if (networkUpdateAt > cachedUpdateAt){
            safeCacheCall(IO){
                noteCacheDataSource.updateNote(
                    primary = networkNote.id,
                    newTitle = networkNote.title,
                    newBody = networkNote.body,
                )
            }
        }else{
            safeApiCall(IO){
                noteNetworkDataSource.insertOrUpdateNote(cachedNote)
            }
        }


    }


}