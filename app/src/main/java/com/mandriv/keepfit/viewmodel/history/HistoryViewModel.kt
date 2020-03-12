package com.mandriv.keepfit.viewmodel.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mandriv.keepfit.data.steps.HistoryEntry
import com.mandriv.keepfit.data.steps.StepsEntry
import com.mandriv.keepfit.data.steps.StepsRepository
import kotlinx.coroutines.launch

class HistoryViewModel(private val stepsRepository: StepsRepository) : ViewModel() {

    val entries = stepsRepository.getHistoryEntries()

    fun removeEntry(historyEntry: HistoryEntry) {
        viewModelScope.launch {
            stepsRepository.delete(
                StepsEntry(
                    historyEntry.id,
                    historyEntry.stepCount,
                    historyEntry.date,
                    historyEntry.goalId
                )
            )
        }
    }

    fun restoreEntry(historyEntry: HistoryEntry) {
        viewModelScope.launch {
            stepsRepository.insert(
                StepsEntry(
                    historyEntry.id,
                    historyEntry.stepCount,
                    historyEntry.date,
                    historyEntry.goalId
                )
            )

        }
    }

}