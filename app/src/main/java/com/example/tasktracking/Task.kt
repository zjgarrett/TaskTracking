package com.example.tasktracking

import java.time.LocalDate
import kotlin.time.Duration

data class SchedulingData(
    var period: String = "Day",
    var startDate: LocalDate = LocalDate.now(),
    var endDate: LocalDate = LocalDate.MAX,
    var frequency: List<String> = listOf("Sun", "Mon", "Tues", "Wed", "Thurs", "Fri", "Sat")
)

open class Task(open var name: String, open var frequency: SchedulingData) {

}

data class DurationTask(
    override var name: String, override var frequency: SchedulingData,
    var timeGoal: Duration, var timeCompleted: Duration) : Task(name, frequency) {

}

data class RepetitionTask(override var name: String, override var frequency: SchedulingData,
                     var repGoal: Int, var repCompleted: Int) : Task(name, frequency) {

}