package com.mandriv.keepfit.ui.goals

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mandriv.keepfit.R
import com.mandriv.keepfit.databinding.GoalsFragmentBinding
import com.mandriv.keepfit.utilities.InjectorUtils
import kotlinx.android.synthetic.main.goals_fragment.*

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
        return binding.root
    }

}