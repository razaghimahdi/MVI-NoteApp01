package com.example.mvi_noteapp.framework.datasource.cache.model


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class NoteCacheEntity(/**so this detail are the same as domain layer in business package
and the reason we didnt use that as Entity which technically we could but it is better to keep separate
                            */

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id: String,

    @ColumnInfo(name = "title")
    var title: String,

    @ColumnInfo(name = "body")
    var body: String,

    @ColumnInfo(name = "updated_at")
    var updated_at: String,

    @ColumnInfo(name = "created_at")
    var created_at: String

){



    companion object{

        fun nullTitleError(): String{
            return "You must enter a title."
        }

        fun nullIdError(): String{
            return "NoteEntity object has a null id. This should not be possible. Check local database."
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NoteCacheEntity

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
        result = 31 * result + updated_at.hashCode()
        result = 31 * result + created_at.hashCode()
        return result
    }
}