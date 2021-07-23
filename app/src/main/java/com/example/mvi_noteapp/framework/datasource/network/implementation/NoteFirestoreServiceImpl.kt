package com.example.mvi_noteapp.framework.datasource.network.implementation

import com.example.mvi_noteapp.business.domain.model.Note
import com.example.mvi_noteapp.framework.datasource.network.abstraction.NoteFirestoreService
import com.example.mvi_noteapp.framework.datasource.network.mapper.NetworkMapper
import com.example.mvi_noteapp.framework.datasource.network.model.NoteNetworkEntity
import com.example.mvi_noteapp.util.cLog
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteFirestoreServiceImpl
@Inject
constructor(
    private val firebaseAuth:FirebaseAuth,
    private val firestore:FirebaseFirestore,
    private val networkMapper: NetworkMapper
):NoteFirestoreService{

    override suspend fun insertOrUpdateNote(note: Note) {
        val entity = networkMapper.mapToEntity(note)
        entity.updated_at = Timestamp.now() // for updates
        firestore
            .collection(NOTES_COLLECTION)
            .document(USER_ID)
            .collection(NOTES_COLLECTION)
            .document(entity.id)
            .set(entity)
            .addOnFailureListener {
                cLog(it.message)
            }
            .await()
    }

    override suspend fun deleteNote(primaryKey: String) {
        firestore
            .collection(NOTES_COLLECTION)
            .document(USER_ID)
            .collection(NOTES_COLLECTION)
            .document(primaryKey)
            .delete()
            .addOnFailureListener {
                cLog(it.message)
            }
            .await()
    }

    override suspend fun insertDeletedNote(note: Note) {
        val entity = networkMapper.mapToEntity(note)
        firestore
            .collection(DELETES_COLLECTION)
            .document(USER_ID)
            .collection(NOTES_COLLECTION)
            .document(entity.id)
            .set(entity)
            .addOnFailureListener {
                cLog(it.message)
            }
            .await()
    }

    override suspend fun insertDeletedNotes(notes: List<Note>) {
        if(notes.size > 500){
            throw Exception("Cannot delete more than 500 notes at a time in firestore.")
        }

        val collectionRef = firestore
            .collection(DELETES_COLLECTION)
            .document(USER_ID)
            .collection(NOTES_COLLECTION)

        firestore.runBatch { batch ->
            for(note in notes){
                val documentRef = collectionRef.document(note.id)
                batch.set(documentRef, networkMapper.mapToEntity(note))
            }
        }
            .addOnFailureListener {
                cLog(it.message)
            }.await()
    }

    override suspend fun deleteDeletedNote(note: Note) {
        val entity = networkMapper.mapToEntity(note)
        firestore
            .collection(DELETES_COLLECTION)
            .document(USER_ID)
            .collection(NOTES_COLLECTION)
            .document(entity.id)
            .delete()
            .addOnFailureListener {
                cLog(it.message)
            }
            .await()
    }

    // used in testing
    override suspend fun deleteAllNotes() {
        firestore
            .collection(NOTES_COLLECTION)
            .document(USER_ID)
            .delete()
            .await()
        firestore
            .collection(DELETES_COLLECTION)
            .document(USER_ID)
            .delete()
            .addOnFailureListener {
                cLog(it.message)
            }
            .await()
    }

    override suspend fun getDeletedNotes(): List<Note> {
        return networkMapper.entityListToNoteList(
            firestore
                .collection(DELETES_COLLECTION)
                .document(USER_ID)
                .collection(NOTES_COLLECTION)
                .get()
                .addOnFailureListener {
                    cLog(it.message)
                }
                .await().toObjects(NoteNetworkEntity::class.java)
        )
    }

    override suspend fun searchNote(note: Note): Note? {
        return firestore
            .collection(NOTES_COLLECTION)
            .document(USER_ID)
            .collection(NOTES_COLLECTION)
            .document(note.id)
            .get()
            .addOnFailureListener {
                cLog(it.message)
            }
            .await()
            .toObject(NoteNetworkEntity::class.java)?.let {
                networkMapper.mapFromEntity(it)
            }
    }

    override suspend fun getAllNotes(): List<Note> {
        return networkMapper.entityListToNoteList(
            firestore
                .collection(NOTES_COLLECTION)
                .document(USER_ID)
                .collection(NOTES_COLLECTION)
                .get()
                .addOnFailureListener {
                    cLog(it.message)
                }
                .await()
                .toObjects(NoteNetworkEntity::class.java)
        )
    }

    override suspend fun insertOrUpdateNotes(notes: List<Note>) {

        if(notes.size > 500){
            throw Exception("Cannot insert more than 500 notes at a time into firestore.")
        }

        val collectionRef = firestore
            .collection(NOTES_COLLECTION)
            .document(USER_ID)
            .collection(NOTES_COLLECTION)

        firestore.runBatch { batch ->
            for(note in notes){
                val entity = networkMapper.mapToEntity(note)
                entity.updated_at = Timestamp.now()
                val documentRef = collectionRef.document(note.id)
                batch.set(documentRef, entity)
            }
        }
            .addOnFailureListener {
                cLog(it.message)
            }.await()

    }


    companion object {
        const val NOTES_COLLECTION = "notes"
        const val USERS_COLLECTION = "users"
        const val DELETES_COLLECTION = "deletes"
        const val USER_ID = ""
        const val EMAIL = ""
    }

}