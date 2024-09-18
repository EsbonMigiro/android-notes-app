package com.example.androidkotlincrudnotesapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.androidkotlincrudnotesapp.databinding.ActivityAddNoteBinding
import com.example.androidkotlincrudnotesapp.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.Date


class AddNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddNoteBinding
    private lateinit var db: NoteDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

//        binding = AddNoteActivity.inflate(layoutInflater)
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        db = NoteDatabaseHelper(this)

        binding.btnSave.setOnClickListener {
            val title = binding.edtTxtTitle.text.toString()
            val content = binding.edtTxtContent.text.toString()

            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault())
            val currentDate = sdf.format(Date())

            val note = Note(0, title, content, currentDate)

            db.insertNote(note)
            finish()
            Toast.makeText(this, "Noted created successful", Toast.LENGTH_SHORT).show()

        }


    }
}