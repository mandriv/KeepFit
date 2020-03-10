package com.mandriv.keepfit.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mandriv.keepfit.data.goals.Goal
import com.mandriv.keepfit.databinding.GoalItemBinding

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
            /*
            binding.setClickListener {
                binding.plant?.let { plant ->
                    navigateToPlant(plant, it)
                }
            }
             */
        }

        /*
        private fun navigateToPlant(
            plant: Plant,
            view: View
        ) {
            val direction =
                HomeViewPagerFragmentDirections.actionViewPagerFragmentToPlantDetailFragment(
                    plant.plantId
                )
            view.findNavController().navigate(direction)
        }
         */

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