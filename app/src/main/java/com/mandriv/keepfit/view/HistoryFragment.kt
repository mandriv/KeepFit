package com.mandriv.keepfit.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.mandriv.keepfit.R
import com.mandriv.keepfit.databinding.HistoryFragmentBinding
import com.mandriv.keepfit.utilities.InjectorUtils
import com.mandriv.keepfit.viewmodel.history.HistoryViewModel

class HistoryFragment : Fragment() {

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
        return binding.root
    }
}