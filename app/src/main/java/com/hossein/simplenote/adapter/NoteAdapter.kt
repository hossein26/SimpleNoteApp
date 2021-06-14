package com.hossein.simplenote.adapter

import android.app.AlertDialog
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hossein.simplenote.MainActivity
import com.hossein.simplenote.R
import com.hossein.simplenote.databinding.NoteLayoutAdapterBinding
import com.hossein.simplenote.fragments.HomeFragmentDirections
import com.hossein.simplenote.model.Note
import com.hossein.simplenote.viewmodel.NoteViewModel

class NoteAdapter : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    class NoteViewHolder(val itemBinding: NoteLayoutAdapterBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    private val differCallback =
        object : DiffUtil.ItemCallback<Note>() {
            override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem.id == newItem.id &&
                        oldItem.noteBody == newItem.noteBody &&
                        oldItem.noteTitle == newItem.noteTitle &&
                        oldItem.color == newItem.color
            }

            override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem == newItem
            }

        }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            NoteLayoutAdapterBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }


    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentNote = differ.currentList[position]

        holder.itemBinding.tvNoteTitle.text = currentNote.noteTitle
        holder.itemBinding.tvNoteBody.text = currentNote.noteBody
        holder.itemBinding.cardView.setCardBackgroundColor(Color.parseColor(currentNote.color))

        holder.itemView.setOnClickListener { view ->

            val direction = HomeFragmentDirections
                .actionHomeFragmentToNoteFragment(noteId = currentNote)
            view.findNavController().navigate(direction)
        }

        holder.itemView.setOnLongClickListener {view ->
            val noteViewModel: NoteViewModel = (view.context as MainActivity).noteViewModel
            AlertDialog.Builder(view.context).apply {
                setTitle("Delete Note")
                setMessage("Are you sure you want to permanently delete this note?")
                setPositiveButton("DELETE") { _, _ ->
                    noteViewModel.deleteNote(currentNote)
                    notifyDataSetChanged()
                    Toast.makeText(view.context, "item deleted", Toast.LENGTH_SHORT).show()
                }
                setNegativeButton("CANCEL", null)
            }.create().show()
            true
        }

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}