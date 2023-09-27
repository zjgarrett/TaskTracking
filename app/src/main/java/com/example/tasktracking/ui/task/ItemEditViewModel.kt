/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.tasktracking.ui.task

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasktracking.data.TasksRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * ViewModel to retrieve and update an task from the [TasksRepository]'s data source.
 */
class TaskEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val tasksRepository: TasksRepository
) : ViewModel() {
    private val taskId: Int = checkNotNull(savedStateHandle[TaskEditDestination.taskIdArg])
    init {
        viewModelScope.launch {
            taskUiState = tasksRepository.getTaskStream(taskId)
                .filterNotNull()
                .first()
                .toTaskUiState(true)
        }
    }


    /**
     * Holds current task ui state
     */
    var taskUiState by mutableStateOf(TaskUiState())
        private set

    private fun validateInput(uiState: TaskDetails = taskUiState.taskDetails): Boolean {
        return with(uiState) {
            name.isNotBlank()
        }
    }

    fun updateUiState(taskDetails: TaskDetails) {
        taskUiState =
            TaskUiState(taskDetails = taskDetails, isEntryValid = validateInput(taskDetails))
    }

    suspend fun updateTask() {
        if (validateInput(taskUiState.taskDetails)) {
            tasksRepository.updateTask(taskUiState.taskDetails.toTask())
        }
    }
}
