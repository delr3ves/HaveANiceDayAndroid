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

    }
}