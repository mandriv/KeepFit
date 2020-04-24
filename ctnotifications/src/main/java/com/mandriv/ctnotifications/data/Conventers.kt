package com.mandriv.ctnotifications.data

import androidx.room.TypeConverter

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
            CustomNumericTrigger(name, min, max)
        }

    }
}