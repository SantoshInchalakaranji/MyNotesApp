package com.prplmnstr.mynotesapp.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.prplmnstr.mynotesapp.repository.NotesRepository

class NotesViewModelFactory(
    private val repository: NotesRepository,
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotesViewModel::class.java)) {
            return NotesViewModel(repository, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }

}