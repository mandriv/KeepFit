package com.mandriv.keepfit.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.Navigation
import com.mandriv.keepfit.R
import com.mandriv.keepfit.adapters.GoalAdapter
import com.mandriv.keepfit.databinding.GoalsFragmentBinding
import com.mandriv.keepfit.utilities.InjectorUtils
import com.mandriv.keepfit.viewmodel.goals.GoalsViewModel

class GoalsFragment: Fragment() {

    private val goalsViewModel: GoalsViewModel by viewModels {
        InjectorUtils.provideGoalsViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<GoalsFragmentBinding>(
            inflater, R.layout.goals_fragment, container, false
        ).apply {
            viewModel = goalsViewModel
            lifecycleOwner = viewLifecycleOwner
        }

        val adapter = GoalAdapter()
        binding.goalList.adapter = adapter
        subscribeUi(adapter)

        binding.fab.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_goals_to_newGoalDialogFragment))
        return binding.root
    }

    private fun subscribeUi(adapter: GoalAdapter) {
        goalsViewModel.inactiveGoals.observe(viewLifecycleOwner) { goals ->
            adapter.submitList(goals)
            adapter.notifyDataSetChanged()
        }
    }

}