package com.mandriv.keepfit.data

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.*

class Conventers {

    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

    @TypeConverter
    fun fromIsoString(value: String?): Calendar? {
        return value?.let {
            sdf.timeZone = TimeZone.getTimeZone("UTC");
            val cal = Calendar.getInstance()
            cal.time = sdf.parse(value);
            return cal;
        }
    }

    @TypeConverter
    fun toIsoString(date: Calendar?): String? {
        return date?.let {
            sdf.timeZone = TimeZone.getTimeZone("UTC");
            return sdf.format(date.time);
        }
    }
}