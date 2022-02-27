package com.fzanutto.todoapp.notification

import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.fzanutto.todoapp.R
import com.fzanutto.todoapp.database.TaskRepository
import com.fzanutto.todoapp.notification.NotificationManager.channelID
import com.fzanutto.todoapp.notification.NotificationManager.messageExtra
import com.fzanutto.todoapp.notification.NotificationManager.taskIdTag
import com.fzanutto.todoapp.notification.NotificationManager.titleExtra
import com.fzanutto.todoapp.view.taskdetails.TaskDetailsActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class TaskNotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notificationTitle = intent.getStringExtra(titleExtra)
        val notificationBody = intent.getStringExtra(messageExtra)
        val taskId = intent.getIntExtra(taskIdTag, -1)

        if (taskId == -1) return

        CoroutineScope(Dispatchers.Default).launch {
            TaskRepository.initialize(context)
            val task = TaskRepository.getTaskById(taskId) ?: return@launch

            val clickPendingIntent = getClickIntent(context, taskId)

            val action = getNotificationAction(context, taskId)

            val notification = NotificationCompat.Builder(context, channelID)
                .setSmallIcon(R.mipmap.ic_task_launcher_round)
                .setContentTitle(notificationTitle)
                .setContentText(notificationBody)
                .setContentIntent(clickPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .addAction(action)
                .setAutoCancel(true)
                .setOngoing(true)
                .build()

            with(NotificationManagerCompat.from(context)) {
                cancel(taskId.taskIdToNotificationId())
                notify(taskId.taskIdToNotificationId(), notification)
            }

            NotificationManager.scheduleTaskNotification(context, task)
            Log.d("notification", "${task.id} ${task.title}")
        }
    }

    private fun getClickIntent(context: Context, taskId: Int): PendingIntent {
        val clickIntent = Intent(context, TaskDetailsActivity::class.java).apply {
            putExtra(TaskDetailsActivity.TASK_ID, taskId)
        }

        val resultPendingIntent = TaskStackBuilder.create(context).run {
            // Add the intent, which inflates the back stack
            addNextIntentWithParentStack(clickIntent)
            // Get the PendingIntent containing the entire back stack
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        }

        return resultPendingIntent
    }


    private fun getNotificationAction(context: Context, taskId: Int): NotificationCompat.Action {
        val actionIntent = Intent(context, CompleteTaskActionReceiver::class.java)
        actionIntent.putExtra(taskIdTag, taskId)

        Log.d("notification", "${taskId}")

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            taskId.taskIdToActionId(),
            actionIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Action.Builder(
            R.mipmap.ic_task_launcher_round,
            "Concluído",
            pendingIntent
        ).build()
    }
}