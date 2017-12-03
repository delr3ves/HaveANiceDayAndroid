package com.emaginalabs.haveaniceday.core.dao.database

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.arch.persistence.room.migration.Migration
import com.emaginalabs.haveaniceday.core.dao.NotificationDAO
import com.emaginalabs.haveaniceday.core.model.Notification
import org.joda.time.DateTime


@Database(entities = arrayOf(Notification::class), version = 3)
@TypeConverters(DBConverters.StatusConverter::class)
abstract class HaveANiceDayDatabase : RoomDatabase() {
    abstract fun notificationDAO(): NotificationDAO

    object Migrations {
        val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                val millis = DateTime().millis
                database.execSQL("ALTER TABLE notification ADD COLUMN createdAt INTEGER NOT NULL DEFAULT $millis")
                database.execSQL("ALTER TABLE notification ADD COLUMN read INTEGER NOT NULL DEFAULT 1")
            }
        }

        val MIGRATION_2_3: Migration = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE notification ADD COLUMN status TEXT NOT NULL DEFAULT 'VISIBLE'")
            }
        }

        val ALL_MIGRATIONS: Array<Migration> = arrayOf(MIGRATION_1_2, MIGRATION_2_3)
    }

}