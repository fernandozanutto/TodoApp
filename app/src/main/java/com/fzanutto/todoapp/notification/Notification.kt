package com.fzanutto.todoapp.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.fzanutto.todoapp.R


const val notificationID = 1
const val channelID = "channel1"
const val titleExtra = "titleExtra"
const val messageExtra = "messageExtra"
const val action1 = "action1"

class Notification : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        val actionIntent = Intent(context, ActionReceiver::class.java)
        actionIntent.putExtra(action1, "asdsadasdaz xzxc")


        val pendingIntent = PendingIntent.getBroadcast(
            context,
            notificationID,
            actionIntent,
            PendingIntent.FLAG_IMMUTABLE
        )


        val action = NotificationCompat.Action.Builder(
            R.drawable.ic_launcher_background,
            "Teste",
            pendingIntent
        ).build()

        val notification = NotificationCompat.Builder(context, channelID)
            .setSmallIcon(R.drawable.ic_baseline_edit_24)
            .setContentTitle(intent.getStringExtra(titleExtra))
            .setContentText(intent.getStringExtra(messageExtra))
            .setSound(null)
            .addAction(action)
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        manager.notify(notificationID, notification)
    }
}