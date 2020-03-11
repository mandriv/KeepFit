package com.mandriv.keepfit.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.mandriv.keepfit.R
import com.mandriv.keepfit.adapters.GoalAdapter
import com.mandriv.keepfit.adapters.HistoryAdapter
import com.mandriv.keepfit.databinding.HistoryFragmentBinding
import com.mandriv.keepfit.utilities.InjectorUtils
import com.mandriv.keepfit.viewmodel.history.HistoryViewModel

class HistoryFragment : Fragment() {

    private lateinit var adapter: HistoryAdapter
    private val historyViewModel: HistoryViewModel by viewModels {
        InjectorUtils.provideHistoryModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<HistoryFragmentBinding>(
            inflater, R.layout.history_fragment, container, false
        ).apply {
            viewModel = historyViewModel
            lifecycleOwner = viewLifecycleOwner
        }

        adapter = HistoryAdapter()
        binding.historyList.adapter = adapter

        subscribeUi()

        return binding.root
    }

    private fun subscribeUi() {
        historyViewModel.entries.observe(viewLifecycleOwner) { entries ->
            // entries.forEach { entry -> Log.i("AAA", entry.toString()) }
            adapter.submitList(entries)
        }
    }
}