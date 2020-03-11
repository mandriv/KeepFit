package com.mandriv.keepfit.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mandriv.keepfit.data.goals.Goal
import com.mandriv.keepfit.data.steps.HistoryEntry
import com.mandriv.keepfit.data.steps.StepsEntry
import com.mandriv.keepfit.databinding.GoalItemBinding
import com.mandriv.keepfit.databinding.HistoryItemBinding
import com.mandriv.keepfit.view.GoalsFragmentDirections

class HistoryAdapter : ListAdapter<HistoryEntry, RecyclerView.ViewHolder>(HistoryEntryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return HistoryItemViewHolder(
            HistoryItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val historyEntry = getItem(position)
        (holder as HistoryItemViewHolder).bind(historyEntry)
    }

    class HistoryItemViewHolder(
        private val binding: HistoryItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.setClickListener {
                binding.historyEntry?.let { historyEntry ->
                    Log.i("AAA", historyEntry.id.toString())
                }
            }
        }


        fun bind(item: HistoryEntry) {
            binding.apply {
                percentageCompleted = "${item.percentageCompleted} %"
                historyEntry = item
                executePendingBindings()
            }
        }
    }
}

private class HistoryEntryDiffCallback: DiffUtil.ItemCallback<HistoryEntry>() {

    override fun areItemsTheSame(oldItem: HistoryEntry, newItem: HistoryEntry): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: HistoryEntry, newItem: HistoryEntry): Boolean {
        return oldItem == newItem
    }
}