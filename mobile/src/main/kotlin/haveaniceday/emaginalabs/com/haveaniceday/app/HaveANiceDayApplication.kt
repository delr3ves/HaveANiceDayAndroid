package haveaniceday.emaginalabs.com.haveaniceday.app

import android.app.Application
import com.crashlytics.android.Crashlytics
import com.google.firebase.messaging.FirebaseMessaging
import haveaniceday.emaginalabs.com.haveaniceday.R
import io.fabric.sdk.android.Fabric

class HaveANiceDayApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Fabric.with(this, Crashlytics())
        FirebaseMessaging.getInstance().subscribeToTopic(resources.getString(R.string.topic_name))

    }
}