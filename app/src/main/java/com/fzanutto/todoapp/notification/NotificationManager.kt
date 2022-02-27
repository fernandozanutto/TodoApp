package com.fzanutto.todoapp.notification

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.fzanutto.todoapp.models.Task
import java.util.Date

object NotificationManager {

    const val channelID = "channel1"
    const val titleExtra = "titleExtra"
    const val messageExtra = "messageExtra"
    const val taskIdTag = "taskId"

    fun createNotificationChannel(context: Context) {
        val name = "Todo List"
        val descriptionText = ""
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(channelID, name, importance)
        channel.description = descriptionText

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    fun scheduleTaskNotification(context: Context, task: Task) {

        val time = task.getNextRun() ?: return
        if (Date() > time) return

        val intent = Intent(context, TaskNotificationReceiver::class.java)

        intent.putExtra(titleExtra, task.title)
        intent.putExtra(messageExtra, task.description)
        intent.putExtra(taskIdTag, task.id)

        val notificationID = task.id.taskIdToNotificationId()

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            notificationID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time.time, pendingIntent)

        Log.d("notification", "$notificationID ${task.title} ${time.time - Date().time}")
    }
}

fun Int.taskIdToNotificationId(): Int {
    return this * 2
}

fun Int.taskIdToActionId(): Int {
    return (this * 2) + 1
}
