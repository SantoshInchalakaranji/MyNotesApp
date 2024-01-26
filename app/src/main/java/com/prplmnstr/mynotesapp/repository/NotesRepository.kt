package com.prplmnstr.mynotesapp.repository

import com.prplmnstr.mynotesapp.db.NotesDao
import com.prplmnstr.mynotesapp.model.Note

class NotesRepository(
    private val notesDao: NotesDao
) {

    val notes = notesDao.getAllNotes()

//    fun getAllNoteItems(user:String) =
//       notesDao.getAllNotes(user)


    suspend fun insertNote(note: Note) = notesDao.insertNoteItem(note)
    suspend fun removeNote(note: Note) = notesDao.deleteNoteItem(note)
    suspend fun removeNotes(noteItems: List<Note>) = notesDao.deleteNoteItems(noteItems)
    suspend fun updateNote(note: Note) = notesDao.updateNoteItem(note)

}