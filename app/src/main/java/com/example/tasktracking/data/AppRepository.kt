package com.example.tasktracking.data

import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

/**
 * Repository that provides insert, update, delete, and retrieve of [Task] from a given data source.
 */
interface AppRepository {
    /********** Tasks Section ************/
    /**
     * Retrieve all the [Task]s from the the given data source.
     */
    fun getAllTasksStream(): Flow<List<Task>>

    /**
     * Retrieve an [Task] from the given data source that matches with the [id].
     */
    fun getTaskStream(id: Int): Flow<Task?>

    /**
     * Insert [Task] in the data source
     */
    suspend fun insertTask(task: Task)

    /**
     * Delete [Task] from the data source
     */
    suspend fun deleteTask(task: Task)

    /**
     * Update [Task] in the data source
     */
    suspend fun updateTask(task: Task)

    /********** Attempted Section ************/
    /**
     * Retrieve all the [Attempt]s from the the given data source.
     */
    fun getAllAttemptedStream(): Flow<List<Attempt>>

    /**
     * Retrieve an [Attempt] from the given data source that matches with the [id].
     */
    fun getAttemptedStream(id: Int): Flow<Attempt?>

    /**
     * Insert [Attempt] in the data source
     */
    suspend fun insertAttempt(attempt: Attempt)

    /**
     * Delete [Attempt] from the data source
     */
    suspend fun deleteAttempt(attempt: Attempt)

    /**
     * Update [Attempt] in the data source
     */
    suspend fun updateAttempt(attempt: Attempt)

    /********** TaskWithAttempted Section ************/
    /**
     * Retrieve an [TaskWithAttempted] from the given data source that matches with the [id].
     */
    fun getByIdTaskWithAttemptedStream(id: Int): Flow<TaskWithAttempted>

    /**
     * Retrieve all [TaskWithAttempted]s from the given data source that overlap with the [date].
     */
    fun getAllByDateTaskWithAttemptedStream(date: LocalDate): Flow<List<TaskWithAttempted>>

    fun getTaskByDate(date: LocalDate): Flow<List<Task>>

    fun getAttemptByDate(date: LocalDate): Flow<List<Attempt>>
}
