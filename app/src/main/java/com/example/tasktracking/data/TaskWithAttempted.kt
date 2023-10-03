package com.example.tasktracking.data

import androidx.room.Embedded
import androidx.room.Relation

data class TaskWithAttempted(
    @Embedded val task: Task,
    @Relation(
        parentColumn = "id",
        entityColumn = "taskId"
    )
    var attempts: List<Attempt>
)
