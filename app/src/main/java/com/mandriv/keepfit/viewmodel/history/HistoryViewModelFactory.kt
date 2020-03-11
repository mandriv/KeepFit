package com.mandriv.keepfit.viewmodel.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mandriv.keepfit.data.steps.StepsRepository

class HistoryViewModelFactory(
    private val repository: StepsRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HistoryViewModel(repository) as T
    }
}