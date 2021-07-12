package com.example.mvi_noteapp.business.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize/**we gonna pass this as arguments*/
data class Note(
    val id:String,
    val title:String,
    val body:String,
    val updated_at:String,
    val created_at:String,
):Parcelable
