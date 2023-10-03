package com.example.tasktracking.ui.attempt

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasktracking.data.AppRepository
import com.example.tasktracking.data.Attempt
import com.example.tasktracking.data.Period
import com.example.tasktracking.data.TaskWithAttempted
import com.example.tasktracking.ui.task.asString
import com.example.tasktracking.ui.task.toLocalDate
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDate

/**
 * ViewModel to retrieve all items in the Room database.
 */
class AttemptListViewModel(savedStateHandle: SavedStateHandle, private val appRepository: AppRepository) : ViewModel() {
    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
    private val dateString: String = savedStateHandle[AttemptListDestination.dateArg] ?: LocalDate.now().asString()

    private val date: LocalDate = dateString.toLocalDate()
    init {
        viewModelScope.launch {
            attemptListUiState = AttemptListUiState(appRepository.getAllByDateTaskWithAttemptedStream(date).first())
            for (i in 0..<attemptListUiState.taskWithAttemptedList.size) {
                val taskAttempt = attemptListUiState.taskWithAttemptedList[i]
                var attemptExists = false
                for (attempt in taskAttempt.attempts) {
                    if (attempt.attemptDateStart <= date && attempt.attemptDateEnd >= date) {
                        attemptExists = true
                        break
                    }
                }
                if (!attemptExists) {
                    var start = date
                    var end = date
                    if (taskAttempt.task.period == Period.WEEK) {
                        val tempStart = start.minusDays((start.dayOfWeek.value % 7).toLong())
                        start = if (tempStart >= taskAttempt.task.startDate) tempStart
                        else taskAttempt.task.startDate

                        val tempEnd = end.plusDays((6 - (start.dayOfWeek.value % 7)).toLong())
                        end = if (tempEnd <= taskAttempt.task.endDate) tempEnd
                        else taskAttempt.task.endDate
                    } else if (taskAttempt.task.period == Period.MONTH){
                        val tempStart = start.minusDays((start.dayOfMonth - 1).toLong())
                        start = if (tempStart >= taskAttempt.task.startDate) tempStart
                        else taskAttempt.task.startDate

                        var tempEnd = end.plusMonths(1)
                        tempEnd = tempEnd.minusDays(end.dayOfMonth.toLong())
                        end = if (tempEnd <= taskAttempt.task.endDate) tempEnd
                        else taskAttempt.task.endDate
                    }

                    val attempt = Attempt(
                        taskId = taskAttempt.task.id,
                        attemptDateStart = start,
                        attemptDateEnd = end
                    )

                    appRepository.insertAttempt(attempt)
                    //attemptListUiState.taskWithAttemptedList[i].attempts = listOf(attempt)
                }
            }
        }
    }

    fun getDateUsed(): LocalDate {
        return date
    }

    /**
     * Holds current ui state
     */
    var attemptListUiState by mutableStateOf(AttemptListUiState())
        private set
}

/**
 * Ui State for HomeScreen
 */
data class AttemptListUiState(val taskWithAttemptedList: List<TaskWithAttempted> = listOf())
