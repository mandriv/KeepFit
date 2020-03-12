package com.mandriv.keepfit.viewmodel.goals

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mandriv.keepfit.data.goals.Goal
import com.mandriv.keepfit.data.goals.GoalRepository
import kotlinx.coroutines.launch

class NewGoalViewModel(
    private val goalsRepository: GoalRepository
) : ViewModel() {

    val newGoalName = MutableLiveData<String>()
    val newGoalValue = MutableLiveData<String>()
    val newGoalActive = MutableLiveData<Boolean>()

    val inserted = MutableLiveData<Boolean>(false)
    val errorEnabled = MutableLiveData<Boolean>(false)

    private fun insert(goal: Goal) {
        viewModelScope.launch {
            goalsRepository.insert(goal)
            inserted.value = true
        }
    }

    fun onSave(): Boolean {
        errorEnabled.value = true
        if (newGoalValue.value.isNullOrBlank() || newGoalName.value.isNullOrBlank()) {
            return false
        }

        val value: Int = newGoalValue.value!!.toInt()
        val name: String = newGoalName.value!!
        val isActive: Boolean = newGoalActive.value ?: false
        val newGoal = Goal(0, value, name, isActive)
        insert(newGoal)
        return false
    }

}