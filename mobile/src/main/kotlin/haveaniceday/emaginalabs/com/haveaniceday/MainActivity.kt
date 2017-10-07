package haveaniceday.emaginalabs.com.haveaniceday

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.crashlytics.android.Crashlytics
import com.google.firebase.messaging.FirebaseMessaging
import io.fabric.sdk.android.Fabric

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fabric.with(this, Crashlytics())
        setContentView(R.layout.activity_main)
        FirebaseMessaging.getInstance().subscribeToTopic("haveANiceDay");
    }
}
