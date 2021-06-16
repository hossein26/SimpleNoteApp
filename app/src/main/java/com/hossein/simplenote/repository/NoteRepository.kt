package com.hossein.simplenote.repository

import androidx.lifecycle.LiveData
import com.hossein.simplenote.db.NoteDatabase
import com.hossein.simplenote.model.Note

class NoteRepository(private val db: NoteDatabase) {

    suspend fun insertNote(note: Note) = db.getNoteDao().insertNote(note)
    suspend fun updateNote(note: Note) = db.getNoteDao().updateNote(note)
    suspend fun deleteNote(note: Note) = db.getNoteDao().deleteNote(note)
    fun getNote(id: Int): LiveData<Note> = db.getNoteDao().getNote(id)
    fun getAllNote() = db.getNoteDao().getAllNotes()
    fun searchNote(query: String?) = db.getNoteDao().searchNote(query)
}