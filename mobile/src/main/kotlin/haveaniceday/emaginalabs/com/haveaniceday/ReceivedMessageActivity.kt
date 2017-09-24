package haveaniceday.emaginalabs.com.haveaniceday

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import haveaniceday.emaginalabs.com.haveaniceday.notification.HappyNotificationMessaging

class ReceivedMessageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_received_message)
        val textContainer = findViewById(R.id.received_message) as TextView
        val happyMessage = intent?.getStringExtra(HappyNotificationMessaging.RECEIVED_MESSAGE)
        if (happyMessage != null) {
            textContainer.text = happyMessage
        } else {
            textContainer.text = "Hi, someone has something beatiful to tell you"

        }
    }
}
