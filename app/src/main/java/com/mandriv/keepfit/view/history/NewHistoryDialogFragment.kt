package com.mandriv.keepfit.view.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.mandriv.keepfit.R
import com.mandriv.keepfit.databinding.AddHistoryEntryFragmentBinding
import com.mandriv.keepfit.utilities.FullScreenDialogFragment
import com.mandriv.keepfit.utilities.InjectorUtils
import com.mandriv.keepfit.viewmodel.history.NewHistoryViewModel


class NewHistoryDialogFragment : FullScreenDialogFragment() {

    private val newHistoryViewModel: NewHistoryViewModel by viewModels {
        InjectorUtils.provideNewHistoryViewModelFactory(requireContext())
    }
    private lateinit var datePicker: MaterialDatePicker<Long>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<AddHistoryEntryFragmentBinding>(
            inflater, R.layout.add_history_entry_fragment, container, false
        ).apply {
            viewModel = newHistoryViewModel
            lifecycleOwner = viewLifecycleOwner
        }
        // toolbar
        val toolbar = binding.toolbar
        toolbar.setNavigationOnClickListener { _ -> dismiss() }
        toolbar.setOnMenuItemClickListener { _ -> binding.viewModel!!.onSave(requireContext()) }
        toolbar.setTitle(R.string.add_historical_activity)
        toolbar.inflateMenu(R.menu.save_menu)
        // date picker
        datePicker = createDatePicker()
        datePicker.addOnPositiveButtonClickListener {
            newHistoryViewModel.onDate(it)
        }
        binding.dateInput.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                fragmentManager?.let { datePicker.show(it, datePicker.toString()) }
                binding.dateInput.clearFocus()
            }
        }
        // dropdown
        val dropdownAdapter =
            ArrayAdapter<String>(requireContext(), R.layout.dropdown_menu_popup_item, ArrayList<String>())
        binding.goalAutocomplete.setAdapter(dropdownAdapter)
        newHistoryViewModel.goalNames.observe(viewLifecycleOwner) { goals ->
            dropdownAdapter.clear()
            dropdownAdapter.addAll(goals)
        }
        // closing
        newHistoryViewModel.close.observe(viewLifecycleOwner) { close -> if (close) dismiss() }
        // error handling
        newHistoryViewModel.badDateError.observe(viewLifecycleOwner) { err ->
            if (newHistoryViewModel.submitted.value == true) {
                if (err) {
                    binding.dateLayout.error = resources.getString(R.string.bad_date_error)
                } else {
                    binding.dateLayout.error = ""
                }
            }
        }
        newHistoryViewModel.dateString.observe(viewLifecycleOwner) { str ->
            if (newHistoryViewModel.submitted.value == true) {
                if (str.isNullOrBlank()) {
                    binding.dateLayout.error = resources.getString(R.string.input_date_error)
                } else {
                    binding.dateLayout.error = ""
                }
            }
        }
        newHistoryViewModel.goalName.observe(viewLifecycleOwner) { str ->
            if (newHistoryViewModel.submitted.value == true) {
                if (str.isNullOrBlank()) {
                    binding.goalLayout.error = resources.getString(R.string.input_list_error)
                } else {
                    binding.goalLayout.error = ""
                }
            }

        }
        newHistoryViewModel.steps.observe(viewLifecycleOwner) { str ->
            if (newHistoryViewModel.submitted.value == true) {
                if (str.isNullOrBlank()) {
                    binding.stepsLayout.error = resources.getString(R.string.input_error_numeric)
                } else {
                    binding.stepsLayout.error = ""
                }
            }
        }
        newHistoryViewModel.submitted.observe((viewLifecycleOwner)) { submitted ->
            if (submitted) {
                if (newHistoryViewModel.dateString.value.isNullOrBlank()) {
                    binding.dateLayout.error = resources.getString(R.string.input_date_error)
                } else {
                    binding.dateLayout.error = ""
                }
                if (newHistoryViewModel.goalName.value.isNullOrBlank()) {
                    binding.goalLayout.error = resources.getString(R.string.input_list_error)
                } else {
                    binding.goalLayout.error = ""
                }
                if (newHistoryViewModel.steps.value.isNullOrBlank()) {
                    binding.stepsLayout.error = resources.getString(R.string.input_error_numeric)
                } else {
                    binding.stepsLayout.error = ""
                }
            }
        }
        return binding.root
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