package com.example.mvi_noteapp.business.data.cache.implemention

import com.example.mvi_noteapp.framework.datasource.cache.abstraction.NoteDaoService
import com.example.mvi_noteapp.business.data.cache.abstraction.NoteCacheDataSource
import com.example.mvi_noteapp.business.domain.model.Note
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteCacheDataSourceImpl
@Inject
constructor(
    private val noteDaoService: NoteDaoService
    /**this is  a layer between sqlite and NoteCacheDataSource*/
): NoteCacheDataSource {

    override suspend fun insertNote(note: Note): Long {
        return noteDaoService.insertNote(note)
    }

    override suspend fun deleteNote(primaryKey: String): Int {
        return noteDaoService.deleteNote(primaryKey)
    }

    override suspend fun deleteNotes(notes: List<Note>): Int {
        return noteDaoService.deleteNotes(notes)
    }

    override suspend fun updateNote(
        primaryKey: String,
        newTitle: String,
        newBody: String?
    ): Int {
        return noteDaoService.updateNote(primaryKey, newTitle, newBody)
    }

    override suspend fun searchNotes(
        query: String,
        filterAndOrder: String,
        page: Int
    ): List<Note> {
        TODO("Check filterAndOrder and make query")
    }

    override suspend fun searchNoteById(id: String): Note? {
        return noteDaoService.searchNoteById(id)
    }

    override suspend fun getNumNotes(): Int {
        return noteDaoService.getNumNotes()
    }

    override suspend fun insertNotes(notes: List<Note>): LongArray{
        return noteDaoService.insertNotes(notes)
    }
}