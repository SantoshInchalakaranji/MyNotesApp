package com.prplmnstr.mynotesapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.prplmnstr.mynotesapp.databinding.FragmentAddOrUpdateNotesBinding
import com.prplmnstr.mynotesapp.viewmodel.NotesViewModel

class AddOrUpdateNotesFragment : Fragment() {

    private lateinit var binding: FragmentAddOrUpdateNotesBinding
    private val notesViewModel: NotesViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAddOrUpdateNotesBinding.inflate(inflater, container, false)

        binding.viewModel = notesViewModel
        binding.lifecycleOwner = this
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        notesViewModel.message.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT).show()
            }
        })

        notesViewModel.navigateBack.observe(viewLifecycleOwner, Observer { shouldNavigate ->
            if (shouldNavigate) {
                findNavController().navigateUp()
                // Reset the LiveData to prevent repeated navigation
                notesViewModel.resetNavigation()
            }
        })
    }


}