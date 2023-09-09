package com.example.tasktracking

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.time.LocalDate


@Entity
@TypeConverters(Converters::class)
data class Schedule(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val period: String = "Day",
    val startDate: LocalDate = LocalDate.now(),
    val endDate: LocalDate = LocalDate.MAX,
    val frequency: List<String> = listOf("Sun", "Mon", "Tues", "Wed", "Thurs", "Fri", "Sat")
)

@Dao
interface ScheduleDao {
    @Query("SELECT * FROM schedule")
    fun getAll(): List<Schedule>

    @Query("SELECT * FROM schedule WHERE id IS (:id)")
    fun getId(id: Int): Schedule

    @Insert
    fun insert(vararg schedule: Schedule)

    @Delete
    fun delete(schedule: Schedule)
}

open class Task(open var name: String?, open var scheduleID: Int?)

@Entity
data class DurationTask(
    override var name: String?,
    override var scheduleID: Int?,
    var timeGoal: Long?,
    var timeCompleted: Long?,
    var unit: Int?,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
) : Task(name, scheduleID)

@Entity
data class RepetitionTask(
    override var name: String?,
    override var scheduleID: Int?,
    var repGoal: Int?,
    var repCompleted: Int?,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
) : Task(name, scheduleID)

class Converters {
    @TypeConverter
    fun fromTimestamp(value: String?): LocalDate? {
        return value?.let { LocalDate.parse(value) }
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDate?): String? {
        return date?.toString()
    }

    @TypeConverter
    fun fromString(value: String?): List<String>? {
        return value?.split(',')
    }

    @TypeConverter
    fun listToString(list: List<String>?): String? {
        return list?.joinToString()
    }
}

@Dao
interface DurationTaskDao {
    @Query("SELECT * FROM durationtask")
    fun getAll(): List<DurationTask>

    @Query("SELECT * FROM durationtask WHERE id IS (:id)")
    fun getId(id: Int): DurationTask

    @Insert
    fun insert(task: DurationTask)

    @Delete
    fun delete(tasks: DurationTask)
}

@Dao
interface RepetitionTaskDao {
    @Query("SELECT * FROM repetitiontask")
    fun getAll(): List<RepetitionTask>

    @Query("SELECT * FROM repetitiontask WHERE id IS (:id)")
    fun getId(id: Int): RepetitionTask

    @Insert
    fun insert(task: RepetitionTask)

    @Delete
    fun delete(tasks: RepetitionTask)
}

@Database(entities = [Schedule::class, DurationTask::class, RepetitionTask::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun scheduleDao(): ScheduleDao

    abstract fun durationTaskDao(): DurationTaskDao

    abstract fun repetitionTaskDao(): RepetitionTaskDao
}
