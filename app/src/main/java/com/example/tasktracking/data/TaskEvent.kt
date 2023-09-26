package com.example.tasktracking.data

import java.time.DayOfWeek
import java.time.LocalDate

sealed interface TaskEvent {
    data object SaveTask: TaskEvent
    data class SetName(val name: String): TaskEvent
    data class SetTimePeriod(val period: Period): TaskEvent
    data class SetStartDate(val startDate: LocalDate): TaskEvent
    data class SetEndDate(val endDate: LocalDate): TaskEvent
    data class AddFrequency(val frequency: DayOfWeek): TaskEvent
    data class RemoveFrequency(val frequency: DayOfWeek): TaskEvent
    data class SetType(val type: TaskType): TaskEvent
    data class SetGoal(val goal: Int): TaskEvent
    data class DeleteTask(val task: Task): TaskEvent
}
