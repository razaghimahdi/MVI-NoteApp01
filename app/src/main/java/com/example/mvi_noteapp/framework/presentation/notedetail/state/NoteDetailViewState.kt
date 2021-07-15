package com.example.mvi_noteapp.framework.presentation.notedetail.state

import android.os.Parcelable
import com.example.mvi_noteapp.business.domain.model.Note
import com.example.mvi_noteapp.business.domain.state.ViewState
import kotlinx.android.parcel.Parcelize


@Parcelize
data class NoteDetailViewState(

    var note: Note? = null,

    var isUpdatePending: Boolean? = null

) : Parcelable, ViewState
