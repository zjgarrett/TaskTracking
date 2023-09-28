package com.example.tasktracking.data

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface TaskWithAttemptedDao {
    @Transaction
    @Query("SELECT * FROM tasks")
    fun getTasksWithAttempted(): Flow<List<TaskWithAttempted>>

    @Transaction
    @Query("SELECT * FROM tasks " +
            "WHERE (" +
            "(tasks.period != :day)" +
            "OR (tasks.startDate <= :date AND tasks.endDate >= :date))"
    )
    fun getByDateTasksWithAttempted(date: LocalDate, day: Period = Period.DAY): Flow<List<TaskWithAttempted>>

    @Transaction
    @Query("SELECT * FROM tasks WHERE id = (:id)")
    fun getByIdTaskWithAttempted(id: Int): Flow<TaskWithAttempted>
}