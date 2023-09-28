package com.example.tasktracking.data

import android.content.Context

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val appRepository: AppRepository
}

/**
 * [AppContainer] implementation that provides instance of [OfflineAppRepository]
 */
class AppDataContainer(private val context: Context) : AppContainer {
    /**
     * Implementation for [AppRepository]
     */
    override val appRepository: AppRepository by lazy {
        val db = AppDatabase.getDatabase(context)
        OfflineAppRepository(db.taskDao(), db.attemptDao(), db.taskWithAttemptedDao())
    }
}
