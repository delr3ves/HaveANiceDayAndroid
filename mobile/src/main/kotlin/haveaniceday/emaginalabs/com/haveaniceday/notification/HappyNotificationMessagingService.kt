package haveaniceday.emaginalabs.com.haveaniceday.notification

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class HappyNotificationMessagingService: FirebaseMessagingService() {

    override fun onMessageReceived(p0: RemoteMessage?) {
        super.onMessageReceived(p0)
        Log.i("NiceDayReceiver", p0.toString())
    }
}

