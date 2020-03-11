package com.mandriv.keepfit.viewmodel.goals

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mandriv.keepfit.data.goals.Goal
import com.mandriv.keepfit.data.goals.GoalRepository
import kotlinx.coroutines.launch

// Class extends AndroidViewModel and requires application as a parameter.
class GoalsViewModel(private val goalsRepository: GoalRepository) : ViewModel() {
    private val activeGoal: LiveData<Goal> = goalsRepository.getActiveGoal()

    val activeGoalName: LiveData<String> = Transformations.map(activeGoal) { goal ->
        goal?.name ?: ""
    }

    val activeGoalValue: LiveData<String> = Transformations.map(activeGoal) { goal ->
        goal?.value?.toString() ?: ""
    }

    val inactiveGoals: LiveData<List<Goal>> = goalsRepository.getInactiveGoals()

    fun removeGoal(goal: Goal) {
        viewModelScope.launch {
            goalsRepository.delete(goal)
        }
    }

    fun restoreGoal(goal: Goal) {
        viewModelScope.launch {
            val restoredGoal = Goal(goal.id, goal.value, goal.name, false, false)
            goalsRepository.update(restoredGoal)
        }
    }

}