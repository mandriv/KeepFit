package com.mandriv.keepfit.viewmodel.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mandriv.keepfit.data.goals.GoalRepository
import com.mandriv.keepfit.data.steps.StepsRepository

class EditHistoryViewModelFactory(
    private val stepsRepository: StepsRepository,
    private val goalRepository: GoalRepository,
    private val stepEntryId: Int,
    private val goalId: Int
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return EditHistoryViewModel(
            stepsRepository,
            goalRepository,
            stepEntryId,
            goalId
        ) as T
    }
}