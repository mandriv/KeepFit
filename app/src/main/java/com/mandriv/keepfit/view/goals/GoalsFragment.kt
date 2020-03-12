package com.mandriv.keepfit.view.goals

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.Navigation
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.mandriv.keepfit.R
import com.mandriv.keepfit.adapters.GoalAdapter
import com.mandriv.keepfit.data.goals.Goal
import com.mandriv.keepfit.databinding.GoalsFragmentBinding
import com.mandriv.keepfit.utilities.FragmentWithSettingsMenu
import com.mandriv.keepfit.utilities.InjectorUtils
import com.mandriv.keepfit.utilities.SwipeToDeleteCallback
import com.mandriv.keepfit.viewmodel.goals.GoalsViewModel
import kotlinx.android.synthetic.main.goals_fragment.*
import kotlinx.android.synthetic.main.goals_fragment.fab
import kotlinx.android.synthetic.main.history_fragment.*


class GoalsFragment : FragmentWithSettingsMenu() {

    private lateinit var adapter: GoalAdapter
    private val goalsViewModel: GoalsViewModel by viewModels {
        InjectorUtils.provideGoalsViewModelFactory(requireContext())
    }
    private var allowGoalAdd = true
    private var allowGoalDelete = true

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

        setupPreferences()
        adapter = GoalAdapter()
        binding.goalList.adapter = adapter
        subscribeUi()
        if (allowGoalAdd) {
            binding.fab.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_goals_to_newGoalDialogFragment))
        } else {
            binding.fab.hide()
        }

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (allowGoalAdd) {
            enableHidingFabOnScroll()
        }
        if (allowGoalDelete) {
            enableSwipeToDeleteAndUndo()
        }
    }


    private fun subscribeUi() {
        goalsViewModel.inactiveGoals.observe(viewLifecycleOwner) { goals ->
            adapter.submitList(goals)
        }
    }

    private fun setupPreferences() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        allowGoalAdd = sharedPreferences.getBoolean("allow_goal_add", true)
        allowGoalDelete = sharedPreferences.getBoolean("allow_goal_delete", true)
    }

    private fun enableHidingFabOnScroll() {
        goal_list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0 || dy < 0 && fab.isShown) fab.hide()
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) fab.show()
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
    }

    private fun enableSwipeToDeleteAndUndo() {
        val swipeToDeleteCallback: SwipeToDeleteCallback = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, i: Int) {
                val position = viewHolder.adapterPosition
                val goalToRemove: Goal = adapter.currentList[position]
                goalsViewModel.removeGoal(goalToRemove)
                val snackbar = Snackbar.make(
                    history_root,
                    "Item was removed from the list.",
                    Snackbar.LENGTH_LONG
                )
                snackbar.setAction("UNDO") {
                    goalsViewModel.restoreGoal(goalToRemove)
                }
                snackbar.show()
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(goal_list)
    }

}