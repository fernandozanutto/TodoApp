package com.fzanutto.todoapp.models

import android.content.Context
import com.fzanutto.todoapp.R
import java.text.SimpleDateFormat
import java.time.Duration
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.ceil


class Task(
    val id: Int,
    var title: String,
    var description: String,
    var repeat: RepeatType,
    var initialDate: Date,
    var interval: Int
) {
    var done = false
    var active = true

    fun getNextRunEstimatedString(context: Context): String {
        return getNextRun()?.let { date ->
            val duration = Duration.between(Date().toInstant(), date.toInstant())

            val days = duration.toDays()
            val hours = duration.toHours()
            val minutes = duration.toMinutes()

            if (minutes < 1) {
                context.getString(R.string.in_less_than_1_minute)
            }

            else if (minutes < 60) {
                context.getString(R.string.in_minutes, minutes.toString())
            }

            else if (hours < 24) {
                context.getString(R.string.in_days, hours.toString())
            }

            else if (days < 30) {
                context.getString(R.string.in_hours, days.toString())
            }

            else {
                val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                sdf.format(date)
            }
        } ?: ""
    }

    fun getNextRun(): Date? {
        if (done || !active) return null

        val initialMilli = initialDate.time
        val now = Date().time

        return when(repeat) {
            RepeatType.DO_NOT_REPEAT -> {
                if (initialDate > Date()) initialDate else null
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
                val diff = (now - initialMilli).coerceAtLeast(0)
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