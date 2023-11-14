package com.example.tasktracking.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks")
    fun getAllTasks(): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE id IS (:id)")
    fun getTask(id: Int): Flow<Task>

    @Query("SELECT * FROM tasks " +
            "WHERE ((tasks.startDate <= :date AND tasks.endDate >= :date) " +
            "OR (tasks.startDate <= :date AND tasks.endDate == '+999999999-12-31')) " +
            "ORDER BY id"
    )
    fun getByDate(date: LocalDate): Flow<List<Task>>

    @Upsert
    suspend fun upsert(task: Task)

    @Delete
    suspend fun delete(task: Task)
}
