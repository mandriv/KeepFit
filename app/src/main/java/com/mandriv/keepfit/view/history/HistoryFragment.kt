package com.mandriv.keepfit.view.history

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.Navigation
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.mandriv.keepfit.R
import com.mandriv.keepfit.adapters.HistoryAdapter
import com.mandriv.keepfit.data.steps.HistoryEntry
import com.mandriv.keepfit.databinding.HistoryFragmentBinding
import com.mandriv.keepfit.utilities.FragmentWithSettingsMenu
import com.mandriv.keepfit.utilities.InjectorUtils
import com.mandriv.keepfit.utilities.SwipeToDeleteCallback
import com.mandriv.keepfit.viewmodel.history.HistoryViewModel
import kotlinx.android.synthetic.main.goals_fragment.fab
import kotlinx.android.synthetic.main.history_fragment.*

class HistoryFragment : FragmentWithSettingsMenu() {

    private lateinit var adapter: HistoryAdapter
    private val historyViewModel: HistoryViewModel by viewModels {
        InjectorUtils.provideHistoryModelFactory(requireContext())
    }
    private var allowHistoryAdd: Boolean = true
    private var allowHistoryEdit: Boolean = true
    private var allowHistoryDelete: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<HistoryFragmentBinding>(
            inflater, R.layout.history_fragment, container, false
        ).apply {
            viewModel = historyViewModel
            lifecycleOwner = viewLifecycleOwner
        }

        adapter = HistoryAdapter()
        binding.historyList.adapter = adapter

        setupPreferences()
        if (allowHistoryAdd) {
            binding.fab.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_history_to_addHistoryEntryFragment))
        } else {
            binding.fab.hide()
        }

        subscribeUi()
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (allowHistoryAdd) {
            enableHidingFabOnScroll()
        }
        if (allowHistoryDelete) {
            enableSwipeToDeleteAndUndo()
        }
    }

    private fun setupPreferences() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        allowHistoryAdd = sharedPreferences.getBoolean("allow_history_add", true)
        allowHistoryEdit = sharedPreferences.getBoolean("allow_history_edit", true)
        allowHistoryDelete = sharedPreferences.getBoolean("allow_history_delete", true)
    }

    private fun subscribeUi() {
        historyViewModel.entries.observe(viewLifecycleOwner) { entries ->
            // entries.forEach { entry -> Log.i("AAA", entry.toString()) }
            adapter.submitList(entries)
        }
    }

    private fun enableHidingFabOnScroll() {
        history_list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0 || dy < 0 && fab.isShown) fab.hide()
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) fab.show()
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
    }

    private fun enableSwipeToDeleteAndUndo() {
        val swipeToDeleteCallback: SwipeToDeleteCallback = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, i: Int) {
                Log.i("AAA", "swiped")
                val position = viewHolder.adapterPosition
                val historyEntryToRemove: HistoryEntry = adapter.currentList[position]
                historyViewModel.removeEntry(historyEntryToRemove)
                view?.let {
                    val snackbar = Snackbar.make(
                        it,
                        "Item was removed from the list.",
                        Snackbar.LENGTH_LONG
                    )
                    snackbar.setAction("UNDO") {
                        historyViewModel.restoreEntry(historyEntryToRemove)
                    }
                    snackbar.show()
                }
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(history_list)
    }
}