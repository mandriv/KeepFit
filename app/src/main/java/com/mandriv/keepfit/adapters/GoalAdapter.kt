package com.mandriv.keepfit.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mandriv.keepfit.R
import com.mandriv.keepfit.data.goals.Goal
import com.mandriv.keepfit.databinding.GoalItemBinding
import com.mandriv.keepfit.view.GoalsFragmentDirections

class GoalAdapter : ListAdapter<Goal, RecyclerView.ViewHolder>(GoalDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return GoalViewHolder(
            GoalItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val goal = getItem(position)
        (holder as GoalViewHolder).bind(goal)
    }

    class GoalViewHolder(
        private val binding: GoalItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.setClickListener {
                binding.goal?.let { goal ->
                    navigateToGoal(goal, it)
                }
            }
        }

        private fun getClickListener(goalId: Int): View.OnClickListener {
            val listener = Navigation.createNavigateOnClickListener(
                R.id.action_goals_to_editGoalDialogFragment,
                bundleOf("goalId" to goalId)
            )
            return listener
        }

        private fun navigateToGoal(
            goal: Goal,
            view: View
        ) {
            val direction =
                GoalsFragmentDirections.actionGoalsToEditGoalDialogFragment(goal.id)
            view.findNavController().navigate(direction)
        }


        fun bind(item: Goal) {
            binding.apply {
                goal = item
                executePendingBindings()
            }
        }
    }
}

private class GoalDiffCallback : DiffUtil.ItemCallback<Goal>() {

    override fun areItemsTheSame(oldItem: Goal, newItem: Goal): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Goal, newItem: Goal): Boolean {
        return oldItem == newItem
    }
}