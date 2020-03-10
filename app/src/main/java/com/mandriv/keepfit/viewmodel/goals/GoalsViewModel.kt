package com.mandriv.keepfit.viewmodel.goals

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.mandriv.keepfit.data.goals.Goal
import com.mandriv.keepfit.data.goals.GoalRepository

// Class extends AndroidViewModel and requires application as a parameter.
class GoalsViewModel internal constructor(goalsRepository: GoalRepository) : ViewModel() {
    private val activeGoal: LiveData<Goal> = goalsRepository.getActiveGoal()

    val activeGoalName: LiveData<String> = Transformations.map(activeGoal) { goal ->
        if (goal == null) "" else goal.name
    }

    val activeGoalValue: LiveData<String> = Transformations.map(activeGoal) { goal ->
        if (goal == null) "" else goal.value.toString()
    }

    val inactiveGoals: LiveData<List<Goal>> = goalsRepository.getInactiveGoals()

}