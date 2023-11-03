package com.example.tasktracking.ui.attempt

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
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
                .padding(top= innerPadding.calculateTopPadding(), bottom = 0.dp)
                .fillMaxSize()
        )
    }
}

@Composable
fun TaskAttemptDetailBody(
    taskAttemptUiState: TaskAttemptDetailUiState,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        IndividualTask(
            task = taskAttemptUiState.task,
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.padding_small))
                .wrapContentHeight()
        )
        Divider(thickness = Dp.Hairline)
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(items = taskAttemptUiState.attempted, key = { it.attemptId }) {
                attempt ->
                IndividualAttemptDetail(
                    attempt = attempt,
                    modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small)))
            }
        }
    }
}

@Composable
fun IndividualAttemptDetail(
    attempt: Attempt,
    modifier: Modifier
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
                    text = "Completed " + attempt.completed.toString() + " times",
                    style = MaterialTheme.typography.titleLarge,
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.starts, attempt.attemptDateStart.readableString()),
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(Modifier.weight(1f))
                Text(
                    text = stringResource(R.string.ends, attempt.attemptDateEnd.readableString()),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}