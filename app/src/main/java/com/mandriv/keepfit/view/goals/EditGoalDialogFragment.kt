package com.mandriv.keepfit.view.goals

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.navArgs
import com.mandriv.keepfit.R
import com.mandriv.keepfit.databinding.EditGoalDialogFragmentBinding
import com.mandriv.keepfit.utilities.FullScreenDialogFragment
import com.mandriv.keepfit.utilities.InjectorUtils
import com.mandriv.keepfit.viewmodel.goals.EditGoalViewModel
import kotlinx.android.synthetic.main.edit_goal_dialog_fragment.*


class EditGoalDialogFragment : FullScreenDialogFragment() {

    private val args: EditGoalDialogFragmentArgs by navArgs()

    private val editViewModel: EditGoalViewModel by viewModels {
        InjectorUtils.provideEditGoalViewModelFactory(requireContext(), args.goalId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<EditGoalDialogFragmentBinding>(
            inflater, R.layout.edit_goal_dialog_fragment, container, false
        ).apply {
            viewModel = editViewModel
            lifecycleOwner = viewLifecycleOwner
        }
        val toolbar = binding.toolbar
        toolbar.setNavigationOnClickListener { _ -> dismiss() }
        toolbar.setTitle(R.string.edit_goal_title)
        toolbar.inflateMenu(R.menu.save_menu)

        toolbar.setOnMenuItemClickListener { _ -> binding.viewModel!!.onSave() }

        editViewModel.goal.observe(viewLifecycleOwner) { goal ->
            editViewModel.updateFields(goal)
        }
        editViewModel.edited.observe(viewLifecycleOwner) { edited ->
            if (edited) {
                dismiss()
            }
        }
        editViewModel.newGoalName.observe(viewLifecycleOwner) { value ->
            if (value.isNullOrBlank()) {
                edit_goal_name.error = resources.getString(R.string.input_error_name)
            } else {
                edit_goal_name.error = ""
            }
        }
        editViewModel.newGoalValue.observe(viewLifecycleOwner) { value ->
            if (value.isNullOrBlank()) {
                edit_goal_value.error = resources.getString(R.string.input_error_numeric)
            } else {
                edit_goal_value.error = ""
            }
        }
        return binding.root
    }


}