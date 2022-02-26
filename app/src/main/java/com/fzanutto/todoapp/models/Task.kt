package com.fzanutto.todoapp.models

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.ceil


class Task(
    val id: Int,
    var title: String,
    var description: String,
    var repeat: RepeatType,
    val initialDate: Date,
    var interval: Int
) {
    var done = false
    var active = true

    fun getNextRunEstimatedString(): String {
        return getNextRun()?.let { date ->
            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
             sdf.format(date)
        } ?: ""
    }

    fun getNextRun(): Date? {
        if (done || !active) return null

        val initialMilli = initialDate.time
        val now = Date().time

        return when(repeat) {
            RepeatType.DO_NOT_REPEAT -> {
                initialDate
            }
            RepeatType.YEARLY -> {
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = initialMilli
                while (calendar.time.time < now) {
                    calendar.add(Calendar.YEAR, interval)
                }

                Date(calendar.timeInMillis)
            }
            RepeatType.MONTHLY -> {
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = initialMilli
                while (calendar.time.time < now) {
                    calendar.add(Calendar.MONTH, interval)
                }

                Date(calendar.timeInMillis)
            }
            else -> {
                val diff = now - initialMilli
                val intervalInMilli = interval * repeat.getIntervalMilliFactor()
                val mult = ceil(diff / intervalInMilli.toDouble()).toInt()
                Date(initialMilli + mult * intervalInMilli)
            }
        }
    }

    override fun toString(): String {
        val nextRun = getNextRun()?.let {
            it.time - Date().time
        } ?: "none"
        return "$id - $title - $nextRun"
    }
}