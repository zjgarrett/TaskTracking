package com.example.tasktracking.ui.attempt

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tasktracking.R
import com.example.tasktracking.TaskTrackingTopAppBar
import com.example.tasktracking.data.Attempt
import com.example.tasktracking.data.Period
import com.example.tasktracking.data.Task
import com.example.tasktracking.data.TaskType
import com.example.tasktracking.data.TaskWithAttempted
import com.example.tasktracking.data.toTitleCase
import com.example.tasktracking.ui.AppViewModelProvider
import com.example.tasktracking.ui.task.readableString
import com.example.tasktracking.ui.navigation.NavigationDestination
import com.example.tasktracking.ui.task.asString
import java.time.DayOfWeek
import java.time.LocalDate

object AttemptListDestination : NavigationDestination {
    override val route = "attempted_list"
    override val titleRes = R.string.app_name
    const val dateArg = "date"
    val routeWithArgs = "$route?$dateArg={$dateArg}"
}

/**
 * Entry route for Attempt List screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AttemptListScreen(
    navigateToNextDay: (String) -> Unit,
    navigateToPreviousDay: (String) -> Unit,
    navigateToTaskListScreen: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AttemptListViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val attemptListUiState = viewModel.attemptListUiState

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TaskTrackingTopAppBar(
                title = stringResource(AttemptListDestination.titleRes),
                canNavigateBack = false,
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToTaskListScreen,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_large))
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.task_entry_title)
                )
            }
        },
    ) { innerPadding ->
        AttemptListBody(
            dateUsed = viewModel.getDateUsed(),
            navigateToNextDay = navigateToNextDay,
            navigateToPreviousDay = navigateToPreviousDay,
            attemptedTaskList = attemptListUiState.taskWithAttemptedList,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        )
    }
}

@Composable
private fun AttemptListBody(
    dateUsed: LocalDate,
    navigateToNextDay: (String) -> Unit,
    navigateToPreviousDay: (String) -> Unit,
    attemptedTaskList: List<TaskWithAttempted>,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Row(

        ) {
            Button(
                onClick = {
                    navigateToPreviousDay(dateUsed.minusDays(1).asString()) },
                content = {
                    Icon(Icons.Filled.ArrowBack, "previous day")
                }
            )
            Text(text = dateUsed.readableString())
            Button(
                onClick = { navigateToNextDay(dateUsed.plusDays(1).asString()) },
                content = {
                    Icon(Icons.Filled.ArrowForward, "Next day")
                }
            )
        }
        if (attemptedTaskList.isEmpty()) {
            Text(
                text = stringResource(R.string.no_tasks_description) + " Today",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )
        } else {
            AttemptedTaskList(
                attemptedTaskList = attemptedTaskList,
                modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_small))
            )
        }
    }
}

@Composable
private fun AttemptedTaskList(
    attemptedTaskList: List<TaskWithAttempted>, modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(items = attemptedTaskList, key = { it.task.id }) { attempt ->
            IndividualAttempt(taskWithAttempted = attempt,
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.padding_small)))
        }
    }
}

@Composable
private fun IndividualAttempt(
    taskWithAttempted: TaskWithAttempted, modifier: Modifier = Modifier
) {
    val attempt: Attempt = if (taskWithAttempted.attempts.isNotEmpty()) {
        taskWithAttempted.attempts[0]
    } else {
        Attempt(taskWithAttempted.task.id, LocalDate.now(), LocalDate.now(), completed = 100)
    }

    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_large)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_small))
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = taskWithAttempted.task.name + ": Completed " + attempt.completed.toString() + " times",
                    style = MaterialTheme.typography.titleLarge,
                )
                Spacer(Modifier.weight(1f))
                Text(
                    text = taskWithAttempted.task.period.toAdjective(),
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.starts, taskWithAttempted.task.startDate.readableString()),
                    style = MaterialTheme.typography.titleMedium
                )
                if (taskWithAttempted.task.endDate != LocalDate.MAX) {
                    Spacer(Modifier.weight(1f))
                    Text(
                        text = stringResource(R.string.ends, taskWithAttempted.task.endDate.readableString()),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
            val (amount, unit) = when (taskWithAttempted.task.type) {
                TaskType.Repetition -> {
                    val temp = if (taskWithAttempted.task.goal == 1) "time" else "times"
                    Pair(taskWithAttempted.task.goal.toString(), temp)
                }
                TaskType.Duration -> if (taskWithAttempted.task.goal > 60) Pair((taskWithAttempted.task.goal.toFloat() / 60.toFloat()).toString(), "hours") else Pair(taskWithAttempted.task.goal.toString(), "minutes")
            }
            val (howOften, specificDays) = when (taskWithAttempted.task.period) {
                Period.DAY -> if (taskWithAttempted.task.frequency.size == 7) Pair(taskWithAttempted.task.period.toAdjective(), false) else Pair("on days", true)
                else -> Pair(taskWithAttempted.task.period.toAdjective(), false)
            }
            Text(
                text = stringResource(R.string.complete_per, amount, unit, howOften.lowercase()),
                style = MaterialTheme.typography.titleMedium
            )
            if (specificDays) {
                var days = ""
                for (day in taskWithAttempted.task.frequency) {
                    days += day.toTitleCase() + " "
                }
                Text(
                    text = days,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AttemptListBodyPreview() {
    AttemptListBody(
        LocalDate.now(),
        {},
        {},
        listOf(
        TaskWithAttempted(Task(
            "Walk",
            1,
            TaskType.Repetition,
            Period.DAY,
            frequency = mutableSetOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY),
            endDate = LocalDate.of(2024, 3, 3),
            id= 0
        ), listOf(Attempt(0, LocalDate.now(), LocalDate.now(), 1))),
        TaskWithAttempted(Task(
            "Read Book",
            90,
            TaskType.Duration,
            period = Period.WEEK,
            id= 1
        ), listOf())
    ))
}

@Preview(showBackground = true)
@Composable
fun AttemptListBodyEmptyListPreview() {
        AttemptListBody(
            LocalDate.now(),
            {},
            {},
            listOf(),
        )
}

@Preview(showBackground = true)
@Composable
fun IndividualAttemptPreview() {
        IndividualAttempt(
            TaskWithAttempted(Task("Walk", 1, TaskType.Repetition, Period.DAY, endDate = LocalDate.of(2023, 11, 3)), listOf())
        )
}
