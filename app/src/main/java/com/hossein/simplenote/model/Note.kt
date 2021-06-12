package com.hossein.simplenote.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.*

@Entity(tableName = "notes")
@Parcelize
data class Note(
    @PrimaryKey
    var id: UUID = UUID.randomUUID(),
    val noteTitle: String = "",
    val noteBody: String = ""
): Parcelable