package com.example.mvi_noteapp.business.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize/**we gonna pass this as arguments*/
data class Note(/**the reason we didnt use that as Entity which technically we could but it is better to keep separate  */
    val id:String,
    val title:String,
    val body:String,
    val updated_at:String,
    val created_at:String,
):Parcelable
