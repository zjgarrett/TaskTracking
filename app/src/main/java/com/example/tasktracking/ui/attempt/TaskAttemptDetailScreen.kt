package com.example.tasktracking.ui.attempt

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tasktracking.R
import com.example.tasktracking.TaskTrackingTopAppBar
import com.example.tasktracking.data.Attempt
import com.example.tasktracking.ui.AppViewModelProvider
import com.example.tasktracking.ui.home.IndividualTask
import com.example.tasktracking.ui.navigation.NavigationDestination
import com.example.tasktracking.ui.task.readableString


object TaskAttemptDetailDestination : NavigationDestination {
    override val route = "task_attempt_detail"
    override val titleRes = R.string.app_name
    const val taskId = "id"
    val routeWithArgs = "$route?$taskId={$taskId}"
}

/**
 * Entry route for Attempt List screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TaskAttemptDetailScreen(
    navigateToTaskEdit: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TaskAttemptDetailViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val taskAttemptUiState = viewModel.taskAttemptDetailUiState

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
                onClick = { navigateToTaskEdit(taskAttemptUiState.task.id) },
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
        TaskAttemptDetailBody(
            taskAttemptUiState = taskAttemptUiState,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        )
    }
}

@Composable
fun TaskAttemptDetailBody(
    taskAttemptUiState: TaskAttemptDetailUiState,
    modifier: Modifier = Modifier
) {
    Column() {
        IndividualTask(
            taskAttemptUiState.task,
            modifier
        )
        LazyColumn(modifier = modifier) {
            items(items = taskAttemptUiState.attempted, key = {it}) {
                attempt ->
                IndividualAttemptDetail(attempt = attempt, modifier = modifier)
            }
        }
    }
}

@Composable
fun IndividualAttemptDetail(
    attempt: Attempt,
    modifier: Modifier
) {
    Row(modifier = modifier) {
        Text(text = attempt.attemptDateStart.readableString())
        Text(text = attempt.attemptDateEnd.readableString())
        Text(text = attempt.completed.toString())
    }
}