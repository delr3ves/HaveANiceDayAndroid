package com.emaginalabs.haveaniceday.app.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.support.v4.app.NotificationCompat
import com.emabinalabs.haveaniceday.R
import com.emaginalabs.haveaniceday.app.ReceivedMessageActivity
import com.emaginalabs.haveaniceday.core.dao.NotificationDAO
import com.emaginalabs.haveaniceday.core.model.Notification
import com.github.salomonbrys.kodein.LazyKodein
import com.github.salomonbrys.kodein.LazyKodeinAware
import com.github.salomonbrys.kodein.android.appKodein
import com.github.salomonbrys.kodein.instance
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.net.URL
import java.util.*


object HappyNotificationMessaging {
    val RECEIVED_NOTIFICATION = "receivedNotification"
}

class HappyNotificationMessagingService : FirebaseMessagingService(), LazyKodeinAware {

    override val kodein = LazyKodein(appKodein)

    val notificationDAO: NotificationDAO by instance()

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val title = remoteMessage.data.get("title")
        val message = remoteMessage.data.get("message")
        val photo = remoteMessage.data.get("photoUrl")
        val notification = Notification(title = title, message = message, photoUrl = photo)

        val createdNotification = notification.copy(id = notificationDAO.insert(notification))
        sendNotification(createdNotification)
    }

    private fun sendNotification(notification: Notification) {
        val intent = Intent(this, ReceivedMessageActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra(HappyNotificationMessaging.RECEIVED_NOTIFICATION, notification)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)


        val notificationBuilder = NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_notify)
                .setContentTitle(notification.title)
                .setContentText(notification.message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)

        notification.photoUrl?.let {
            val imageUri = URL(notification.photoUrl)
            val bitmap = BitmapFactory.decodeStream(imageUri.openConnection().getInputStream());

            val bigPictureStyle = NotificationCompat.BigPictureStyle()
                    .bigPicture(bitmap)
                    .setSummaryText(notification.message)
            notificationBuilder.setStyle(bigPictureStyle)
        }

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(Random().nextInt(), notificationBuilder.build())
    }
}

