package com.example.tasktracking.ui.attempt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasktracking.data.AppRepository
import com.example.tasktracking.data.TaskWithAttempted
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate

/**
 * ViewModel to retrieve all items in the Room database.
 */
class AttemptListViewModel(appRepository: AppRepository) : ViewModel() {
    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    var date: LocalDate = LocalDate.now()
    val attemptListUiState: StateFlow<AttemptListUiState> =
        appRepository.getAllByDateTaskWithAttemptedStream(date).map { AttemptListUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = AttemptListUiState()
            )
}

/**
 * Ui State for HomeScreen
 */
data class AttemptListUiState(val taskWithAttemptedList: List<TaskWithAttempted> = listOf())
