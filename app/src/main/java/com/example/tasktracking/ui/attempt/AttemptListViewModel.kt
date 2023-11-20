package com.example.tasktracking.ui.attempt

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasktracking.data.AppRepository
import com.example.tasktracking.data.Attempt
import com.example.tasktracking.data.Period
import com.example.tasktracking.data.Task
import com.example.tasktracking.data.TaskWithAttempted
import com.example.tasktracking.data.attemptEndDate
import com.example.tasktracking.ui.task.asString
import com.example.tasktracking.ui.task.toLocalDate
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate

/**
 * ViewModel to retrieve all items in the Room database.
 */
class AttemptListViewModel(savedStateHandle: SavedStateHandle, private val appRepository: AppRepository) : ViewModel() {
    private val dateString: String = savedStateHandle[AttemptListDestination.dateArg] ?: LocalDate.now().asString()

    private val date: LocalDate = dateString.toLocalDate()

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    val attemptUiState: StateFlow<AttemptUiState> =
        appRepository.getAllByDateTaskWithAttemptedStream(date).map {it.toAttemptUiState(date) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = AttemptUiState()
            )

    fun getDateUsed(): LocalDate {
        return date
    }

    suspend fun updateAttempt(attempt: Attempt) {
        appRepository.updateAttempt(attempt)
    }
}

data class AttemptUiState(
    val daily: Map<Int, TaskWithAttemptedDetails> = mapOf(),
    val weekly: Map<Int, TaskWithAttemptedDetails> = mapOf(),
    val monthly: Map<Int, TaskWithAttemptedDetails> = mapOf(),
    val completed: Map<Int, TaskWithAttemptedDetails> = mapOf()
)

data class TaskWithAttemptedDetails(
    val task: Task,
    val attempt: Attempt
)

fun List<TaskWithAttempted>.toAttemptUiState(date: LocalDate): AttemptUiState {
    val daily = mutableMapOf<Int, TaskWithAttemptedDetails>()
    val weekly = mutableMapOf<Int, TaskWithAttemptedDetails>()
    val monthly = mutableMapOf<Int, TaskWithAttemptedDetails>()
    val completed = mutableMapOf<Int, TaskWithAttemptedDetails>()

    for (i in indices) {
        val taskAttempt = this[i]
        var attemptExists = false
        var todaysAttempt = Attempt(taskAttempt.task.id, date, date)
        Log.d("Num Attempts Recieved", taskAttempt.attempts.toString())

        if (date.dayOfWeek !in taskAttempt.task.frequency) continue

        for (attempt in taskAttempt.attempts) {
            Log.d("Attempt: ", attempt.toString())
            if (attempt.attemptDateStart <= date && attempt.attemptDateEnd >= date) {
                attemptExists = true
                todaysAttempt = attempt
            }
        }
        if (!attemptExists) {
            var start = date
            if (taskAttempt.task.period == Period.WEEK) {
                val tempStart = start.minusDays((start.dayOfWeek.value % 7).toLong())
                start = if (tempStart >= taskAttempt.task.startDate) tempStart
                else taskAttempt.task.startDate
            } else if (taskAttempt.task.period == Period.MONTH) {
                val tempStart = start.minusDays((start.dayOfMonth - 1).toLong())
                start = if (tempStart >= taskAttempt.task.startDate) tempStart
                else taskAttempt.task.startDate
            }

            val end = taskAttempt.task.attemptEndDate(start)

            todaysAttempt = Attempt(
                taskId = taskAttempt.task.id,
                attemptDateStart = start,
                attemptDateEnd = end!!
            )
        }

        val tempTaskWithAttemptedDetails = TaskWithAttemptedDetails(taskAttempt.task, todaysAttempt)
        if (taskAttempt.task.goal <= todaysAttempt.completed) completed[tempTaskWithAttemptedDetails.task.id] =
            tempTaskWithAttemptedDetails
        else when (taskAttempt.task.period) {
            Period.DAY -> daily[tempTaskWithAttemptedDetails.task.id] =
                tempTaskWithAttemptedDetails

            Period.WEEK -> weekly[tempTaskWithAttemptedDetails.task.id] =
                tempTaskWithAttemptedDetails

            Period.MONTH -> monthly[tempTaskWithAttemptedDetails.task.id] =
                tempTaskWithAttemptedDetails
        }
    }
    return AttemptUiState(daily, weekly, monthly, completed)
}
