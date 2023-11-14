package com.example.tasktracking.data

import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class OfflineAppRepository(
    private val taskDao: TaskDao,
    private val attemptDao: AttemptDao,
    private val taskWithAttemptedDao: TaskWithAttemptedDao
) : AppRepository {
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
    override fun getByIdTaskWithAttemptedStream(id: Int): Flow<TaskWithAttempted> = taskWithAttemptedDao.getByIdTaskWithAttempted(id)

    override fun getAllByDateTaskWithAttemptedStream(date: LocalDate): Flow<List<TaskWithAttempted>> = taskWithAttemptedDao.getByDateTasksWithAttempted(date)
//    {
//
//        val tasksFlow: Flow<List<Task>> = taskDao.getByDate(date)
//        val attemptsFlow: Flow<List<Attempt>> = attemptDao.getByDate(date)
//
//        var taskWithAttemptedFlow: Flow<List<TaskWithAttempted>> = flowOf(listOf<TaskWithAttempted>())
//        Log.d("HELP", "NOTHING????")
//        tasksFlow.combine(attemptsFlow) {
//            tasks, attempts -> {
//            Log.d("TASKS", tasks.toString())
//            Log.d("ATTEMPTS", attempts.toString())
//                var i = 0
//                val result = mutableListOf<TaskWithAttempted>()
//                for (task in tasks) {
//                    if (i < attempts.size && task.id == attempts[i].taskId) {
//                        result.add(TaskWithAttempted(task, listOf(attempts[i])))
//                        i += 1
//                    } else {
//                        result.add(TaskWithAttempted(task, listOf()))
//                    }
//                }
//                taskWithAttemptedFlow = flowOf(result.toList())
//            }
//        }
//
//        return taskWithAttemptedFlow
//    }

    override fun getTaskByDate(date: LocalDate): Flow<List<Task>> = taskDao.getByDate(date)

    override fun getAttemptByDate(date: LocalDate): Flow<List<Attempt>> = attemptDao.getByDate(date)
}
