package com.fzanutto.todoapp.models

import java.util.Calendar
import java.util.Date
import kotlin.math.ceil

class Task(
    val title: String,
    val description: String,
    val repeat: RepeatType,
    val initialDate: Date,
    val interval: Long
) {
    val done = false
    val active = true


    fun getNextRun(): Date? {
        if (done || !active) return null

        val initialMilli = initialDate.time
        val now = Date().time
        val diff = now - initialMilli
        val intervalInMilli = interval *  repeat.getIntervalMilliFactor()

        val mult = ceil(diff / intervalInMilli.toDouble()).toInt()

        return when(repeat) {
            RepeatType.DO_NOT_REPEAT -> {
                initialDate
            }
            RepeatType.YEARLY -> {
                val calendar = Calendar.getInstance()
                calendar.add(Calendar.YEAR, mult)
                Date(calendar.timeInMillis)
            }
            RepeatType.MONTHLY -> {
                val calendar = Calendar.getInstance()
                calendar.add(Calendar.MONTH, mult)
                Date(calendar.timeInMillis)
            }
            else -> {
                Date(initialMilli + mult * intervalInMilli)
            }
        }
    }
}