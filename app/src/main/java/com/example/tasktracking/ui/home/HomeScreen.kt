package com.example.tasktracking.ui.home

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.tasktracking.data.Period
import com.example.tasktracking.data.Task
import com.example.tasktracking.data.TaskType
import com.example.tasktracking.data.toTitleCase
import com.example.tasktracking.ui.AppViewModelProvider
import com.example.tasktracking.ui.task.readableString
import com.example.tasktracking.ui.navigation.NavigationDestination
import java.time.DayOfWeek
import java.time.LocalDate

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.app_name
}

/**
 * Entry route for Home screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    navigateToTaskEntry: () -> Unit,
    navigateToTaskAttempt: (Int) -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    canNavigateBack: Boolean = true,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val homeUiState by viewModel.homeUiState.collectAsState()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TaskTrackingTopAppBar(
                title = stringResource(HomeDestination.titleRes),
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp,
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToTaskEntry,
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
        HomeBody(
            taskList = homeUiState.taskList,
            onTaskClick = navigateToTaskAttempt,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        )
    }
}

@Composable
private fun HomeBody(
    taskList: List<Task>, onTaskClick: (Int) -> Unit, modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        if (taskList.isEmpty()) {
            Text(
                text = stringResource(R.string.no_tasks_description),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )
        } else {
            TaskList(
                taskList = taskList,
                onTaskClick = { onTaskClick(it.id) },
                modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_small))
            )
        }
    }
}

@Composable
private fun TaskList(
    taskList: List<Task>, onTaskClick: (Task) -> Unit, modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(items = taskList, key = { it.id }) { task ->
            IndividualTask(task = task,
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.padding_small))
                    .clickable { onTaskClick(task) })
        }
    }
}

@Composable
fun IndividualTask(
    task: Task, modifier: Modifier = Modifier
) {
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
                    text = task.name,
                    style = MaterialTheme.typography.titleLarge,
                )
                Spacer(Modifier.weight(1f))
                Text(
                    text = task.period.toAdjective(),
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.starts, task.startDate.readableString()),
                    style = MaterialTheme.typography.titleMedium
                )
                if (task.endDate != LocalDate.MAX) {
                    Spacer(Modifier.weight(1f))
                    Text(
                        text = stringResource(R.string.ends, task.endDate.readableString()),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
            val (amount, unit) = when (task.type) {
                TaskType.Repetition -> {
                    val temp = if (task.goal == 1) "time" else "times"
                    Pair(task.goal.toString(), temp)
                }
                TaskType.Duration -> if (task.goal > 60) Pair((task.goal.toFloat() / 60.toFloat()).toString(), "hours") else Pair(task.goal.toString(), "minutes")
            }
            val (howOften, specificDays) = when (task.period) {
                Period.DAY -> if (task.frequency.size == 7) Pair(task.period.toAdjective(), false) else Pair("on days", true)
                else -> Pair(task.period.toAdjective(), false)
            }
            Text(
                text = stringResource(R.string.complete_per, amount, unit, howOften.lowercase()),
                style = MaterialTheme.typography.titleMedium
            )
            if (specificDays) {
                var days = ""
                for (day in task.frequency) {
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
fun HomeBodyPreview() {
    HomeBody(listOf(
        Task(
            "Walk",
            1,
            TaskType.Repetition,
            Period.DAY,
            frequency = mutableSetOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY),
            endDate = LocalDate.of(2024, 3, 3),
            id= 0
        ),
        Task(
            "Read Book",
            90,
            TaskType.Duration,
            period = Period.WEEK,
            id= 1
        )
    ), onTaskClick = {})
}

@Preview(showBackground = true)
@Composable
fun HomeBodyEmptyListPreview() {
        HomeBody(listOf(), onTaskClick = {})
}

@Preview(showBackground = true)
@Composable
fun IndividualTaskPreview() {
        IndividualTask(
            Task("Walk", 1, TaskType.Repetition, Period.DAY, endDate = LocalDate.of(2023, 11, 3))
        )
}
