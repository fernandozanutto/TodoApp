package com.fzanutto.todoapp.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class ActionReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val text = intent.getStringExtra(action1) ?: "Fallback"
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }
}