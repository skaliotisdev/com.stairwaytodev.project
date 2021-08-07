package com.stairwaytodev.project.utils

import android.text.TextUtils
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object DateFormat {

    fun convertDateAsStringLocale(date: String): String {
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val dateParsed: Date = simpleDateFormat.parse(date)!!
        return DateFormat.getDateInstance(DateFormat.DATE_FIELD, Locale.getDefault()).format(dateParsed)
    }

    fun convertTimeAsStringLocale(time: String): String {
        val simpleDateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val splitter = TextUtils.split(time, ":")
        val hour = splitter[0]
        val minute = splitter[1]

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hour.toInt())
        calendar.set(Calendar.MINUTE, minute.toInt())

        var timeFormatted = simpleDateFormat.format(calendar.time)
        if (timeFormatted[0] == '0')
            timeFormatted = timeFormatted.substring(1)

        return timeFormatted
    }

    fun convertTimeAndDateAsString(time: String, date: String): String {
        val timedateString = "$time $date"
        val simpleDateFormat = SimpleDateFormat("hh:mm dd/MM/yyyy", Locale.getDefault())
        val dateParsed: Date = simpleDateFormat.parse(timedateString)!!
        return DateFormat.getInstance().format(dateParsed)
    }
}