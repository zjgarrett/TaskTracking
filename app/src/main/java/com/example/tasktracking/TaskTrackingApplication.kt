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

package com.example.tasktracking

import android.app.Application
import com.example.tasktracking.data.AppContainer
import com.example.tasktracking.data.AppDataContainer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class TaskTrackingApplication : Application() {
    private val scope = CoroutineScope(Job() + Dispatchers.Main)

    /**
     * AppContainer instance used by the rest of classes to obtain dependencies
     */
    var container: AppContainer = AppDataContainer(this)

    init {
        scope.launch {
            container.appRepository.addSkippedAttempts()
        }
    }
}
