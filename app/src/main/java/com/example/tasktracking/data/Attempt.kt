package com.example.tasktracking.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey

@Entity(tableName = "attempted", foreignKeys = [ForeignKey(entity = Task::class, parentColumns = ["id"], childColumns = ["taskId"], onDelete = CASCADE)])
data class Attempt(
    var taskId: Int = 0,
    var attemptDateStart: String,
    var attemptDateEnd: String,
    var completed: Int = 0,
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
)