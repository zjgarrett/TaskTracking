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

    // Either the date needs to be within the provided range (inclusive),
    // or the date needs to be later or on the start date with the stored end date being LocalDate.MAX
    @Transaction
    @Query("SELECT * FROM tasks " +
            "WHERE ((tasks.startDate <= :date AND tasks.endDate >= :date) " +
            "OR (tasks.startDate <= :date AND tasks.endDate == '+999999999-12-31'))"
    )
    fun getByDateTasksWithAttempted(date: LocalDate): Flow<List<TaskWithAttempted>>

    @Transaction
    @Query("SELECT * FROM tasks WHERE id = (:id)")
    fun getByIdTaskWithAttempted(id: Int): Flow<TaskWithAttempted>
}