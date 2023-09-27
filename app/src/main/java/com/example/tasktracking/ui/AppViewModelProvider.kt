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

package com.example.tasktracking.ui

import android.app.Application
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.tasktracking.ui.home.HomeViewModel
import com.example.tasktracking.TaskTrackingApplication
import com.example.tasktracking.ui.task.TaskEntryViewModel
import com.example.tasktracking.ui.task.TaskEditViewModel

/**
 * Provides Factory to create instance of ViewModel for the entire Inventory app
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {
        // Initializer for ItemEditViewModel
        initializer {
            TaskEditViewModel(
                this.createSavedStateHandle(),
                taskTrackingApplication().container.tasksRepository
            )
        }
        // Initializer for TaskEntryViewModel
        initializer {
            TaskEntryViewModel(TaskTrackingApplication().container.tasksRepository)
        }
//
//        // Initializer for ItemDetailsViewModel
//        initializer {
//            ItemDetailsViewModel(
//                this.createSavedStateHandle(),
//                inventoryApplication().container.itemsRepository
//            )
//        }

        // Initializer for HomeViewModel
        initializer {
            HomeViewModel(taskTrackingApplication().container.tasksRepository)
        }
    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [TaskTrackingApplication].
 */
fun CreationExtras.taskTrackingApplication(): TaskTrackingApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as TaskTrackingApplication)
