package com.hossein.simplenote.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.*
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
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

private const val IMAGE_PICK_CODE = 1000
private const val PERMISSION_CODE = 1001

class NoteFragment : Fragment() {

    private var _binding: FragmentNoteBinding? = null
    private val binding get() = _binding!!
    private lateinit var noteViewModel: NoteViewModel
    private lateinit var note: Note
    private var noteId: Int = 0
    private var selectedColor: String = "#FFFFFF"
    private lateinit var preferences: SharedPreferences
    private var onClicked = false

    private var selectedImagePath = ""

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

                        if (note.imagePath != ""){
                            selectedImagePath = note.imagePath
                            binding.imgNote.setImageBitmap(BitmapFactory.decodeFile(note.imagePath))
                            binding.layoutImage.visibility = View.VISIBLE
                            binding.imgNote.visibility = View.VISIBLE
                            binding.imgDelete.visibility = View.VISIBLE
                        }else{
                            binding.layoutImage.visibility = View.GONE
                            binding.imgNote.visibility = View.GONE
                            binding.imgDelete.visibility = View.GONE
                        }
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

        binding.imgDelete.setOnClickListener {
            selectedImagePath = ""
            binding.layoutImage.visibility = View.GONE
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
            val note =
                Note(id = noteId, noteTitle = noteTitle, noteBody = noteBody, color = selectedColor, imagePath = selectedImagePath)

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
                Note(id = noteId, noteTitle = noteTitle, noteBody = noteBody, color = selectedColor, imagePath = selectedImagePath)

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
                "Image" -> {
                    imageButtonClicked()
                }
                else -> {
                    selectedColor = p1.getStringExtra("selectedColor")!!
                    binding.colorView.setBackgroundColor(Color.parseColor(selectedColor))
                }
            }
        }
    }

    private fun imageButtonClicked() {
        //check runtime permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (requireContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                //permission denied
                val permission = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                //show popup to request runtime
                requestPermissions(permission, PERMISSION_CODE)
            }else{
                //permission already granted
                pickImageFromGallery()
            }
        }else{
            //system os <= Marshmallow
            pickImageFromGallery()
        }
    }

    //handle permission
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            PERMISSION_CODE ->{
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED ){
                    //permission from popup granted
                    pickImageFromGallery()
                }else{
                    //permission from popup denied
                    Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){
            binding.imgNote.setImageURI(data?.data)
            binding.layoutImage.visibility = View.VISIBLE
            binding.imgNote.visibility = View.VISIBLE
            binding.imgDelete.visibility = View.VISIBLE

           selectedImagePath = getPathFromUri(data?.data!!)!!
        }
    }

    private fun getPathFromUri(contentUri: Uri): String? {
        val filePath: String?

        val cursor = requireActivity().contentResolver.query(contentUri,null,null,null,null)
        if (cursor == null){
            filePath = contentUri.path
        }else{
            cursor.moveToFirst()
            val index = cursor.getColumnIndex("_data")
            filePath = cursor.getString(index)
            cursor.close()
        }
        return filePath
    }


}
















