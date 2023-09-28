package com.example.tasktracking.data

import kotlinx.coroutines.flow.Flow

class OfflineAppRepository(private val taskDao: TaskDao, private val attemptDao: AttemptDao) : AppRepository {
    override fun getAllTasksStream(): Flow<List<Task>> = taskDao.getAllTasks()

    override fun getTaskStream(id: Int): Flow<Task?> = taskDao.getTask(id)

    override suspend fun insertTask(task: Task) = taskDao.upsert(task)

    override suspend fun deleteTask(task: Task) = taskDao.delete(task)

    override suspend fun updateTask(task: Task) = taskDao.upsert(task)

    override fun getAllAttemptedStream(): Flow<List<Attempt>> = attemptDao.getAllAttempted()

    override fun getAttemptedStream(id: Int): Flow<Attempt?> = attemptDao.getAttempted(id)

    override suspend fun insertAttempt(attempt: Attempt) = attemptDao.upsert(attempt)

    override suspend fun deleteAttempt(attempt: Attempt) = attemptDao.delete(attempt)

    override suspend fun updateAttempt(attempt: Attempt) = attemptDao.upsert(attempt)
}
