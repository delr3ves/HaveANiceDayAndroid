package haveaniceday.emaginalabs.com.haveaniceday.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import haveaniceday.emaginalabs.com.haveaniceday.R
import haveaniceday.emaginalabs.com.haveaniceday.ReceivedMessageActivity

class HappyNotificationInstanceIdService : FirebaseInstanceIdService() {

    val TAG = "HappyNotifidationMsg"

     override fun onTokenRefresh() {
         // Get updated InstanceID token.
         val refreshedToken = FirebaseInstanceId.getInstance().token
         Log.d(TAG, "Refreshed token: " + refreshedToken!!)
        //TODO send token to server
    }
}