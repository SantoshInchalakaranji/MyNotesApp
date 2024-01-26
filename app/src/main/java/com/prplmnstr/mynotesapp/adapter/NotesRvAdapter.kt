package com.prplmnstr.mynotesapp.adapter

import android.os.Handler
import android.os.Looper
import android.view.ActionMode
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.prplmnstr.mynotesapp.R
import com.prplmnstr.mynotesapp.databinding.NotesItemBinding
import com.prplmnstr.mynotesapp.model.Note

class NotesRvAdapter(
    val requireActivity: FragmentActivity,
    private val itemClickListener: (Note) -> Unit,

    private val removeNoteClickListener: (List<Note>) -> Unit,
) : RecyclerView.Adapter<NotesRvAdapter.ViewHolder>(), ActionMode.Callback {

    // Flag for multi-selection mode
    private var multiSelection = false

    // ActionMode instance
    private lateinit var mActionMode: ActionMode

    // List to store selected favorite items
    private var selectedNotes = mutableListOf<Note>()

    // List to store ViewHolders for selected items
    private var myViewHolders = arrayListOf<ViewHolder>()

    // List to store favorite items
    private val notes = ArrayList<Note>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: NotesItemBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.notes_item, parent, false)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        myViewHolders.add(holder)
        holder.bind(
            notes[position],
            itemClickListener,
            removeNoteClickListener
        )

        /**  Long click listener for initiating multi-selection mode **/
        holder.binding.parentCard.setOnLongClickListener {
            if (!multiSelection) {
                multiSelection = true
                requireActivity.startActionMode(this)
                applySelection(holder, notes[position])
                true
            } else {
                multiSelection = false
                false
            }

        }

        // Click listener for handling item selection in multi-selection mode
        holder.binding.parentCard.setOnClickListener {
            if (multiSelection) {
                applySelection(holder, notes[position])
            } else {

                itemClickListener(notes[position])

            }


        }
    }

    fun setList(items: List<Note>) {
        notes.clear()
        notes.addAll(items)
    }

    fun removeItem(note: Note) {
        val position = notes.indexOf(note)
        notes.remove(note)
        notifyItemRemoved(position)
    }

    private fun applySelection(holder: ViewHolder, note: Note) {

        if (selectedNotes.contains(note)) {
            selectedNotes.remove(note)
            changeItemStyle(holder, R.color.white, R.color.transparent)


        } else {
            selectedNotes.add(note)
            changeItemStyle(holder, R.color.card_selection_color, R.color.orange)
        }
        applyActionModeTitle()


    }

    private fun applyActionModeTitle() {
        when (selectedNotes.size) {
            0 -> {
                mActionMode.finish()
            }

            1 -> {

                mActionMode.title = "${selectedNotes.size} item selected"
            }

            else -> {
                mActionMode.title = "${selectedNotes.size} items selected"
            }
        }

    }

    private fun changeItemStyle(holder: ViewHolder, backgroundColor: Int, strokeColor: Int) {
        holder.binding.parentCard.setBackgroundColor(
            ContextCompat.getColor(requireActivity, backgroundColor)
        )
        holder.binding.parentCard.strokeColor = ContextCompat.getColor(requireActivity, strokeColor)
    }

    override fun getItemCount(): Int {
        return notes.size
    }


    class ViewHolder(val binding: NotesItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            note: Note,
            itemClickListener: (Note) -> Unit,
            removeNoteClickListener: (List<Note>) -> Unit,
        ) {

            binding.titleTv.text = note.title
            binding.duedateTv.text = note.creationDate
            binding.descriptionTv.text = note.description


        }
    }

    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        mode?.menuInflater?.inflate(R.menu.delete_menu, menu)
        mActionMode = mode!!
        addColorToStatusBar(R.color.black_gradient_1)

        return true
    }

    private fun addColorToStatusBar(color: Int) {
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            requireActivity.window.statusBarColor = ContextCompat.getColor(requireActivity, color)
        }, 200)

    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        return true
    }

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        if (item?.itemId == R.id.delete_item) {
            removeNoteClickListener(selectedNotes)
            multiSelection = false
            selectedNotes.clear()
            mode?.finish()
        }
        return true
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        multiSelection = false
        selectedNotes.clear()
        myViewHolders.forEach { holder ->
            changeItemStyle(holder, R.color.white, R.color.transparent)
        }
        addColorToStatusBar(R.color.dark_gray)
    }


}

