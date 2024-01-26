package com.prplmnstr.mynotesapp.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.prplmnstr.mynotesapp.R
import com.prplmnstr.mynotesapp.db.AppDatabase
import com.prplmnstr.mynotesapp.repository.NotesRepository
import com.prplmnstr.mynotesapp.utils.Constants
import com.prplmnstr.mynotesapp.utils.SharedPreferencesManager
import com.prplmnstr.mynotesapp.viewmodel.NotesViewModel
import com.prplmnstr.mynotesapp.viewmodel.NotesViewModelFactory


class SignInFragment : Fragment() {

    private lateinit var notesViewModel: NotesViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("VIEW", "onCreate: signin")


        // initialize view model in the activity scope
        val notesDao = AppDatabase.getInstance(requireActivity().application).notesDao
        val notesRepository = NotesRepository(notesDao)
        val factory = NotesViewModelFactory(notesRepository, requireActivity().application)
        notesViewModel = ViewModelProvider(requireActivity(), factory)[NotesViewModel::class.java]


        val sharedPreferencesManager = SharedPreferencesManager(requireContext())
        val userLoggedIn = sharedPreferencesManager.isUserLoggedIn()


        if (userLoggedIn) {
            // User is logged in, navigate to home fragment
            // Toast.makeText(requireContext(), sharedPreferencesManager.getUserName(), Toast.LENGTH_SHORT).show()

            notesViewModel.userEmail.value = sharedPreferencesManager.getUserName()
            findNavController().navigate(R.id.action_signInFragment_to_homeFragment)


        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        Log.d("VIEW", "onCreateview: signin")
        return inflater.inflate(R.layout.fragment_sign_in, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        Log.d("VIEW", "onCreated: singnin")

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        val mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        val signInButton: SignInButton = view.findViewById(R.id.sign_in_button)
        signInButton.setSize(SignInButton.SIZE_STANDARD)

        signInButton.setOnClickListener {
            signIn(mGoogleSignInClient)
        }


    }


    private fun signIn(mGoogleSignInClient: GoogleSignInClient) {


        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, Constants.SIGN_IN_REQUEST_CODE)


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == Constants.SIGN_IN_REQUEST_CODE) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful
                val account: GoogleSignInAccount? = task.getResult(ApiException::class.java)

                if (account != null) {
                    Toast.makeText(requireContext(), "Logged In", Toast.LENGTH_SHORT).show()
                    notesViewModel.userEmail.value = account.email.toString()
                    val sharedPreferencesManager = SharedPreferencesManager(requireContext())
                    sharedPreferencesManager.saveUserLoggedIn(true)
                    sharedPreferencesManager.saveUserName(account.email.toString())
                    findNavController().navigate(R.id.action_signInFragment_to_homeFragment)
                }

            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("TAGFailed", "Google sign in failed", e)
            }

        } else {
            Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show()
        }
    }



}