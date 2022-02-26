package com.fzanutto.todoapp.models

import android.content.Context
import com.fzanutto.todoapp.R

enum class RepeatType(val type: Int) {
    DO_NOT_REPEAT(1),
    DAILY(2),
    HOURLY(3),
    MONTHLY(4),
    MINUTES(5),
    YEARLY(6),
    WEEKLY(7),
    SECONDS(8);

    companion object {
        fun fromInt(value: Int): RepeatType? {
            return values().firstOrNull { it.type == value }
        }
    }

    fun getIntervalMilliFactor(): Long {
        return when(this) {
            SECONDS -> 1000
            MINUTES -> 1000 * 60
            HOURLY -> 1000 * 60 * 60
            DAILY -> 1000 * 60 * 60 * 24
            WEEKLY -> 1000 * 60 * 60 * 24 * 7
            MONTHLY -> 0
            YEARLY -> 0
            DO_NOT_REPEAT -> 0
        }
    }

    fun getName(context: Context): String {
        return when (this) {
            SECONDS -> "a cada X segundos"
            WEEKLY -> context.getString(R.string.weekly)
            HOURLY -> "a cada X horas"
            YEARLY -> "a cada X anos"
            MONTHLY -> "a cada X meses"
            MINUTES -> "a cada X minutos"
            DAILY -> "a cada X dias"
            DO_NOT_REPEAT -> "Não repetir"
        }
    }


    fun getShortName(): String {
        return when (this) {
            SECONDS -> "segundo(s)"
            WEEKLY -> "semana(s)"
            HOURLY -> "hora(s)"
            YEARLY -> "ano(s)"
            MONTHLY -> "mes(es)"
            MINUTES -> "min"
            DAILY -> "dia(s)"
            else -> ""
        }
    }
}