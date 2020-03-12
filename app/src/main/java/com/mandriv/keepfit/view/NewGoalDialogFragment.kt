package com.mandriv.keepfit.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.mandriv.keepfit.R
import com.mandriv.keepfit.databinding.NewGoalDialogFragmentBinding
import com.mandriv.keepfit.utilities.FullScreenDialogFragment
import com.mandriv.keepfit.utilities.InjectorUtils
import com.mandriv.keepfit.viewmodel.newgoal.NewGoalViewModel
import kotlinx.android.synthetic.main.new_goal_dialog_fragment.*


class NewGoalDialogFragment: FullScreenDialogFragment() {

    private val newGoalViewModel: NewGoalViewModel by viewModels {
        InjectorUtils.provideNewGoalViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<NewGoalDialogFragmentBinding>(
            inflater, R.layout.new_goal_dialog_fragment, container, false
        ).apply {
            viewModel = newGoalViewModel
            lifecycleOwner = viewLifecycleOwner
        }
        val toolbar = binding.toolbar
        toolbar.setNavigationOnClickListener { _ -> dismiss() }
        toolbar.setTitle(R.string.add_new_goal_title)
        toolbar.inflateMenu(R.menu.save_menu)

        toolbar.setOnMenuItemClickListener { _ -> binding.viewModel!!.onSave() }
        newGoalViewModel.inserted.observe(viewLifecycleOwner) { inserted ->
            if (inserted) {
                dismiss()
            }
        }
        newGoalViewModel.newGoalName.observe(viewLifecycleOwner) { value ->
            if (newGoalViewModel.errorEnabled.value == true) {
                if (value.isNullOrBlank()) {
                    new_goal_name.error = resources.getString(R.string.input_error_name)
                } else {
                    new_goal_name.error = ""
                }
            }
        }
        newGoalViewModel.newGoalValue.observe(viewLifecycleOwner) { value ->
            if (newGoalViewModel.errorEnabled.value == true) {
                if (value.isNullOrBlank()) {
                    new_goal_value.error = resources.getString(R.string.input_error_numeric)
                } else {
                    new_goal_value.error = ""
                }
            }
        }
        newGoalViewModel.errorEnabled.observe(viewLifecycleOwner) { enabled ->
            if (enabled) {
                if (newGoalViewModel.newGoalName.value.isNullOrBlank()) {
                    new_goal_name.error = resources.getString(R.string.input_error_name)
                } else {
                    new_goal_name.error = ""
                }
                if (newGoalViewModel.newGoalValue.value.isNullOrBlank()) {
                    new_goal_value.error = resources.getString(R.string.input_error_numeric)
                } else {
                    new_goal_value.error = ""
                }
            }
        }
        return binding.root
    }

}