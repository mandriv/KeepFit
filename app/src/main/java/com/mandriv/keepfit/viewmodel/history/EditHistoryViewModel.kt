package com.mandriv.keepfit.viewmodel.history

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mandriv.keepfit.data.goals.GoalRepository
import com.mandriv.keepfit.data.steps.StepsEntry
import com.mandriv.keepfit.data.steps.StepsRepository
import kotlinx.coroutines.launch

class EditHistoryViewModel(
    private val stepsRepository: StepsRepository,
    goalRepository: GoalRepository,
    stepsEntryId: Int,
    val goalId: Int
) : ViewModel() {

    val stepsEntry = stepsRepository.getEntryById(stepsEntryId)
    val goal = goalRepository.getById(goalId)
    val goals = goalRepository.getNotDeleted()

    val steps: MutableLiveData<String> = MutableLiveData("")
    val goalName: MutableLiveData<String> = MutableLiveData("")
    val goalNames = Transformations.map(goals) { goals -> goals.map { goal -> "${goal.name} - ${goal.value} steps" } }

    val close = MutableLiveData<Boolean>(false)


    fun updateSteps(stepCount: Int) {
        steps.value = stepCount.toString()
    }

    fun onSave(): Boolean {
        if (goalName.value.isNullOrBlank() || steps.value.isNullOrBlank()) {
            return false
        }
        val goalParts = goalName.value!!.substring(0, goalName.value!!.length - 6).split(" - ")
        Log.i("AAA", "Given goal - ${goalParts[0]}, ${goalParts[1]}")
        var givenGoal = goals.value!!.find { g -> g.name == goalParts[0] && g.value == goalParts[1].toInt() }
        if (givenGoal == null) {
            givenGoal = goal.value
        }
        Log.i("AAA", "Found goal - ${givenGoal!!.name}, ${givenGoal.value}")
        val updateStepEntry =
            StepsEntry(stepsEntry.value!!.id, steps.value!!.toInt(), stepsEntry.value!!.addedAt, givenGoal.id)
        viewModelScope.launch {
            stepsRepository.update(updateStepEntry)
            close.value = true
        }
        return false
    }
}