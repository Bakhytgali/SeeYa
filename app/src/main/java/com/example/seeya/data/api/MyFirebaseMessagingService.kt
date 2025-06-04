package com.example.seeya.data.api

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.*
import com.example.seeya.R
import com.example.seeya.utils.TokenManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    // Вызывается, когда приходит новое уведомление
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        Log.d("FCM", "Message received: ${remoteMessage.data}")

        // Если приходит data payload (то, что ты сам отправляешь с бэка)
        remoteMessage.data.let { data ->
            val title = data["title"] ?: "Уведомление"
            val message = data["message"] ?: ""
            showNotification(title, message)
        }
    }

    // Если обновился токен — тут ты можешь отправить его на сервер снова (опционально)
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "New token: $token")

        // Сохраняем токен локально
        TokenManager.saveFcmToken(applicationContext, token)

        // TODO: при желании можешь отправить его на сервер, если юзер уже авторизован
    }

    private fun showNotification(title: String, message: String) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "seeya_channel_id"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "SeeYa Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = Builder(this, channelId)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.seeya_logo)
            .setPriority(PRIORITY_HIGH)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }
}
