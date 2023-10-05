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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tasktracking.R
import com.example.tasktracking.TaskTrackingTopAppBar
import com.example.tasktracking.data.Attempt
import com.example.tasktracking.data.Period
import com.example.tasktracking.data.TaskType
import com.example.tasktracking.data.toTitleCase
import com.example.tasktracking.ui.AppViewModelProvider
import com.example.tasktracking.ui.task.readableString
import com.example.tasktracking.ui.navigation.NavigationDestination
import com.example.tasktracking.ui.task.asString
import kotlinx.coroutines.launch
import java.time.LocalDate
import kotlin.reflect.KSuspendFunction1

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
    val attemptUiState = viewModel.attemptUiState
    val updateAttempt = viewModel::updateAttempt

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
            attemptUiState = attemptUiState,
            updateAttempt = updateAttempt,
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
    attemptUiState: AttemptUiState,
    updateAttempt: KSuspendFunction1<Attempt, Unit>,
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
        if (attemptUiState.daily.isEmpty() && attemptUiState.weekly.isEmpty() && attemptUiState.monthly.isEmpty() && attemptUiState.completed.isEmpty()) {
            Text(
                text = stringResource(R.string.no_tasks_description) + " Today",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )
        } else {
            AttemptedTaskList(
                attemptUiState = attemptUiState,
                updateAttempt = updateAttempt,
                modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_small))
            )
        }
    }
}

@Composable
private fun AttemptedTaskList(
    attemptUiState: AttemptUiState,
    updateAttempt: KSuspendFunction1<Attempt, Unit>,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(items = attemptUiState.daily.toList(), key = { it }) { attempt ->
            IndividualAttempt(
                taskWithAttempted = attempt.second,
                updateAttempt = updateAttempt,
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.padding_small)))
        }
        items(items = attemptUiState.weekly.toList(), key = { it }) { attempt ->
            IndividualAttempt(
                taskWithAttempted = attempt.second,
                updateAttempt = updateAttempt,
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.padding_small)))
        }
        items(items = attemptUiState.monthly.toList(), key = { it }) { attempt ->
            IndividualAttempt(
                taskWithAttempted = attempt.second,
                updateAttempt = updateAttempt,
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.padding_small)))
        }
        if (attemptUiState.completed.isNotEmpty()) {
            item {
                Text(text = "Completed tasks")
            }
            items(items = attemptUiState.completed.toList(), key = { it }) { attempt ->
                IndividualAttempt(
                    taskWithAttempted = attempt.second,
                    updateAttempt = updateAttempt,
                    modifier = Modifier
                        .padding(dimensionResource(id = R.dimen.padding_small))
                )
            }
        }
    }
}

@Composable
private fun IndividualAttempt(
    taskWithAttempted: TaskWithAttemptedDetails,
    updateAttempt: KSuspendFunction1<Attempt, Unit>,
    modifier: Modifier = Modifier
) {
    val attempt: Attempt = taskWithAttempted.attempt
    val coroutineScope = rememberCoroutineScope()

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
                    text = stringResource(R.string.starts, attempt.attemptDateStart.readableString()),
                    style = MaterialTheme.typography.titleMedium
                )
                if (taskWithAttempted.task.endDate != LocalDate.MAX) {
                    Spacer(Modifier.weight(1f))
                    Text(
                        text = stringResource(R.string.ends, attempt.attemptDateEnd.readableString()),
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
            Button(
                onClick = { coroutineScope.launch{
                    updateAttempt(attempt.copy(completed = attempt.completed + 1))
                } },
                enabled = true,
                content = {
                    Icon(Icons.Filled.Add, "Add completion")
                }
            )
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun AttemptListBodyPreview() {
//    AttemptListBody(
//        LocalDate.now(),
//        {},
//        {},
//        AttemptUiState(
//        TaskWithAttemptedDetails(Task(
//            "Walk",
//            1,
//            TaskType.Repetition,
//            Period.DAY,
//            frequency = mutableSetOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY),
//            endDate = LocalDate.of(2024, 3, 3),
//            id= 0
//        ), Attempt(0, LocalDate.now(), LocalDate.now(), 1)),
//        TaskWithAttemptedDetails(Task(
//            "Read Book",
//            90,
//            TaskType.Duration,
//            period = Period.WEEK,
//            id= 1
//        ), Attempt(0, LocalDate.now(), LocalDate.now(), 1))))
//    ))
//}
//
//@Preview(showBackground = true)
//@Composable
//fun AttemptListBodyEmptyListPreview() {
//        AttemptListBody(
//            LocalDate.now(),
//            {},
//            {},
//            listOf(),
//        )
//}
//
//@Preview(showBackground = true)
//@Composable
//fun IndividualAttemptPreview() {
//        IndividualAttempt(
//            TaskWithAttempted(Task("Walk", 1, TaskType.Repetition, Period.DAY, endDate = LocalDate.of(2023, 11, 3)), listOf())
//        )
//}
