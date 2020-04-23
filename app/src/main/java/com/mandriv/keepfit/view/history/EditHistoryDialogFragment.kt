package com.mandriv.keepfit.view.history

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.navArgs
import com.mandriv.keepfit.R
import com.mandriv.keepfit.databinding.EditHistoryEntryFragmentBinding
import com.mandriv.keepfit.utilities.FullScreenDialogFragment
import com.mandriv.keepfit.utilities.InjectorUtils
import com.mandriv.keepfit.viewmodel.history.EditHistoryViewModel
import kotlinx.android.synthetic.main.edit_history_entry_fragment.*

class EditHistoryDialogFragment : FullScreenDialogFragment() {
    private val args: EditHistoryDialogFragmentArgs by navArgs()
    private val editHistoryViewModel: EditHistoryViewModel by viewModels {
        InjectorUtils.provideEditHistoryViewModelFactory(requireContext(), args.stepEntryId, args.goalId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<EditHistoryEntryFragmentBinding>(
            inflater, R.layout.edit_history_entry_fragment, container, false
        ).apply {
            viewModel = editHistoryViewModel
            lifecycleOwner = viewLifecycleOwner
        }
        // toolbar
        val toolbar = binding.toolbar
        toolbar.setNavigationOnClickListener { _ -> dismiss() }
        toolbar.setTitle(R.string.edit_history_entry)
        toolbar.inflateMenu(R.menu.save_menu)
        toolbar.setOnMenuItemClickListener { _ -> editHistoryViewModel.onSave() }
        // fill fields
        editHistoryViewModel.stepsEntry.observe(viewLifecycleOwner) {
            editHistoryViewModel.updateSteps(it.stepCount)
        }
        val dropdownAdapter =
            ArrayAdapter<String>(requireContext(), R.layout.dropdown_menu_popup_item, ArrayList<String>())
        binding.goalAutocomplete.setAdapter(dropdownAdapter)
        editHistoryViewModel.goalNames.observe(viewLifecycleOwner) { goals ->
            val isEmpty = dropdownAdapter.isEmpty
            dropdownAdapter.addAll(goals)
            if (isEmpty) {
                dropdownAdapter.sort { o1, o2 ->
                    o1.substring(0, o1.length - 6).split(" - ")[1].toInt()
                        .compareTo(o2.substring(0, o2.length - 6).split(" - ")[1].toInt())
                }
            }
        }
        editHistoryViewModel.goal.observe(viewLifecycleOwner) {
            val goalName = "${it.name} - ${it.value} steps"
            if (dropdownAdapter.getPosition(goalName) == -1) {
                dropdownAdapter.add(it.name)
                dropdownAdapter.sort { o1, o2 ->
                    o1.substring(0, o1.length - 6).split(" - ")[1].toInt()
                        .compareTo(o2.substring(0, o2.length - 6).split(" - ")[1].toInt())
                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                goal_autocomplete.setText(goalName, false)
            }
        }
        // error handling
        editHistoryViewModel.goalName.observe(viewLifecycleOwner) { name ->
            if (name.isNullOrBlank()) {
                binding.goalLayout.error = resources.getString(R.string.input_list_error)
            } else {
                binding.goalLayout.error = ""
            }
        }
        editHistoryViewModel.steps.observe(viewLifecycleOwner) { name ->
            if (name.isNullOrBlank()) {
                binding.stepsLayout.error = resources.getString(R.string.input_error_numeric)
            } else {
                binding.stepsLayout.error = ""
            }
        }
        // close
        editHistoryViewModel.close.observe(viewLifecycleOwner) { close -> if (close) dismiss() }
        return binding.root
    }
}