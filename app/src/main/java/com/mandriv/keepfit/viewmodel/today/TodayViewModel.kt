package com.mandriv.keepfit.viewmodel.today

import android.content.Context
import androidx.lifecycle.*
import com.mandriv.keepfit.data.goals.Goal
import com.mandriv.keepfit.data.goals.GoalRepository
import com.mandriv.keepfit.data.steps.StepsEntry
import com.mandriv.keepfit.data.steps.StepsRepository
import com.mandriv.keepfit.utilities.NewStepsEntryAlertBuilder
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class TodayViewModel(
    private val stepsRepository: StepsRepository,
    goalRepository: GoalRepository
) : ViewModel() {

    private val todayStepEntry: LiveData<StepsEntry> = stepsRepository.getTodayStepEntry()
    private val activeGoal: LiveData<Goal> = goalRepository.getActiveGoal()

    val stepsCountToday = MutableLiveData<String>("0")

    val activeGoalValue = MutableLiveData<String>("0")
    val activeGoalName = MutableLiveData<String>("")

    val percentageCompleted = MediatorLiveData<String>()

    init {
        percentageCompleted.addSource(todayStepEntry) {
            if (it != null) {
                stepsCountToday.value = it.stepCount.toString()
            }
            if (it != null && activeGoal.value != null) {
                percentageCompleted.value = getPercentageString(it.stepCount, activeGoal.value!!.value)
            }
        }
        percentageCompleted.addSource(activeGoal) {
            if (it != null) {
                activeGoalValue.value = it.value.toString()
                activeGoalName.value = it.name
                if (todayStepEntry.value != null) {
                    percentageCompleted.value = getPercentageString(todayStepEntry.value!!.stepCount, it.value)
                } else {
                    percentageCompleted.value = getPercentageString(0, it.value)
                }
            }
        }
    }

    fun onOpenAddStepsDialog(context: Context) {
        NewStepsEntryAlertBuilder(context, object : NewStepsEntryAlertBuilder.AddStepsListener {
            override fun onAddSteps(steps: Int) {
                viewModelScope.launch {
                    stepsRepository.addTodaySteps(steps)
                }
            }
        }).create().show()
    }

    private fun getPercentageString(a: Int, b: Int): String {
        assert(b != 0)
        val aDouble = a.toDouble()
        val bDouble = b.toDouble()
        val percentageDouble = (aDouble / bDouble) * 100

        return if (percentageDouble > 100) "100" else percentageDouble.roundToInt().toString()
    }

}