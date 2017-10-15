package com.emaginalabs.haveaniceday.core.dao.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.emaginalabs.haveaniceday.core.dao.NotificationDAO
import com.emaginalabs.haveaniceday.core.model.Notification
import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.migration.Migration



@Database(entities = arrayOf(Notification::class), version = 2)
abstract class HaveANiceDayDatabase : RoomDatabase() {
    abstract fun notificationDAO(): NotificationDAO

    object Migrations {
        val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE notification ADD COLUMN createdAt DATE DEFAULT (datetime('now','localtime'))")
                database.execSQL("ALTER TABLE notification ADD COLUMN read INTEGER DEFAULT 1")
            }
        }
    }
}