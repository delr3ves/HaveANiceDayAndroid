package com.emaginalabs.haveaniceday.app

import android.app.Application
import android.arch.persistence.room.Room
import com.crashlytics.android.Crashlytics
import com.emabinalabs.haveaniceday.BuildConfig
import com.emabinalabs.haveaniceday.R
import com.emaginalabs.haveaniceday.core.dao.NotificationDAO
import com.emaginalabs.haveaniceday.core.dao.database.HaveANiceDayDatabase
import com.github.salomonbrys.kodein.*
import com.google.firebase.messaging.FirebaseMessaging
import io.fabric.sdk.android.Fabric
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build



class HaveANiceDayApplication : Application(), KodeinAware {

    val app = this
    override val kodein by Kodein.lazy {
        bind<HaveANiceDayDatabase>() with singleton {
            Room.databaseBuilder(app, HaveANiceDayDatabase::class.java, "haveANiceDay").build()
        }
        bind<NotificationDAO>() with singleton {
            instance<HaveANiceDayDatabase>().notificationDAO()
        }


    }

    override fun onCreate() {
        super.onCreate()
        if (!BuildConfig.DEBUG) {
            Fabric.with(this, Crashlytics())
        }
        FirebaseMessaging.getInstance().subscribeToTopic(resources.getString(R.string.topic_name))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val appName = getString(R.string.app_name)

            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId(), appName, importance)
            channel.description = appName
            channel.enableLights(true)
            channel.lightColor = Color.YELLOW
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun channelId() = getString(R.string.app_name)
}