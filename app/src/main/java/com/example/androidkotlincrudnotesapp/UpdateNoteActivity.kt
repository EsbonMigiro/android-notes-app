package com.example.androidkotlincrudnotesapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.androidkotlincrudnotesapp.databinding.ActivityUpdateNoteBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class UpdateNoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateNoteBinding
    private lateinit var db: NoteDatabaseHelper
    private  var noteId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityUpdateNoteBinding.inflate(layoutInflater)
        db = NoteDatabaseHelper(this)

        noteId = intent.getIntExtra("note_id", -1)


        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if(noteId == -1){
            finish()
            return
        }

        val note = db.getNoteById(noteId)
        binding.edtTxtTitleUpdate.setText(note.title)
        binding.edtTxtContentUpdate.setText(note.content)

        binding.btnSaveUpdate.setOnClickListener {
            val newTitle = binding.edtTxtTitleUpdate.text.toString()
            val newContent = binding.edtTxtContentUpdate.text.toString()

            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val newCurrentDate = sdf.format(Date())

            val updatedNote = Note(0, newTitle, newContent, newCurrentDate)
            db.updateNote(updatedNote)
            finish()
            Toast.makeText(this, "Note Updated", Toast.LENGTH_SHORT).show()
        }

    }
}