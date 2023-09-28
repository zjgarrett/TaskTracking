package com.example.tasktracking.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface AttemptDao {
    @Query("SELECT * FROM attempted")
    fun getAllAttempted(): Flow<List<Attempt>>

    @Query("SELECT * FROM attempted WHERE id IS (:id)")
    fun getAttempted(id: Int): Flow<Attempt>

    @Upsert
    suspend fun upsert(attempt: Attempt)

    @Delete
    suspend fun delete(attempt: Attempt)
}
