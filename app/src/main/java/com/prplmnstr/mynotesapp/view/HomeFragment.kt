package com.prplmnstr.mynotesapp.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.prplmnstr.mynotesapp.R
import com.prplmnstr.mynotesapp.adapter.NotesRvAdapter
import com.prplmnstr.mynotesapp.databinding.FragmentHomeBinding
import com.prplmnstr.mynotesapp.model.Note
import com.prplmnstr.mynotesapp.viewmodel.NotesViewModel


class HomeFragment : Fragment() {

    private val notesViewModel: NotesViewModel by activityViewModels()
    private lateinit var rvAdapter: NotesRvAdapter
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addOrUpdateNotesFragment)
        }

        initRecyclerView()
        loadNotes()

    }

    private fun loadNotes() {
        notesViewModel.notes.observe(viewLifecycleOwner, Observer { notes ->


            if (notes.isNullOrEmpty()) {

                binding.recyclerView.visibility = View.GONE
                //  binding.emptyNotesLayout.visibility = View.VISIBLE
            } else {
                binding.recyclerView.visibility = View.VISIBLE
                //  binding.emptyFavoriteLayout.visibility = View.GONE
                rvAdapter.setList(notes)
                rvAdapter.notifyDataSetChanged()

            }
        })
    }

    private fun initRecyclerView() {

        // Initialize RecyclerView
        binding.recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rvAdapter =
            NotesRvAdapter(
                requireActivity(),
                { selectedNote: Note -> updateNote(selectedNote) },
                { selectedNotes: List<Note> ->
                    removeMultipleNotes(
                        selectedNotes
                    )
                },
            )
        binding.recyclerView.adapter = rvAdapter
    }

    private fun updateNote(selectedNote: Note) {


        notesViewModel.insert = false
        notesViewModel.noteObject.value = selectedNote.copy()
        findNavController().navigate(R.id.action_homeFragment_to_addOrUpdateNotesFragment)


    }


    /**
     * Handle removing multiple items .
     */
    private fun removeMultipleNotes(selectedNotes: List<Note>) {
        val itemsToDelete = arrayListOf<Note>()
        itemsToDelete.addAll(selectedNotes)
        var message: String
        if (selectedNotes.size == 1) {
            message = "Item removed."
        } else {
            message = "Items removed."

        }

        for (item in selectedNotes) {

            rvAdapter.removeItem(item)
        }

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            notesViewModel.removeNotes(itemsToDelete)
        }, 500)

        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }


}