package com.prplmnstr.mynotesapp.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.prplmnstr.mynotesapp.model.Note
import com.prplmnstr.mynotesapp.utils.Constants

@Dao
interface NotesDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNoteItem(noteItem: Note): Long


    @Update
    suspend fun updateNoteItem(noteItem: Note): Int

    @Delete
    suspend fun deleteNoteItem(noteItem: Note): Int

    @Delete
    suspend fun deleteNoteItems(noteItems: List<Note>): Int

    @Query("SELECT * FROM ${Constants.NOTES_TABLE} WHERE user = :user")
    fun getAllNotes(user: String): LiveData<List<Note>>


    @Query("SELECT * FROM ${Constants.NOTES_TABLE}")
    fun getAllNotes(): LiveData<List<Note>>
}