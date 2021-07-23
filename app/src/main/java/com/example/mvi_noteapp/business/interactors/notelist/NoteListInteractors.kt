package com.example.mvi_noteapp.business.interactors.notelist

import com.example.mvi_noteapp.business.interactors.common.DeleteNote
import com.example.mvi_noteapp.framework.presentation.notelist.state.NoteListViewState


// Use cases
class NoteListInteractors (
    val insertNewNote: InsertNewNote,
    val deleteNote: DeleteNote<NoteListViewState>,
    val searchNotes: SearchNotes,
    val getNumNotes: GetNumNotes,
    val restoreDeletedNote: RestoreDeletedNote,
    val deleteMultipleNotes: DeleteMultipleNotes
)
