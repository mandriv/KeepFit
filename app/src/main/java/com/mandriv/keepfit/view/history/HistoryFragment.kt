package com.mandriv.keepfit.view.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.Navigation
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
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

    private lateinit var datePicker: MaterialDatePicker<Long>

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
        // date picker
        datePicker = createDatePicker()
        datePicker.addOnPositiveButtonClickListener {
            val manager = binding.historyList.layoutManager as LinearLayoutManager
            val list = adapter.currentList
            var positionToJump = 0
            if (list.size > 3) {
                for (i in list.indices) {
                    if (i != list.indices.last) {
                        val current = list[i].date.time.time
                        val next = list[i + 1].date.time.time
                        if (it in next..current) {
                            manager.scrollToPositionWithOffset(i, 0)
                        }
                    }
                }
            }

        }
        binding.dateChip.setOnClickListener {
            fragmentManager?.let { it1 -> datePicker.show(it1, datePicker.tag) }
        }

        historyViewModel.entries.observe(viewLifecycleOwner) { entries ->
            if (entries.isEmpty()) {
                no_data_text.visibility = View.VISIBLE
            } else {
                no_data_text.visibility = View.INVISIBLE
            }
            adapter.submitList(entries)
        }
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
        enableHidingChipOnScroll()
    }

    private fun setupPreferences() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        allowHistoryAdd = sharedPreferences.getBoolean("allow_history_add", true)
        allowHistoryEdit = sharedPreferences.getBoolean("allow_history_edit", true)
        allowHistoryDelete = sharedPreferences.getBoolean("allow_history_delete", true)
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

    private fun enableHidingChipOnScroll() {
        history_list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0 || dy < 0 && date_chip.isShown) date_chip.animate().alpha(0.0f)
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) date_chip.animate().alpha(1.0f)
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
    }

    private fun enableSwipeToDeleteAndUndo() {
        val swipeToDeleteCallback: SwipeToDeleteCallback = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, i: Int) {
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

    private fun createDatePicker(): MaterialDatePicker<Long> {
        val builder = MaterialDatePicker.Builder.datePicker()
        val now: Long = System.currentTimeMillis()
        val tenYearsAgo = now - 315576000000L
        builder.setCalendarConstraints(
            CalendarConstraints.Builder()
                .setStart(tenYearsAgo)
                .setEnd(now).build()
        )
        return builder.build()
    }
}