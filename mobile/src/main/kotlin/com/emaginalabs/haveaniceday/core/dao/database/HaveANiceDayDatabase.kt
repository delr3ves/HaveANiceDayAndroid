package com.emaginalabs.haveaniceday.core.dao.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.emaginalabs.haveaniceday.core.dao.NotificationDAO
import com.emaginalabs.haveaniceday.core.model.Notification

@Database(entities = arrayOf(Notification::class), version = 1)
abstract class HaveANiceDayDatabase : RoomDatabase() {
    abstract fun notificationDAO(): NotificationDAO
}