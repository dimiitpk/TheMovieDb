package com.dimi.moviedatabase.framework.cache.model

import androidx.room.TypeConverter
import java.util.*

class MyTypeConverters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}