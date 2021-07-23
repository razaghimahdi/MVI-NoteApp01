package com.example.mvi_noteapp.di

import android.content.SharedPreferences
import androidx.lifecycle.ViewModelProvider
import com.example.mvi_noteapp.business.domain.model.NoteFactory
import com.example.mvi_noteapp.business.interactors.notedetail.NoteDetailInteractors
import com.example.mvi_noteapp.business.interactors.notelist.NoteListInteractors
import com.example.mvi_noteapp.framework.presentation.common.NoteViewModelFactory
import com.example.mvi_noteapp.framework.presentation.splash.NoteNetworkSyncManager
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Singleton


@ExperimentalCoroutinesApi
@FlowPreview
@Module
object NoteViewModelModule {

    @Singleton
    @JvmStatic
    @Provides
    fun provideNoteViewModelFactory(
        noteListInteractors: NoteListInteractors,
        noteDetailInteractors: NoteDetailInteractors,
        noteNetworkSyncManager: NoteNetworkSyncManager,
        noteFactory: NoteFactory,
        editor: SharedPreferences.Editor,
        sharedPreferences: SharedPreferences
    ): ViewModelProvider.Factory{
        return NoteViewModelFactory(
            noteListInteractors = noteListInteractors,
            noteDetailInteractors = noteDetailInteractors,
            noteNetworkSyncManager = noteNetworkSyncManager,
            noteFactory = noteFactory,
            editor = editor,
            sharedPreferences = sharedPreferences
        )
    }

}

