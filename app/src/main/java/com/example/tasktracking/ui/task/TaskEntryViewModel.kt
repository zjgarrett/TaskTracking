package com.example.tasktracking.ui.task

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.tasktracking.data.AppRepository
import com.example.tasktracking.data.DEFAULT_FREQUENCY
import com.example.tasktracking.data.Period
import com.example.tasktracking.data.Task
import com.example.tasktracking.data.TaskType
import java.lang.Integer.parseInt
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * ViewModel to validate and insert items in the Room database.
 */
class TaskEntryViewModel(private val appRepository: AppRepository) : ViewModel() {

    /**
     * Holds current item ui state
     */
    var taskUiState by mutableStateOf(TaskUiState())
        private set

    /**
     * Updates the [taskUiState] with the value provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateUiState(taskDetails: TaskDetails) {
        taskUiState =
            TaskUiState(taskDetails = taskDetails, isEntryValid = validateInput(taskDetails))
    }

    // TODO: FIX
    private fun validateInput(uiState: TaskDetails = taskUiState.taskDetails): Boolean {
        return with(uiState) {
            name.isNotBlank() && goal.isNotBlank() && frequency.isNotEmpty() && startDate < endDate
        }
    }

    suspend fun saveTask() {
        if (validateInput()) {
            appRepository.insertTask(taskUiState.taskDetails.toTask())
        }
    }
}

/**
 * Represents Ui State for an [Task].
 */
data class TaskUiState(
    val taskDetails: TaskDetails = TaskDetails(),
    val isEntryValid: Boolean = false
)

data class TaskDetails(
    val id: Int = 0,
    val name: String = "",
    val goal: String = "",
    val type: TaskType = TaskType.Repetition,
    val startDate: String = LocalDate.now().asString(),
    val endDate: String = "",
    val period: Period = Period.DAY,
    val frequency: MutableSet<DayOfWeek> = DEFAULT_FREQUENCY.toMutableSet()
)

/**
 * Extension function to convert [TaskDetails] to [Task]
 */
fun TaskDetails.toTask(): Task = Task(
    id = id,
    name = name,
    goal = parseInt(goal),
    type = type,
    startDate = startDate.toLocalDate(),
    endDate = if (endDate != "") endDate.toLocalDate() else LocalDate.MAX,
    period = period,
    frequency = frequency
)

fun LocalDate.readableString(): String {
    return this.format(DateTimeFormatter.ofPattern("d MMM uuuu"))
}

/**
 * Extension function to convert [Task] to [TaskUiState]
 */
fun Task.toTaskUiState(isEntryValid: Boolean = false): TaskUiState = TaskUiState(
    taskDetails = this.toTaskDetails(),
    isEntryValid = isEntryValid
)

fun String.cleanInputDate(): String {
    val cleaned = this.replace("/","")
    if (cleaned.isEmpty()) return ""
    val lastChar = cleaned[cleaned.length - 1]
    println(lastChar)
    if (!lastChar.isDigit() || cleaned.length > 8) return cleaned.substring(0,cleaned.length - 1)
    return cleaned
}

fun String.formatDate(): String {
    if (this.isEmpty()) return ""

    var formatted = this
    if (this.length >= 3) {
        formatted = this.substring(0,2) + '/' + this.substring(2)
    }
    if (this.length >= 5) {
        formatted = formatted.substring(0,5) + '/' + formatted.substring(5)
    }

    return formatted
}

fun String.toLocalDate(): LocalDate = LocalDate.parse(this,
    DateTimeFormatter.ofPattern("ddMMuuuu")
)

fun LocalDate.asString(): String {
    return this.format(DateTimeFormatter.ofPattern("ddMMuuuu"))
}

/**
 * Extension function to convert [Task] to [TaskDetails]
 */
fun Task.toTaskDetails(): TaskDetails = TaskDetails(
    id = id,
    name = name,
    goal = goal.toString(),
    type = type,
    startDate = startDate.asString(),
    endDate = if (endDate == LocalDate.MAX) "" else endDate.asString(),
    period = period,
    frequency = frequency
)
