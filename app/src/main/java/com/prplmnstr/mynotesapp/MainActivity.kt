package com.prplmnstr.mynotesapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.prplmnstr.mynotesapp.viewmodel.NotesViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    // ViewModel instance for handling data and UI logic
    lateinit var notesViewModel: NotesViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        Log.d("VIEW", "onCreate: activity")
        // Initialize the Navigation Controller with the navHostFragment
        navController = findNavController(R.id.navHostFragment)
    }
}
