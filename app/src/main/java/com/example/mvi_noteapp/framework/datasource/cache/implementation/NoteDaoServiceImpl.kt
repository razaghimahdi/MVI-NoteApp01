package com.example.mvi_noteapp.framework.datasource.cache.implementation

import com.example.mvi_noteapp.business.domain.model.Note
import com.example.mvi_noteapp.business.domain.util.DateUtil
import com.example.mvi_noteapp.framework.datasource.cache.abstraction.NoteDaoService
import com.example.mvi_noteapp.framework.datasource.cache.database.NoteDao
import com.example.mvi_noteapp.framework.datasource.cache.database.returnOrderedQuery
import com.example.mvi_noteapp.framework.datasource.cache.mappers.CacheMapper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteDaoServiceImpl/**by dagger, this class going to pass to NoteCacheDataSourceImpl*/
@Inject
constructor(
    private val noteDao:NoteDao,
    private val noteMapper: CacheMapper,
    private val dateUtil:DateUtil
):NoteDaoService{

    override suspend fun insertNote(note: Note): Long {
        return noteDao.insertNote(noteMapper.mapToEntity(note))
    }

    override suspend fun insertNotes(notes: List<Note>): LongArray {
        return noteDao.insertNotes(
            noteMapper.noteListToEntityList(notes)
        )
    }

    override suspend fun searchNoteById(id: String): Note? {
        return noteDao.searchNoteById(id)?.let { noteCacheEntity->
            noteMapper.mapFromEntity(noteCacheEntity)
        }
    }

    override suspend fun updateNote(primaryKey: String, title: String, body: String?): Int {
        return noteDao.updateNote(
            primaryKey=primaryKey,
            title = title,
            body = body,
            updated_at = dateUtil.getCurrentTimestamp()
        )
    }

    override suspend fun deleteNote(primaryKey: String): Int {
        return noteDao.deleteNote(primaryKey)
    }

    override suspend fun deleteNotes(notes: List<Note>): Int {
        val ids = notes.mapIndexed { index, note -> note.id }
        return noteDao.deleteNotes(ids)
    }

    override suspend fun getAllNotes(): List<Note> {
        return noteMapper.entityListToNoteList(
            noteDao.searchNotes()
        )
    }

    override suspend fun searchNotes(): List<Note> {
        return noteMapper.entityListToNoteList(
            noteDao.searchNotes()
        )
    }

    override suspend fun searchNotesOrderByDateDESC(
        query: String,
        page: Int,
        pageSize: Int
    ): List<Note> {
        return noteMapper.entityListToNoteList(
            noteDao.searchNotesOrderByDateDESC(
                query=query,
                page=page,
                pageSize = pageSize
            )
        )
    }

    override suspend fun searchNotesOrderByDateASC(
        query: String,
        page: Int,
        pageSize: Int
    ): List<Note> {
        TODO("Not yet implemented")
    }

    override suspend fun searchNotesOrderByTitleDESC(
        query: String,
        page: Int,
        pageSize: Int
    ): List<Note> {
        return noteMapper.entityListToNoteList(
            noteDao.searchNotesOrderByTitleDESC(
                query=query,
                page=page,
                pageSize = pageSize
            )
        )
    }

    override suspend fun searchNotesOrderByTitleASC(
        query: String,
        page: Int,
        pageSize: Int
    ): List<Note> {
        return noteMapper.entityListToNoteList(
            noteDao.searchNotesOrderByTitleASC(
                query=query,
                page=page,
                pageSize = pageSize
            )
        )
    }

    override suspend fun getNumNotes(): Int {
        return noteDao.getNumNotes()
    }

    override suspend fun returnOrderedQuery(
        query: String,
        filterAndOrder: String,
        page: Int
    ): List<Note> {
        return noteMapper.entityListToNoteList(
            noteDao.returnOrderedQuery(
                query=query,
                filterAndOrder=filterAndOrder,
                page=page
            )
        )
    }

}