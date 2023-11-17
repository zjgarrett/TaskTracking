package com.example.tasktracking.data

import android.util.Log
import com.example.tasktracking.ui.task.readableString
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
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

    override suspend fun addSkippedAttempts() {
        val tasksWithAttempts = taskWithAttemptedDao.getTasksWithAttempted().first()
        val today = LocalDate.now()
        for (taskAttempt in tasksWithAttempts) {
            val orderedAttempts = taskAttempt.attempts.sortedByDescending { it.attemptDateStart }
            if (
                orderedAttempts.isEmpty() ||
                (orderedAttempts[0].attemptDateEnd < today
                && orderedAttempts[0].attemptDateEnd != taskAttempt.task.endDate)
                ) {
                var startDate = if (orderedAttempts.isEmpty()) taskAttempt.task.startDate else orderedAttempts[0].attemptDateEnd.plusDays(1)
                while (startDate <= today) {
                    var endDate = when (taskAttempt.task.period) {
                        Period.DAY -> {
                            if (startDate.dayOfWeek in taskAttempt.task.frequency) {
                                startDate = startDate.plusDays(1)
                                continue
                            }
                            startDate
                        }
                        Period.WEEK -> {
                            startDate.plusDays((6 - (startDate.dayOfWeek.value % 7)).toLong())
                        }
                        Period.MONTH -> {
                            startDate.plusMonths(1).minusDays(startDate.dayOfMonth.toLong())
                        }
                    }

                    if (endDate > taskAttempt.task.endDate) endDate = taskAttempt.task.endDate

                    Log.d("NEW ATTEMPT ENTRY", taskAttempt.task.name + " " + startDate.readableString() + " " + endDate.readableString())
                    attemptDao.upsert(
                        Attempt(
                            taskAttempt.task.id,
                            startDate,
                            endDate,
                        )
                    )
                    startDate = endDate.plusDays(1)
                }
            }
        }

    }
}
