package com.hossein.simplenote.db

import androidx.room.TypeConverter
import java.util.*

class NoteTypeConverters {
    @TypeConverter
    fun fromUUID(uuid: UUID): String{
        return uuid.toString()
    }

    @TypeConverter
    fun toUUID(uuid: String): UUID {
        return UUID.fromString(uuid)
    }
}