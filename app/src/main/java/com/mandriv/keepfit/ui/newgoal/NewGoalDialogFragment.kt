package com.mandriv.keepfit.ui.newgoal

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.mandriv.keepfit.R
import com.mandriv.keepfit.databinding.NewGoalDialogFragmentBinding
import com.mandriv.keepfit.utilities.InjectorUtils


class NewGoalDialogFragment: DialogFragment() {

    private val newGoalViewModel: NewGoalViewModel by viewModels {
        InjectorUtils.provideNewGoalViewModelFactory(requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_FullScreenDialog);
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
        var toolbar = binding.toolbar
        toolbar.setNavigationOnClickListener { _ -> dismiss() }
        toolbar.setTitle(R.string.add_new_goal_title)
        toolbar.inflateMenu(R.menu.save_menu)

        fun saveAndDismiss(): Boolean {
            binding.viewModel!!.onSave()
            dismiss()
            return true
        }
        toolbar.setOnMenuItemClickListener { _ -> saveAndDismiss() }
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.MATCH_PARENT
        dialog?.window?.setLayout(width, height)
        // make rounded corners visible
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

}