package haveaniceday.emaginalabs.com.haveaniceday.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import haveaniceday.emaginalabs.com.haveaniceday.R
import haveaniceday.emaginalabs.com.haveaniceday.ReceivedMessageActivity

object HappyNotificationMessaging {
    val RECEIVED_MESSAGE = "receivedMessage"
}

class HappyNotificationMessagingService : FirebaseMessagingService() {

    val TAG = "HappyNotifidationMsg"


    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val title = remoteMessage.data.get("title")
        val message = remoteMessage.data.get("message")
        val photo = remoteMessage.data.get("photoUrl")
        val notification = Notification(title, message, photo)
        sendNotification(notification)
    }

    private fun sendNotification(notification: Notification) {
        val intent = Intent(this, ReceivedMessageActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra(HappyNotificationMessaging.RECEIVED_MESSAGE, notification.message)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)

        val notificationBuilder = NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_notify)
                .setContentTitle(notification.title)
                .setContentText(notification.message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }
}

