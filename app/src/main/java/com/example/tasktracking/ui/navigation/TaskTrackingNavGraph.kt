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

package com.example.tasktracking.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.tasktracking.ui.attempt.AttemptListDestination
import com.example.tasktracking.ui.attempt.AttemptListScreen
import com.example.tasktracking.ui.home.HomeDestination
import com.example.tasktracking.ui.home.HomeScreen
import com.example.tasktracking.ui.task.TaskEditDestination
import com.example.tasktracking.ui.task.TaskEditScreen
import com.example.tasktracking.ui.task.TaskEntryDestination
import com.example.tasktracking.ui.task.TaskEntryScreen
import com.example.tasktracking.ui.task.asString
import java.time.LocalDate


/**
 * Provides Navigation graph for the application.
 */
@Composable
fun TaskTrackingNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = AttemptListDestination.routeWithArgs,
        modifier = modifier
    ) {
        composable(route = HomeDestination.route) {
            HomeScreen(
                navigateToTaskEntry = { navController.navigate(TaskEntryDestination.route)
                    },
                navigateToTaskEdit = { navController.navigate("${TaskEditDestination.route}/${it}")
                    },
                onNavigateUp = { navController.navigateUp() }
            )
        }
        composable(route = TaskEntryDestination.route) {
            TaskEntryScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
//        composable(
//            route = ItemDetailsDestination.routeWithArgs,
//            arguments = listOf(navArgument(ItemDetailsDestination.itemIdArg) {
//                type = NavType.IntType
//            })
//        ) {
//            ItemDetailsScreen(
//                navigateToEditItem = { navController.navigate("${ItemEditDestination.route}/$it") },
//                navigateBack = { navController.navigateUp() }
//            )
//        }
        composable(
            route = TaskEditDestination.routeWithArgs,
            arguments = listOf(navArgument(TaskEditDestination.taskIdArg) {
                type = NavType.IntType
            })
        ) {
            TaskEditScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
        composable(
            route = AttemptListDestination.routeWithArgs,
            arguments = listOf(navArgument("date") { defaultValue = LocalDate.now().asString() })
        ) {
            AttemptListScreen(
                navigateToNextDay = { navController.navigate("${AttemptListDestination.route}?${AttemptListDestination.dateArg}=${it}") },
                navigateToPreviousDay = { navController.navigate("${AttemptListDestination.route}?${AttemptListDestination.dateArg}=${it}") },
                navigateToTaskListScreen = { navController.navigate(HomeDestination.route) }
            )
        }
    }
}
