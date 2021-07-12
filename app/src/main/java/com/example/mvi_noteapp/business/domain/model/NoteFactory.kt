package com.example.mvi_noteapp.business.domain.model

import com.example.mvi_noteapp.business.domain.util.DateUtil
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.ArrayList



/**a class to create note*/
@Singleton
class NoteFactory
@Inject
constructor(
    private val dateUtil: DateUtil
) {

    /**we need for step one to send title first*/
    fun createSingleNote(
        id:String?=null,
        title:String,
        body:String?=null
    ):Note{
        return Note(
            id= id ?: UUID.randomUUID().toString(),
            title=title,
            body=body?:"",
            created_at = dateUtil.getCurrentTimestamp(),
            updated_at = dateUtil.getCurrentTimestamp(),
        )
    }


    /**this fun is for testing*/
    fun createNoteList(numNotes:Int):List<Note>{
        val list:ArrayList<Note> = ArrayList()
        for ( i in 0 until  numNotes){
            list.add(
                createSingleNote(
                    id=null,
                    title = UUID.randomUUID().toString(),
                    body = UUID.randomUUID().toString(),
                )
            )
        }
        return list
    }


}