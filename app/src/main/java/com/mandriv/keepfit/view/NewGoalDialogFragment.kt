package com.mandriv.keepfit.view

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
import com.mandriv.keepfit.viewmodel.newgoal.NewGoalViewModel


class NewGoalDialogFragment: DialogFragment() {

    private val newGoalViewModel: NewGoalViewModel by viewModels {
        InjectorUtils.provideNewGoalViewModelFactory(requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppTheme_FullScreenDialog)
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
        // make it full screen
        dialog?.window?.setLayout(width, height)
        // make rounded corners visible
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        // add animation
        dialog?.window?.setWindowAnimations(R.style.AppTheme_Slide)
    }

}