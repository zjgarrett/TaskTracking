package com.example.tasktracking.data

import androidx.room.TypeConverter
import java.time.DayOfWeek
import java.time.LocalDate

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

    @TypeConverter
    fun dayOfWeekSetToString(days: MutableSet<DayOfWeek>?): String {
        val result = mutableListOf<Int>()
        if (days != null) {
            for (day in days) {
                result.add(day.value)
            }
        }
        return result.joinToString(','.toString())
    }

    @TypeConverter
    fun stringToDayOfWeekSet(value: String?): MutableSet<DayOfWeek> {
        val list = value?.split(',')
        val result = mutableSetOf<DayOfWeek>()
        if (list != null) {
            for (day in list) {
                result.add(DayOfWeek.of(day.toInt()))
            }
        }
        return result
    }

    @TypeConverter
    fun periodToInt(period: Period?): Int? {
        return period?.ordinal
    }

    @TypeConverter
    fun intToPeriod(value: Int?): Period {
        return Period.of(value)
    }
}