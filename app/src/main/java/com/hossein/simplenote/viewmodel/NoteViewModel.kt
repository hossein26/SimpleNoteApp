package com.hossein.simplenote.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.hossein.simplenote.model.Note
import com.hossein.simplenote.repository.NoteRepository
import kotlinx.coroutines.launch
import java.util.*

class NoteViewModel(app: Application, private val noteRepository: NoteRepository) :
    AndroidViewModel(app) {

    private val noteIdLiveData = MutableLiveData<Int>()

    fun addNote(note: Note) =
        viewModelScope.launch {
            noteRepository.insertNote(note)
        }

    fun deleteNote(note: Note) =
        viewModelScope.launch {
            noteRepository.deleteNote(note)
        }

    fun updateNote(note: Note) =
        viewModelScope.launch {
            noteRepository.updateNote(note)
        }

    fun getAllNote() = noteRepository.getAllNote()

    var noteLiveData: LiveData<Note> =
        Transformations.switchMap(noteIdLiveData){ noteId ->
            noteRepository.getNote(noteId)
        }

    fun loadNote(noteId: Int){
        noteIdLiveData.value = noteId
    }
}