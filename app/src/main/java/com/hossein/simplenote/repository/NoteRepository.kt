package com.hossein.simplenote.repository

import androidx.lifecycle.LiveData
import com.hossein.simplenote.db.NoteDatabase
import com.hossein.simplenote.model.Note
import java.util.*

class NoteRepository(private val db: NoteDatabase) {

    suspend fun insertNote(note: Note) = db.getNoteDao().insertNote(note)
    suspend fun updateNote(note: Note) = db.getNoteDao().updateNote(note)
    suspend fun deleteNote(note: Note) = db.getNoteDao().updateNote(note)
    fun getNote(id: UUID): LiveData<Note> = db.getNoteDao().getNote(id)
    fun getAllNote() = db.getNoteDao().getAllNotes()
}