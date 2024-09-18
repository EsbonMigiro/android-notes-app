package com.example.androidkotlincrudnotesapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class NoteDatabaseHelper (context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object{
        private const val DATABASE_NAME = "notesapp.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "allnotes"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_CONTENT = "content"
        private const val COLUMN_DATE = "date"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = """
            CREATE TABLE $TABLE_NAME(
               $COLUMN_ID INTEGER PRIMARY KEY, 
               $COLUMN_TITLE TEXT, 
               $COLUMN_CONTENT TEXT,
               $COLUMN_DATE TEXT
            )
            """.trimIndent()
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTableQuery = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(dropTableQuery)
        onCreate(db)
    }

    fun insertNote(note: Note){
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, note.title)
            put(COLUMN_CONTENT, note.content)
            put(COLUMN_DATE, note.date)
        }

        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun getNotes(): List<Note>{
        val noteList = mutableListOf<Note>()
        val  db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(query, null)

        while (cursor.moveToNext()){
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
            val content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT))
            val date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE))


            val note = Note(id, title, content, date)

            noteList.add(note)


        }
        cursor.close()
        db.close()
        return noteList
    }

//    fun updateNote(note: Note){
//        val db = writableDatabase
//        val values = ContentValues().apply {
//            put("COLUMN_TITLE", note.title)
//            put("COLUMN_CONTENT", note.content)
//        }
//        val whereClause = "$COLUMN_ID = ?"
//        val whereArgs = arrayOf(note.id.toString())
//
//        db.update(TABLE_NAME, values, whereClause, whereArgs)
//        db.close()
//    }

    fun getNoteById(noteId: Int): Note{
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = $noteId"
        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()

        val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
        val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
        val content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT))
        val date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE))


        cursor.close()
        db.close()

        return Note(id, title, content, date)
    }


    fun updateNote(note: Note) {
        // Check if note ID is valid
        if (note.id == null) {
            println("Note ID is null")
            return
        }

        // Get a writable instance of the database
        val db = this.writableDatabase
        if (db == null) {
            println("Database is null or unavailable")
            return
        }

        try {
            val values = ContentValues().apply {
                put(COLUMN_TITLE, note.title)
                put(COLUMN_CONTENT, note.content)
                put(COLUMN_DATE, note.date)

            }

            val whereClause = "$COLUMN_ID = ?"
            val whereArgs = arrayOf(note.id.toString())

            val rowsUpdated = db.update(TABLE_NAME, values, whereClause, whereArgs)
            if (rowsUpdated > 0) {
                println("Note updated successfully")
            } else {
                println("Note update failed or no rows affected")
            }

        } catch (e: Exception) {
            println("An exception occurred while updating the note")
//            e.printStackTrace() // Log the error for debugging
        } finally {
            // Close the database to free up resources
            db.close()
//            println("Database connection closed")
        }
    }

    fun deleteNote(noteId: Int){
        val db = writableDatabase
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(noteId.toString())
        db.delete(TABLE_NAME, whereClause, whereArgs)
        db.close()
    }

}