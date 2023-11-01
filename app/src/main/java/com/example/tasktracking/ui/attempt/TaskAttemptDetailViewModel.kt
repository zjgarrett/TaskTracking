package com.example.tasktracking.ui.attempt

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasktracking.data.AppRepository
import com.example.tasktracking.data.Attempt
import com.example.tasktracking.data.Task
import com.example.tasktracking.data.TaskType
import com.example.tasktracking.data.TaskWithAttempted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class TaskAttemptDetailViewModel(savedStateHandle: SavedStateHandle, private val appRepository: AppRepository) : ViewModel() {
    private val taskId: Int = checkNotNull(savedStateHandle[TaskAttemptDetailDestination.taskId])
    init {
        viewModelScope.launch {
            taskAttemptDetailUiState =
                appRepository.getByIdTaskWithAttemptedStream(taskId).first()
                    .toDetailUiState()
        }
    }
    
    var taskAttemptDetailUiState by mutableStateOf(TaskAttemptDetailUiState())
        private set
}

data class TaskAttemptDetailUiState(
    val task: Task = Task("EMPTY",0,TaskType.Duration),
    val attempted: List<Attempt> = emptyList()
)

fun TaskWithAttempted.toDetailUiState(): TaskAttemptDetailUiState {
    return TaskAttemptDetailUiState(this.task, this.attempts)
}
