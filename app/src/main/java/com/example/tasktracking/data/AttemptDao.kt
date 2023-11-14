package com.example.tasktracking.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface AttemptDao {
    @Query("SELECT * FROM attempted")
    fun getAllAttempted(): Flow<List<Attempt>>

    @Query("SELECT * FROM attempted WHERE attemptId IS (:id)")
    fun getAttempted(id: Int): Flow<Attempt>

    @Query("SELECT * FROM attempted " +
           "WHERE (attemptDateStart <= :date AND attemptDateEnd >= :date) " +
            "ORDER BY taskId"
    )
    fun getByDate(date: LocalDate): Flow<List<Attempt>>

    @Upsert
    suspend fun upsert(attempt: Attempt)

    @Delete
    suspend fun delete(attempt: Attempt)
}
