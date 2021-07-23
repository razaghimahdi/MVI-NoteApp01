package com.example.mvi_noteapp.business.interactors.notedetail

import com.example.mvi_noteapp.business.interactors.common.DeleteNote
import com.example.mvi_noteapp.framework.presentation.notedetail.state.NoteDetailViewState

// Use cases
class NoteDetailInteractors (
    val deleteNote: DeleteNote<NoteDetailViewState>,
    val updateNote: UpdateNote
)