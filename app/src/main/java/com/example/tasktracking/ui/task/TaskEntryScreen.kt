package com.example.tasktracking.ui.task


import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.Popup
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tasktracking.TaskTrackingTopAppBar
import com.example.tasktracking.R
import com.example.tasktracking.data.DEFAULT_FREQUENCY
import com.example.tasktracking.data.Period
import com.example.tasktracking.data.TaskType
import com.example.tasktracking.ui.AppViewModelProvider
import com.example.tasktracking.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset

object TaskEntryDestination : NavigationDestination {
    override val route = "task_entry"
    override val titleRes = R.string.task_entry_title
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskEntryScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    canNavigateBack: Boolean = true,
    viewModel: TaskEntryViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TaskTrackingTopAppBar(
                title = stringResource(TaskEntryDestination.titleRes),
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp
            )
        }
    ) { innerPadding ->
        TaskEntryBody(
            taskUiState = viewModel.taskUiState,
            onTaskValueChange = viewModel::updateUiState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.saveTask()
                    navigateBack()
            } },
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
        )
    }
}

@Composable
fun TaskEntryBody(
    taskUiState: TaskUiState,
    onTaskValueChange: (TaskDetails) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_large)),
        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium))
        ) {
        TaskInputForm(
            taskDetails = taskUiState.taskDetails,
            onValueChange = onTaskValueChange,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = onSaveClick,
            enabled = taskUiState.isEntryValid,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.save_action))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskInputForm(
    taskDetails: TaskDetails,
    modifier: Modifier = Modifier,
    onValueChange: (TaskDetails) -> Unit = {},
    enabled: Boolean = true
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
    ) {
        OutlinedTextField(
            value = taskDetails.name,
            onValueChange = { onValueChange(taskDetails.copy(name = it)) },
            label = { Text(stringResource(R.string.task_name_req)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            for (i in 0..<TaskType.entries.size) {
                // Sorted because I prefer Repetition to be first and the default
                val type = TaskType.entries[i]
                RadioButton(
                    selected = taskDetails.type == type,
                    onClick = { onValueChange(taskDetails.copy(type = type)) }
                )
                Text(
                    text = type.name,
                    style = MaterialTheme.typography.titleMedium
                )
                // A spacer, ATM looks too wide (Buttons take entire width)
                // if (i < TaskType.entries.size - 1) {
                //     Spacer(Modifier.weight(1f))
                // }
            }
        }
        OutlinedTextField(
            value = taskDetails.goal,
            onValueChange = { if (it.isDigitsOnly()) onValueChange(taskDetails.copy(goal = it)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = { Text(stringResource(R.string.goal_req)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            // TODO Create a more complex state
            // The state will hold the date, reference to dismiss function, and reference to confirm function
            val dialogState = remember { mutableStateOf(DateUiState())}
            fun dismiss() {
                dialogState.value.displayEnd = false
                dialogState.value.displayStart = false
            }

            ClickableText(
                text = AnnotatedString(taskDetails.startDate.formatDate()),
                onClick = {
                    dialogState.value.displayStart = true
                    dialogState.value.displayEnd = false
                          },
                modifier = Modifier.weight(1f),
            )
            OutlinedTextField(
                value = taskDetails.endDate.formatDate(),
                onValueChange = { onValueChange(taskDetails.copy(endDate = it.cleanInputDate())) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = { Text(stringResource(R.string.end_date)) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                ),
                modifier = Modifier.weight(1f),
                enabled = enabled,
                singleLine = true
            )
            if (dialogState.value.displayStart) {
                val datePickerState =
                    rememberDatePickerState(initialSelectedDateMillis = taskDetails.startDate.toLocalDate().atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli())
                dateDialog(datePickerState, { dismiss() })?.let {
                    onValueChange(taskDetails.copy(startDate= Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate().asString()))
                }
            }
            if (dialogState.value.displayEnd) {
                val datePickerState =
                    rememberDatePickerState(initialSelectedDateMillis = taskDetails.endDate.toLocalDate().atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli())
                dateDialog(datePickerState, { dismiss() })?.let {
                    onValueChange(taskDetails.copy(endDate= Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate().asString()))
                }

            }

        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            for (i in 0..<Period.entries.size) {
                // Sorted because I prefer Repetition to be first and the default
                val period = Period.entries[i]
                RadioButton(
                    selected = taskDetails.period == period,
                    onClick = { onValueChange(taskDetails.copy(period = period)) }
                )
                Text(
                    text = period.toString(),
                    style = MaterialTheme.typography.titleMedium
                )
                // A spacer, ATM looks too wide (Buttons take entire width)
                // if (i < TaskType.entries.size - 1) {
                //     Spacer(Modifier.weight(1f))
                // }
            }
        }
        if (taskDetails.period == Period.DAY) {
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                DEFAULT_FREQUENCY.forEach { day ->
                    IconButton(
                        onClick = {
                            val set = ((taskDetails.frequency union mutableSetOf(day)) subtract (taskDetails.frequency intersect mutableSetOf(day))).toMutableSet()
                            onValueChange(taskDetails.copy(frequency = set))
                        },
                        content = {
                            val color = if (taskDetails.frequency.contains(day)) {
                                Color.DarkGray
                            } else {
                                Color.LightGray
                            }
                            Icon(
                                painter = painterResource(R.drawable.filled_circle),
                                contentDescription = null,
                                tint = color,
                                modifier = Modifier.size(48.dp)
                            )
                            Icon(
                                painter = painterResource(R.drawable.circle),
                                contentDescription = null,
                                modifier = Modifier.size(48.dp)
                            )
                            Text(text = day.name[0].toString())
                        },
                    )
                }
            }
        }
        if (enabled) {
            Text(
                text = stringResource(R.string.required_fields),
                modifier = Modifier.padding(start = dimensionResource(id = R.dimen.padding_medium))
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun dateDialog(datePickerState: DatePickerState, dismiss: () -> Unit): Long? {
    var result = datePickerState.selectedDateMillis
    DatePickerDialog(
        onDismissRequest = { dismiss() },
        dismissButton = {
            Button(onClick = { dismiss() }) {
                Text(stringResource(R.string.cancel))
            }
        },
        confirmButton = {
            Button(onClick = { result = datePickerState.selectedDateMillis }) {
                Text(stringResource(R.string.ok))
            }
        }) {
        DatePicker(state = datePickerState)
    }

    return result
}

@Preview(showBackground = true)
@Composable
private fun TaskEntryScreenPreview() {
    val freq = DEFAULT_FREQUENCY.toMutableSet()
    freq.remove(DayOfWeek.FRIDAY)
        TaskEntryBody(taskUiState = TaskUiState(
            TaskDetails(
                name = "Task name", goal = "5", type = TaskType.Repetition, startDate = LocalDate.now().asString(), frequency = freq
            )
        ), onTaskValueChange = {}, onSaveClick = {})
}
