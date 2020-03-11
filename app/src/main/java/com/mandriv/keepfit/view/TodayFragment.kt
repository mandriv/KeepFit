package com.mandriv.keepfit.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.mandriv.keepfit.R
import com.mandriv.keepfit.databinding.TodayFragmentBinding
import com.mandriv.keepfit.utilities.InjectorUtils
import com.mandriv.keepfit.viewmodel.today.TodayViewModel
import kotlinx.android.synthetic.main.today_fragment.*

class TodayFragment: Fragment() {

    private val todayViewModel: TodayViewModel by viewModels {
        InjectorUtils.provideTodayViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<TodayFragmentBinding>(
            inflater, R.layout.today_fragment, container, false
        ).apply {
            viewModel = todayViewModel
            lifecycleOwner = viewLifecycleOwner
        }

        todayViewModel.percentageCompleted.observe(viewLifecycleOwner) { percentage ->
            seekArc.progress = percentage.toInt()
        }

        return binding.root
    }

}