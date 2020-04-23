package com.mandriv.keepfit.adapters

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mandriv.keepfit.data.steps.HistoryEntry
import com.mandriv.keepfit.databinding.HistoryItemBinding
import com.mandriv.keepfit.view.history.HistoryFragmentDirections
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


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
                    navigateToEntry(historyEntry.id, historyEntry.goalId, it)
                }
            }
        }

        fun bind(item: HistoryEntry) {
            binding.apply {
                val today = Calendar.getInstance()
                val locale = getCurrentLocale(binding.root.context)
                val formatDay = SimpleDateFormat("EEEE", locale)
                val formatDayMonth = SimpleDateFormat("d MMM", locale)
                val formatDayMonthYear = SimpleDateFormat("d MMM YYYY", locale)
                val daysBetween = daysBetween(item.date, today)
                entryTime = when {
                    daysBetween == 0 -> {
                        "Today"
                    }
                    daysBetween < 7 -> {
                        formatDay.format(item.date.time)
                    }
                    daysBetween < 365 -> {
                        formatDayMonth.format(item.date.time)
                    }
                    else -> {
                        formatDayMonthYear.format(item.date.time)
                    }
                }
                stepsFormatted = "${item.stepCount} steps"
                percentage = item.percentageCompleted
                percentageCompleted = "${item.percentageCompleted}%"
                historyEntry = item
                executePendingBindings()
            }
        }

        private fun navigateToEntry(
            stepEntryId: Int,
            goalId: Int,
            view: View
        ) {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(binding.root.context)
            val allowEntryEdit = sharedPreferences.getBoolean("allow_history_edit", true)
            if (allowEntryEdit) {
                val direction = HistoryFragmentDirections.actionHistoryToEditHistoryDialogFragment(
                    stepEntryId,
                    goalId
                )
                view.findNavController().navigate(direction)
            }
        }

        private fun getCurrentLocale(context: Context): Locale {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                context.resources.configuration.locales.get(0)
            } else {
                //noinspection deprecation
                context.resources.configuration.locale
            }
        }

        private fun daysBetween(startDate: Calendar, endDate: Calendar): Int {
            val start = Calendar.getInstance().apply {
                timeInMillis = 0
                set(Calendar.DAY_OF_YEAR, startDate.get(Calendar.DAY_OF_YEAR))
                set(Calendar.YEAR, startDate.get(Calendar.YEAR))
            }.timeInMillis
            val end = Calendar.getInstance().apply {
                timeInMillis = 0
                set(Calendar.DAY_OF_YEAR, endDate.get(Calendar.DAY_OF_YEAR))
                set(Calendar.YEAR, endDate.get(Calendar.YEAR))
            }.timeInMillis
            val differenceMillis = end - start
            return TimeUnit.MILLISECONDS.toDays(differenceMillis).toInt()
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