package com.example.androidkotlincrudnotesapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class NotesAdapter(private var notes: List<Note>, context: Context): RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    private val db: NoteDatabaseHelper = NoteDatabaseHelper(context)

    class NoteViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val txtVwTitle: TextView = itemView.findViewById(R.id.txtVwNoteTitle)
        val txtVwContent: TextView = itemView.findViewById(R.id.txtVwContent)
        val btnSaveUpdate: ImageView = itemView.findViewById(R.id.btnUpdate)
        val btnDelete: ImageView = itemView.findViewById(R.id.btnDelete)
        val txtVwDate: TextView = itemView.findViewById(R.id.txtVwDate)



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
        return NoteViewHolder(view)
    }

    override fun getItemCount(): Int = notes.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
       val note = notes[position]
        holder.txtVwTitle.text = note.id.toString() +". "+ note.title
        holder.txtVwContent.text = note.content
        holder.txtVwDate.text = note.date

        holder.btnSaveUpdate.setOnClickListener {
            val intent = Intent(holder.itemView.context, UpdateNoteActivity::class.java).apply {
                putExtra("note_id", note.id)
            }
            holder.itemView.context.startActivity(intent)
        }
        holder.btnDelete.setOnClickListener {
            db.deleteNote(note.id)
            refreshData(db.getNotes())
            Toast.makeText(holder.itemView.context, note.title + " deleted", Toast.LENGTH_SHORT).show()
        }
    }
    fun refreshData(newNotes: List<Note>){
        notes = newNotes
        notifyDataSetChanged()
    }
}