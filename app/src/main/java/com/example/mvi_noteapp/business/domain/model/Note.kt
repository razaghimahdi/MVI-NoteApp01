package com.example.mvi_noteapp.business.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize/**we gonna pass this as arguments for process death*/
data class Note(/**the reason we didnt use that as Entity which technically we could but it is better to keep separate  */
    val id:String,
    val title:String,
    val body:String,
    val updated_at:String,
    val created_at:String,
):Parcelable{
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Note

        if (id != other.id) return false
        if (title != other.title) return false
        if (body != other.body) return false
        if (created_at != other.created_at) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + body.hashCode()
        result = 31 * result + created_at.hashCode()
        return result
    }
}
