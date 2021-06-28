package com.hossein.simplenote.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "notes")
@Parcelize
data class Note(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var noteTitle: String = "",
    val noteBody: String = "",
    val color: String = "#FFFFFF",
    val imagePath: String = ""
): Parcelable