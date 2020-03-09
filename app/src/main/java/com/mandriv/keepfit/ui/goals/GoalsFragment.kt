package com.mandriv.keepfit.ui.goals

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.mandriv.keepfit.R
import com.mandriv.keepfit.databinding.GoalsFragmentBinding
import com.mandriv.keepfit.utilities.InjectorUtils

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
        binding.fab.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_goals_to_newGoalDialogFragment))
        return binding.root
    }

}