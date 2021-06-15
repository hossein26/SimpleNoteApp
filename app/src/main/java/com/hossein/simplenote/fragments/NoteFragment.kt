package com.hossein.simplenote.fragments

import android.annotation.SuppressLint
import android.content.*
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
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
    private var selectedColor: String = "#FFFFFF"
    private lateinit var preferences: SharedPreferences
    private var onClicked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        note = Note()
        //preference
        preferences = PreferenceManager.getDefaultSharedPreferences(context)
        //args
        val args = arguments?.let { NoteFragmentArgs.fromBundle(it) }
        //args from home fragment
        noteId = args?.noteId?.id ?: 0
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("Range")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        noteViewModel = (activity as MainActivity).noteViewModel
        noteViewModel.loadNote(noteId)
        //use preference
        preferences = context?.getSharedPreferences("preference", Context.MODE_PRIVATE) ?: return
        binding.etNoteTitle.setText(preferences.getString("title", ""))
        binding.etNoteBody.setText(preferences.getString("body", ""))
        //get color from bottomSheet
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(
            broadcastReceiver, IntentFilter("bottom_sheet_action")
        )

        binding.colorView.setBackgroundColor(Color.parseColor(selectedColor))

        if (noteId != 0) {
            noteViewModel.noteLiveData.observe(
                viewLifecycleOwner,
                { note ->
                    note?.let {
                        this.note = note
                        binding.etNoteTitle.setText(note.noteTitle)
                        binding.etNoteBody.setText(note.noteBody)
                        binding.colorView.setBackgroundColor(Color.parseColor(note.color))
                    }
                }
            )
        }

        binding.fabAddNote.setOnClickListener {
            onClicked = true

            if (noteId == 0) {
                saveNote()
            } else {
                updateNote()
            }
        }

        binding.frameLayoutMore.setOnClickListener {
            findNavController().navigate(
                R.id.action_noteFragment_to_noteBottomSheetFragment
            )
        }
    }

    override fun onPause() {
        super.onPause()
        preferences.edit().apply {
            putString("title", binding.etNoteTitle.text.toString())
            putString("body", binding.etNoteBody.text.toString())
        }.apply()

        if (onClicked) {
            val editor: SharedPreferences.Editor = preferences.edit()
            editor.clear()
            editor.apply()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null

        val editor: SharedPreferences.Editor = preferences.edit()
        editor.clear()
        editor.apply()
    }

    private fun saveNote() {
        val noteTitle = binding.etNoteTitle.text.toString().trim()
        val noteBody = binding.etNoteBody.text.toString().trim()

        if (noteTitle.isNotEmpty()) {
            val note = Note(id = noteId, noteTitle = noteTitle, noteBody = noteBody, color = selectedColor)

            noteViewModel.addNote(note)
            Toast.makeText(context, R.string.saved_toast, Toast.LENGTH_SHORT).show()

            findNavController().navigate(
                R.id.action_noteFragment_to_homeFragment
            )
        } else {
            Toast.makeText(context, R.string.save_fail_toast, Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateNote() {
        val noteTitle = binding.etNoteTitle.text.toString().trim()
        val noteBody = binding.etNoteBody.text.toString().trim()

        if (noteTitle.isNotEmpty()) {
            val note =
                Note(id = noteId, noteTitle = noteTitle, noteBody = noteBody, color = selectedColor)

            noteViewModel.updateNote(note)
            Toast.makeText(context, R.string.updated_toast, Toast.LENGTH_SHORT).show()

            findNavController().navigate(
                R.id.action_noteFragment_to_homeFragment
            )
        } else {
            Toast.makeText(context, R.string.update_failed_toast, Toast.LENGTH_SHORT).show()
        }
    }

    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            val actionColor = p1!!.getStringExtra("action")
            when (actionColor!!) {
                "Blue" -> {
                    selectedColor = p1.getStringExtra("selectedColor")!!
                    binding.colorView.setBackgroundColor(Color.parseColor(selectedColor))
                }
                "Yellow" -> {
                    selectedColor = p1.getStringExtra("selectedColor")!!
                    binding.colorView.setBackgroundColor(Color.parseColor(selectedColor))
                }
                "Purple" -> {
                    selectedColor = p1.getStringExtra("selectedColor")!!
                    binding.colorView.setBackgroundColor(Color.parseColor(selectedColor))
                }
                "Green" -> {
                    selectedColor = p1.getStringExtra("selectedColor")!!
                    binding.colorView.setBackgroundColor(Color.parseColor(selectedColor))
                }
                "Orange" -> {
                    selectedColor = p1.getStringExtra("selectedColor")!!
                    binding.colorView.setBackgroundColor(Color.parseColor(selectedColor))
                }
                "Black" -> {
                    selectedColor = p1.getStringExtra("selectedColor")!!
                    binding.colorView.setBackgroundColor(Color.parseColor(selectedColor))
                }
                else -> {
                    selectedColor = p1.getStringExtra("selectedColor")!!
                    binding.colorView.setBackgroundColor(Color.parseColor(selectedColor))
                }
            }
        }
    }
}