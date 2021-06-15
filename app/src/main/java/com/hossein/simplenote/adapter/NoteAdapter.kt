package com.hossein.simplenote.adapter

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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

        if (currentNote.color != "#202734") {
            holder.itemBinding.tvNoteTitle.setTextColor(Color.BLACK)
            holder.itemBinding.tvNoteBody.setTextColor(Color.BLACK)
        }

        holder.itemView.setOnClickListener { view ->

            val direction = HomeFragmentDirections
                .actionHomeFragmentToNoteFragment(noteId = currentNote)
            view.findNavController().navigate(direction)
        }

        holder.itemView.setOnLongClickListener { view ->
            val noteViewModel: NoteViewModel = (view.context as MainActivity).noteViewModel

            val dialogView = View.inflate(view.context, R.layout.alert_dialog, null)
            val textDelete = dialogView.findViewById<TextView>(R.id.text_delete)
            val textCancel = dialogView.findViewById<TextView>(R.id.text_cancel)

            val alertDialog = AlertDialog.Builder(view.context).create()
            alertDialog.setView(dialogView)
            alertDialog.show()
            textDelete.setOnClickListener {
                noteViewModel.deleteNote(currentNote)
                notifyDataSetChanged()
                Toast.makeText(view.context, "item deleted", Toast.LENGTH_SHORT).show()
                alertDialog.dismiss()
            }

            textCancel.setOnClickListener {
                alertDialog.dismiss()
            }
        true
    }

}

override fun getItemCount(): Int {
    return differ.currentList.size
}
}