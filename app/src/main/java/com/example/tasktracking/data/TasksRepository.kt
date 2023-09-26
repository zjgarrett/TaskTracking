package com.example.tasktracking.data

import kotlinx.coroutines.flow.Flow

/**
 * Repository that provides insert, update, delete, and retrieve of [Task] from a given data source.
 */
interface TasksRepository {
    /**
     * Retrieve all the Tasks from the the given data source.
     */
    fun getAllTasksStream(): Flow<List<Task>>

    /**
     * Retrieve an Task from the given data source that matches with the [id].
     */
    fun getTaskStream(id: Int): Flow<Task?>

    /**
     * Insert Task in the data source
     */
    suspend fun insertTask(task: Task)

    /**
     * Delete Task from the data source
     */
    suspend fun deleteTask(task: Task)

    /**
     * Update Task in the data source
     */
    suspend fun updateTask(task: Task)
}
