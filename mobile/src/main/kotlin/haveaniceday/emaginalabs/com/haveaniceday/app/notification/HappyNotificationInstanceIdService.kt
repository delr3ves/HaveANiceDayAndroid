package haveaniceday.emaginalabs.com.haveaniceday.app.notification

import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService

class HappyNotificationInstanceIdService : FirebaseInstanceIdService() {

    val TAG = "HappyNotifidationMsg"

     override fun onTokenRefresh() {
         // Get updated InstanceID token.
         val refreshedToken = FirebaseInstanceId.getInstance().token
         Log.d(TAG, "Refreshed token: " + refreshedToken!!)
        //TODO send token to server
     }
}