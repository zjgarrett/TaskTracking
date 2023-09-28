package com.example.tasktracking.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "attempted")
data class Attempt(
    var taskId: Int = 0,
    var attemptDateStart: String,
    var attemptDateEnd: String,
    var completed: Int = 0,
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
)