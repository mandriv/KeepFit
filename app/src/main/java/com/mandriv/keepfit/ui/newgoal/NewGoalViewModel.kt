package com.mandriv.keepfit.ui.newgoal

import androidx.lifecycle.*
import com.mandriv.keepfit.data.goals.Goal
import com.mandriv.keepfit.data.goals.GoalRepository
import kotlinx.coroutines.launch

class NewGoalViewModel(
    private val goalsRepository: GoalRepository
) : ViewModel() {

    val newGoalName = MutableLiveData<String>()
    val newGoalValue = MutableLiveData<String>()
    val newGoalActive = MutableLiveData<Boolean>()

    private fun insert(goal: Goal) {
        viewModelScope.launch {
            goalsRepository.insert(goal)
        }
    }

    private fun resetCurrenlyActiveGoals() {
        viewModelScope.launch {
            goalsRepository.resetActiveGoals()
        }
    }

    fun onSave(): Boolean {
        if (newGoalValue.value != null && newGoalName.value != null && newGoalActive.value != null) {
            val value: Int = newGoalValue.value!!.toInt()
            val name: String = newGoalName.value!!
            val isActive: Boolean = newGoalActive.value!!

            val newGoal = Goal(0, value, name, isActive)
            if (isActive)
            insert(newGoal)
            return true
        }
        return false
    }

}