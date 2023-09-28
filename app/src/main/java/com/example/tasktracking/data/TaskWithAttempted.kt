package com.example.tasktracking.data

import androidx.room.Embedded
import androidx.room.Relation
import kotlinx.coroutines.flow.Flow

data class TaskWithAttempted(
    @Embedded val task: Task,
    @Relation(
        parentColumn = "id",
        entityColumn = "taskId"
    )
    val attempts: Flow<List<Attempt>>
)
