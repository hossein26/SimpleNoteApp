package com.hossein.simplenote

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.hossein.simplenote.databinding.ActivityMainBinding
import com.hossein.simplenote.db.NoteDatabase
import com.hossein.simplenote.repository.NoteRepository
import com.hossein.simplenote.viewmodel.NoteViewModel
import com.hossein.simplenote.viewmodel.NoteViewModelProviderFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var noteViewModel: NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpViewModel()
    }

    //set up viewModel
    private fun setUpViewModel() {
        val noteRepository = NoteRepository(
            NoteDatabase(this)
        )

        val viewModelProviderFactory = NoteViewModelProviderFactory(
            application, noteRepository
        )

        noteViewModel =ViewModelProvider(
            this,
            viewModelProviderFactory
        ).get(NoteViewModel::class.java)
    }
}