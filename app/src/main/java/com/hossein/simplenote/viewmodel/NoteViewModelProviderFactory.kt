package com.hossein.simplenote.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hossein.simplenote.repository.NoteRepository

@Suppress("UNCHECKED_CAST")
class NoteViewModelProviderFactory(
    val app: Application,
    private val noteRepository: NoteRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NoteViewModel(app, noteRepository) as T
    }
}