package com.example.tasktracking.ui.item

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.tasktracking.data.DEFAULT_FREQUENCY
import com.example.tasktracking.data.Period
import com.example.tasktracking.data.Task
import com.example.tasktracking.data.TaskEvent
import com.example.tasktracking.data.TaskType
import com.example.tasktracking.data.TasksRepository
import java.lang.Integer.parseInt
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * ViewModel to validate and insert items in the Room database.
 */
class TaskEntryViewModel(private val tasksRepository: TasksRepository) : ViewModel() {

    /**
     * Holds current item ui state
     */
    var taskUiState by mutableStateOf(TaskUiState())
        private set

    /**
     * Updates the [itemUiState] with the value provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateUiState(taskDetails: TaskDetails) {
        taskUiState =
            TaskUiState(taskDetails = taskDetails, isEntryValid = validateInput(taskDetails))
    }

    // TODO: FIX
    private fun validateInput(uiState: TaskDetails = taskUiState.taskDetails): Boolean {
        return with(uiState) {
            name.isNotBlank()
        }
    }

    suspend fun saveTask() {
        if (validateInput()) {
            tasksRepository.insertTask(taskUiState.taskDetails.toTask())
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
 * TODO: FIX COMMENT
 * Extension function to convert [TaskDetails] to [Task]. If the value of [TaskDetails.price] is
 * not a valid [Double], then the price will be set to 0.0. Similarly if the value of
 * [TaskDetails.quantity] is not a valid [Int], then the quantity will be set to 0
 */
fun TaskDetails.toTask(): Task = Task(
    id = id,
    name = name,
    goal = parseInt(goal),
    type = type,
    startDate = startDate.toLocalDate(),
    endDate = if (endDate != "") endDate.toLocalDate() else LocalDate.MAX,
    period = period
)

fun LocalDate.readableString(): String {
    return this.format(DateTimeFormatter.ofPattern("d MMM uuuu"))
}

fun readableToLocalDate(string: String): LocalDate = LocalDate.parse(string,
    DateTimeFormatter.ofPattern("d MMM uuuu")
)

/**
 * Extension function to convert [Task] to [TaskUiState]
 */
fun Task.toTaskUiState(isEntryValid: Boolean = false): TaskUiState = TaskUiState(
    taskDetails = this.toTaskDetails(),
    isEntryValid = isEntryValid
)

fun String.formatDate(): String {
    if (this.isEmpty()) return ""
    val lastChar = this[this.length - 1]
    if (!lastChar.isDigit() && lastChar != '/') return this.substring(0,this.length - 1)

    if (this.length == 2) {
        return this.plus('/')
    } else if (this.length == 5) {
        return this.plus('/')
    } else if (this.length == 11) return this.substring(0,10)

    return this
}

fun String.toLocalDate(): LocalDate = LocalDate.parse(this,
    DateTimeFormatter.ofPattern("dd/MM/uuuu")
)

fun LocalDate.asString(): String {
    return this.format(DateTimeFormatter.ofPattern("dd/MM/uuuu"))
}

/**
 * Extension function to convert [Item] to [ItemDetails]
 */
fun Task.toTaskDetails(): TaskDetails = TaskDetails(
    id = id,
    name = name,
    goal = goal.toString(),
    type = type,
    startDate = startDate.asString(),
    endDate = if (endDate == LocalDate.MAX) "" else endDate.asString(),
    period = period
)