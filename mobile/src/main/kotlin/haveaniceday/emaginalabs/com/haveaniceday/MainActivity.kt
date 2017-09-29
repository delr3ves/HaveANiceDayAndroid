package haveaniceday.emaginalabs.com.haveaniceday

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.google.firebase.messaging.FirebaseMessaging
import haveaniceday.emaginalabs.com.haveaniceday.notification.HappyNotificationMessaging
import haveaniceday.emaginalabs.com.haveaniceday.notification.HappyNotificationMessagingService

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FirebaseMessaging.getInstance().subscribeToTopic("haveANiceDay");
    }
}
