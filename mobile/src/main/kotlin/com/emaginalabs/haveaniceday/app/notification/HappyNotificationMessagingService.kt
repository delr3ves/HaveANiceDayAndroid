package com.emaginalabs.haveaniceday.app.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.support.v4.app.NotificationCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.emabinalabs.haveaniceday.R
import com.emaginalabs.haveaniceday.app.HaveANiceDayApplication
import com.emaginalabs.haveaniceday.app.MainActivity
import com.emaginalabs.haveaniceday.core.dao.NotificationDAO
import com.emaginalabs.haveaniceday.core.model.Notification
import com.emaginalabs.haveaniceday.core.usecase.UpdateNotificationBudget
import com.emaginalabs.haveaniceday.core.utils.TimeProvider
import com.github.salomonbrys.kodein.LazyKodein
import com.github.salomonbrys.kodein.LazyKodeinAware
import com.github.salomonbrys.kodein.android.appKodein
import com.github.salomonbrys.kodein.instance
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.jetbrains.anko.runOnUiThread
import java.util.*

object HappyNotificationMessaging {
    val RECEIVED_NOTIFICATION = "receivedNotification"
}

class HappyNotificationMessagingService : FirebaseMessagingService(), LazyKodeinAware {

    override val kodein = LazyKodein(appKodein)

    private val notificationDAO: NotificationDAO by instance()
    private val timeProvider: TimeProvider by instance()
    private val updateNotificationBudget: UpdateNotificationBudget by instance()

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val title = remoteMessage.data.get("title")
        val message = remoteMessage.data.get("message")
        val photo = remoteMessage.data.get("photoUrl")
        val notification = Notification(
                title = title,
                message = message,
                photoUrl = photo,
                createdAt = timeProvider.now().millis,
                read = false
        )
        val createdNotification = notification.copy(id = notificationDAO.insert(notification))
        sendNotification(createdNotification)
        updateNotificationBudget.execute()
    }

    private fun sendNotification(notification: Notification) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra(HappyNotificationMessaging.RECEIVED_NOTIFICATION, notification)

        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)

        val appName = getString(R.string.app_name)
        val notificationBuilder = NotificationCompat.Builder(this,
                (this.application as HaveANiceDayApplication).channelId())
                .setSmallIcon(R.mipmap.ic_notify)
                .setBadgeIconType(R.mipmap.ic_launcher)
                .setChannelId(appName)
                .setContentTitle(notification.title)
                .setContentText(notification.message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setNumber(1)
                .setColor(255)
                .setWhen(System.currentTimeMillis())
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationId = notification.id?.toInt() ?: Random().nextInt()

        if (notification.photoUrl != null) {
            runOnUiThread {
                Glide.with(applicationContext)
                        .asBitmap()
                        .load(notification.photoUrl)
                        .into(object : SimpleTarget<Bitmap>() {
                            override fun onResourceReady(resource: Bitmap?, transition: Transition<in Bitmap>?) {
                                val bigPictureStyle = NotificationCompat.BigPictureStyle()
                                        .bigPicture(resource)
                                        .setSummaryText(notification.message)
                                notificationBuilder.setStyle(bigPictureStyle)
                                notificationManager.notify(notificationId, notificationBuilder.build())
                            }
                        })
            }
        } else {
            notificationManager.notify(notificationId, notificationBuilder.build())
        }

    }

}
