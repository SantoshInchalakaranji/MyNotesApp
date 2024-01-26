package com.prplmnstr.mynotesapp.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prplmnstr.mynotesapp.model.Note
import com.prplmnstr.mynotesapp.repository.NotesRepository
import com.prplmnstr.mynotesapp.utils.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NotesViewModel(
    private val repository: NotesRepository,
    private val application: Application
) : ViewModel() {


    private val statusMessage = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>>
        get() = statusMessage

    val _navigateBack = MutableLiveData<Boolean>()
    val navigateBack: LiveData<Boolean>
        get() = _navigateBack

    val userEmail = MutableLiveData<String>()
    var noteObject = MutableLiveData<Note>()
    var insert = true

    val notes = repository.notes


    val sampleNote = Note(0, "", "", "", "")

    init {
        noteObject.value = sampleNote.copy()
    }


//    fun loadUserNotes(): LiveData<List<Note>> {
//       return repository.getAllNoteItems(userEmail.value!!)
//    }


    fun removeNotes(notes: List<Note>) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.removeNotes(notes)

        }
    }


    fun removeNote(note: Note) {

        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.removeNote(note)
            withContext(Dispatchers.Main) {
                if (result > -1) {
                    statusMessage.value = Event("Note deleted.")

                } else {
                    statusMessage.value = Event("Error while deleting!")
                }
            }
        }
    }



    fun insertOrUpdateNote() {
        if (insert) {
            insertNoteObject()
        } else {
            updateNoteObject()
        }
    }

    private fun updateNoteObject() = viewModelScope.launch {
        if (noteObject.value == null || noteObject.value!!.title.isEmpty()) {
            withContext(Dispatchers.Main) {
                statusMessage.value = Event("Please Enter Title")

            }
        } else {
            val newRowId = repository.updateNote(noteObject.value!!)
            withContext(Dispatchers.Main) {
                if (newRowId > -1) {
                    statusMessage.value = Event("Note Updated Successfully! ")
                    noteObject.value = sampleNote.copy()
                    _navigateBack.value = true

                } else {
                    statusMessage.value = Event("Error Occurred!")
                }
            }
        }
        insert = true
    }

    private fun insertNoteObject() = viewModelScope.launch {
        if (noteObject.value == null || noteObject.value!!.title.isEmpty()) {
            withContext(Dispatchers.Main) {
                statusMessage.value = Event("Please Enter Title")

            }
        } else {
            val newRowId = repository.insertNote(noteObject.value!!)
            withContext(Dispatchers.Main) {
                if (newRowId > -1) {
                    statusMessage.value = Event("Note Created Successfully! ")
                    noteObject.value = sampleNote.copy()
                    _navigateBack.value = true

                } else {
                    statusMessage.value = Event("Error Occurred!")
                }
            }
        }
    }

    fun resetNavigation() {
        _navigateBack.value = false
    }
}