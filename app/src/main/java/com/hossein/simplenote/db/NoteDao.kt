package com.hossein.simplenote.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.hossein.simplenote.model.Note
import java.util.*

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Update
    suspend fun updateNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("SELECT * FROM NOTES ORDER BY id DESC")
    fun getAllNotes(): LiveData<List<Note>>

    @Query("SELECT * FROM notes WHERE id=(:id)")
    fun getNote(id: UUID): LiveData<Note>




















}