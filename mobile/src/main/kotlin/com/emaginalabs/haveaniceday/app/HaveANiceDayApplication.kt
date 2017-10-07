package com.emaginalabs.haveaniceday.app

import android.app.Application
import com.crashlytics.android.Crashlytics
import com.emabinalabs.haveaniceday.BuildConfig
import com.emabinalabs.haveaniceday.R
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.KodeinAware
import com.github.salomonbrys.kodein.lazy
import com.google.firebase.messaging.FirebaseMessaging
import io.fabric.sdk.android.Fabric

class HaveANiceDayApplication : Application(), KodeinAware {

    override val kodein by Kodein.lazy {
        /* bindings */
    }

    override fun onCreate() {
        super.onCreate()
        if (!BuildConfig.DEBUG) {
            Fabric.with(this, Crashlytics())
        }
        FirebaseMessaging.getInstance().subscribeToTopic(resources.getString(R.string.topic_name))

    }
}