package com.mandriv.keepfit.view

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import androidx.fragment.app.viewModels
import com.mandriv.keepfit.R
import com.mandriv.keepfit.databinding.NewGoalDialogFragmentBinding
import com.mandriv.keepfit.utilities.FullScreenDialogFragment
import com.mandriv.keepfit.utilities.InjectorUtils
import com.mandriv.keepfit.viewmodel.newgoal.NewGoalViewModel


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
        return binding.root
    }

}