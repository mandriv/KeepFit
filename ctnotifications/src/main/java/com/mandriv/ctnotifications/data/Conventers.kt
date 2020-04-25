package com.mandriv.ctnotifications.data

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.*

class Conventers {

    @TypeConverter
    fun fromCustomNumericList(list: List<CustomNumericTrigger>?): String {
        if (list != null) {
            return list.joinToString(",")
        }
        return ""
    }

    @TypeConverter
    fun toCustomNumericList(str: String?): List<CustomNumericTrigger> {
        if (str == null || str.isBlank()) {
            return emptyList()
        }
        return str.split(",").map { raw ->
            val values = raw.split("-")
            val name = values[0]
            val min = values[1].toIntOrNull()
            val max = values[2].toIntOrNull()
            val currentValue = values[3].toIntOrNull()
            CustomNumericTrigger(name, min, max, currentValue)
        }
    }

    private val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")

    @TypeConverter
    fun fromIsoString(value: String?): Calendar? {
        return value?.let {
            sdf.timeZone = TimeZone.getTimeZone("UTC")
            val cal = Calendar.getInstance()
            cal.time = sdf.parse(value)
            return cal
        }
    }

    @TypeConverter
    fun toIsoString(date: Calendar?): String? {
        return date?.let {
            sdf.timeZone = TimeZone.getTimeZone("UTC")
            return sdf.format(date.time)
        }
    }
}