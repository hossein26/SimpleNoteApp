package com.hossein.simplenote.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.hossein.simplenote.MainActivity
import com.hossein.simplenote.R
import com.hossein.simplenote.databinding.FragmentNoteBinding
import com.hossein.simplenote.model.Note
import com.hossein.simplenote.viewmodel.NoteViewModel

class NoteFragment : Fragment() {

    private var _binding: FragmentNoteBinding? = null
    private val binding get() = _binding!!

    private lateinit var noteViewModel: NoteViewModel

    private lateinit var note: Note

    private var noteId: Int = 0

    private var noteColor: String = "#FF000000"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        note = Note()

        val args = arguments?.let { NoteFragmentArgs.fromBundle(it) }
        noteId = args?.noteId?.id ?: 0

        noteColor = args?.noteColor ?: "#FF000000"

        noteViewModel = (activity as MainActivity).noteViewModel
        noteViewModel.loadNote(noteId)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoteBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.colorView.setBackgroundColor(Color.parseColor(noteColor))

        if (noteId != 0) {
            noteViewModel.noteLiveData.observe(
                viewLifecycleOwner,
                { note ->
                    note?.let {
                        this.note = note
                        binding.etNoteTitle.setText(note.noteTitle)
                        binding.etNoteBody.setText(note.noteBody)
                    }
                }
            )
        }


        binding.fabAddNote.setOnClickListener {
            if (noteId == 1) {
                updateNote()
            } else {
                saveNote()
            }
        }

        binding.imgMore.setOnClickListener {
            findNavController().navigate(
                R.id.action_noteFragment_to_noteBottomSheetFragment
            )
        }
    }

    override fun onStop() {
        super.onStop()
        binding.layout.removeAllViews()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun saveNote() {
        val noteTitle = binding.etNoteTitle.text.toString().trim()
        val noteBody = binding.etNoteBody.text.toString().trim()

        if (noteTitle.isNotEmpty()) {
            val note = Note(noteTitle = noteTitle, noteBody = noteBody)

            noteViewModel.addNote(note)
            Toast.makeText(context, "saved", Toast.LENGTH_SHORT).show()

            findNavController().navigate(
                R.id.action_noteFragment_to_homeFragment
            )
        } else {
            Toast.makeText(context, "save failed!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateNote() {
        val noteTitle = binding.etNoteTitle.text.toString().trim()
        val noteBody = binding.etNoteBody.text.toString().trim()

        if (noteTitle.isNotEmpty()) {
            val note = Note(noteId, noteTitle = noteTitle, noteBody = noteBody)

            noteViewModel.updateNote(note)
            Toast.makeText(context, "updated", Toast.LENGTH_SHORT).show()

            findNavController().navigate(
                R.id.action_noteFragment_to_homeFragment
            )
        } else {
            Toast.makeText(context, "update failed!", Toast.LENGTH_SHORT).show()
        }
    }

}