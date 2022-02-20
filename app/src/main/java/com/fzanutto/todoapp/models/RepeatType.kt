package com.fzanutto.todoapp.models

enum class RepeatType {
    DO_NOT_REPEAT,
    DAILY,
    HOURLY,
    MONTHLY,
    MINUTES,
    YEARLY,
    WEEKLY;

    fun getIntervalMilliFactor(): Long {
        return when(this) {
            MINUTES -> 1000 * 60
            HOURLY -> 1000 * 60 * 60
            DAILY -> 1000 * 60 * 60 * 24
            WEEKLY -> 1000 * 60 * 60 * 24
            MONTHLY -> 0
            YEARLY -> 0
            DO_NOT_REPEAT -> 0
        }
    }
}