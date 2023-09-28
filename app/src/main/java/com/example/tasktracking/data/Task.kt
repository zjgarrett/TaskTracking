package com.example.tasktracking.data


import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.Locale

val DEFAULT_FREQUENCY = listOf(DayOfWeek.SUNDAY, DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY)
enum class TaskType {
    Repetition,
    Duration
}

enum class Period {
    DAY,
    WEEK,
    MONTH;
    override fun toString(): String {
        return name[0].toString() + name.substring(1).lowercase(Locale.getDefault())
    }

    fun toAdjective(): String {
        return when(this) {
            DAY -> "Daily"
            WEEK -> "Weekly"
            MONTH -> "Monthly"
        }
    }

    companion object {
        fun of(value: Int?): Period {
            return when(value) {
                0 -> DAY
                1 -> WEEK
                2 -> MONTH
                else -> throw Throwable("Invalid Enum Value")
            }
        }
    }
}

fun DayOfWeek.toTitleCase(): String {
    return name[0].toString() + name.substring(1).lowercase(Locale.getDefault())
}


@Entity(tableName = "tasks")
data class Task(
    var name: String,
    var goal: Int,
    var type: TaskType,
    val period: Period = Period.DAY,
    val startDate: LocalDate = LocalDate.now(),
    val endDate: LocalDate = LocalDate.MAX,
    val frequency: MutableSet<DayOfWeek> = DEFAULT_FREQUENCY.toMutableSet(),
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)

