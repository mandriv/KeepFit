package com.mandriv.keepfit.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.navigation.fragment.navArgs
import com.mandriv.keepfit.R
import com.mandriv.keepfit.data.goals.Goal
import com.mandriv.keepfit.databinding.EditGoalDialogFragmentBinding
import com.mandriv.keepfit.utilities.FullScreenDialogFragment
import com.mandriv.keepfit.utilities.InjectorUtils
import com.mandriv.keepfit.viewmodel.editgoal.EditGoalViewModel


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
        return binding.root
    }


}