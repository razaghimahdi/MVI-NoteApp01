package com.example.mvi_noteapp.di

import android.content.SharedPreferences
import com.example.mvi_noteapp.business.data.cache.abstraction.NoteCacheDataSource
import com.example.mvi_noteapp.business.data.cache.implemention.NoteCacheDataSourceImpl
import com.example.mvi_noteapp.business.data.network.abstraction.NoteNetworkDataSource
import com.example.mvi_noteapp.business.domain.model.NoteFactory
import com.example.mvi_noteapp.business.domain.util.DateUtil
import com.example.mvi_noteapp.framework.datasource.cache.database.NoteDao
import com.example.mvi_noteapp.framework.datasource.cache.database.NoteDatabase
import com.example.mvi_noteapp.framework.datasource.cache.mappers.CacheMapper
import com.example.mvi_noteapp.framework.datasource.network.implementation.NoteFirestoreServiceImpl
import com.example.mvi_noteapp.business.data.network.implementation.NoteNetworkDataSourceImpl
import com.example.mvi_noteapp.business.interactors.common.DeleteNote
import com.example.mvi_noteapp.business.interactors.notedetail.NoteDetailInteractors
import com.example.mvi_noteapp.business.interactors.notedetail.UpdateNote
import com.example.mvi_noteapp.business.interactors.notelist.*
import com.example.mvi_noteapp.business.interactors.splash.SyncDeletedNotes
import com.example.mvi_noteapp.business.interactors.splash.SyncNotes
import com.example.mvi_noteapp.framework.datasource.cache.abstraction.NoteDaoService
import com.example.mvi_noteapp.framework.datasource.cache.implementation.NoteDaoServiceImpl
import com.example.mvi_noteapp.framework.datasource.network.abstraction.NoteFirestoreService
import com.example.mvi_noteapp.framework.datasource.network.mapper.NetworkMapper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@FlowPreview
@Module
object AppModule {


    // https://developer.android.com/reference/java/text/SimpleDateFormat.html?hl=pt-br
    @JvmStatic
    @Singleton
    @Provides
    fun provideDateFormat(): SimpleDateFormat {
        val sdf = SimpleDateFormat("yyyy-MM-dd hh:mm:ss a", Locale.ENGLISH)
        sdf.timeZone = TimeZone.getTimeZone("UTC-7") // match firestore
        return sdf
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideDateUtil(dateFormat: SimpleDateFormat): DateUtil {
        return DateUtil(
            dateFormat
        )
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideSharedPrefsEditor(
        sharedPreferences: SharedPreferences
    ): SharedPreferences.Editor {
        return sharedPreferences.edit()
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideNoteFactory(dateUtil: DateUtil): NoteFactory {
        return NoteFactory(
            dateUtil
        )
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideNoteDAO(noteDatabase: NoteDatabase): NoteDao {
        return noteDatabase.noteDao()
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideNoteCacheMapper(dateUtil: DateUtil): CacheMapper {
        return CacheMapper(dateUtil)
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideNoteNetworkMapper(dateUtil: DateUtil): NetworkMapper {
        return NetworkMapper(dateUtil)
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideNoteDaoService(
        noteDao: NoteDao,
        noteEntityMapper: CacheMapper,
        dateUtil: DateUtil
    ): NoteDaoService{
        return NoteDaoServiceImpl(noteDao, noteEntityMapper, dateUtil)
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideNoteCacheDataSource(
        noteDaoService: NoteDaoService
    ): NoteCacheDataSource {
        return NoteCacheDataSourceImpl(noteDaoService)
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideFirestoreService(
        firebaseAuth: FirebaseAuth,
        firebaseFirestore: FirebaseFirestore,
        networkMapper: NetworkMapper
    ): NoteFirestoreService {
        return NoteFirestoreServiceImpl(
            firebaseAuth,
            firebaseFirestore,
            networkMapper
        )
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideNoteNetworkDataSource(
        firestoreService: NoteFirestoreServiceImpl
    ): NoteNetworkDataSource {
        return NoteNetworkDataSourceImpl(
            firestoreService
        )
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideSyncNotes(
        noteCacheDataSource: NoteCacheDataSource,
        noteNetworkDataSource: NoteNetworkDataSource
    ): SyncNotes{
        return SyncNotes(
            noteCacheDataSource,
            noteNetworkDataSource
        )
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideSyncDeletedNotes(
        noteCacheDataSource: NoteCacheDataSource,
        noteNetworkDataSource: NoteNetworkDataSource
    ): SyncDeletedNotes{
        return SyncDeletedNotes(
            noteCacheDataSource,
            noteNetworkDataSource
        )
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideNoteDetailInteractors(
        noteCacheDataSource: NoteCacheDataSource,
        noteNetworkDataSource: NoteNetworkDataSource
    ): NoteDetailInteractors{
        return NoteDetailInteractors(
            DeleteNote(noteCacheDataSource, noteNetworkDataSource),
            UpdateNote(noteCacheDataSource, noteNetworkDataSource)
        )
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideNoteListInteractors(
        noteCacheDataSource: NoteCacheDataSource,
        noteNetworkDataSource: NoteNetworkDataSource,
        noteFactory: NoteFactory
    ): NoteListInteractors {
        return NoteListInteractors(
            InsertNewNote(noteCacheDataSource, noteNetworkDataSource, noteFactory),
            DeleteNote(noteCacheDataSource, noteNetworkDataSource),
            SearchNotes(noteCacheDataSource),
            GetNumNotes(noteCacheDataSource),
            RestoreDeletedNote(noteCacheDataSource, noteNetworkDataSource),
            DeleteMultipleNotes(noteCacheDataSource, noteNetworkDataSource)
        )
    }



}










