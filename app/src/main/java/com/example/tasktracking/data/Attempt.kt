package com.example.tasktracking.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "attempted")
data class Attempt(
    var taskId: Int,
    var attemptDateStart: LocalDate,
    var attemptDateEnd: LocalDate,
    var completed: Int = 0,
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
)