package com.example.chmovie.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.chmovie.R
import com.example.chmovie.presentation.main.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MyFirebaseMessagingService : FirebaseMessagingService() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        remoteMessage.notification?.let {
            sendNotification(it.body, remoteMessage.data)
        }
    }

    override fun onNewToken(token: String) {
        // Handle token refresh if needed
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun sendNotification(messageBody: String?, data: Map<String, String>) {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            putExtra("id", data["id"]?.toIntOrNull())
            putExtra("type", data["type"])
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val channelId = getString(R.string.app_name)
        createNotificationChannel(channelId)

        val notification = buildNotification(channelId, messageBody, pendingIntent)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId: String) {
        val channel = NotificationChannel(
            channelId,
            "Rating notification",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Channel for rating notifications"
        }

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun buildNotification(
        channelId: String,
        messageBody: String?,
        pendingIntent: PendingIntent
    ): Notification {
        return NotificationCompat.Builder(this, channelId).apply {
            setSmallIcon(R.mipmap.ic_launcher)
            setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
            setContentTitle(getString(R.string.app_name))
            setContentText(messageBody)
            setAutoCancel(true)
            setContentIntent(pendingIntent)
            setDefaults(Notification.DEFAULT_ALL)
        }.build()
    }
}
