package com.emaginalabs.haveaniceday.core.dao.database

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.migration.Migration
import org.joda.time.DateTime

object DBMigrations {
    val MIGRATION_1_2: Migration = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            val millis = DateTime().millis
            database.execSQL("ALTER TABLE notification ADD COLUMN createdAt INTEGER NOT NULL DEFAULT $millis")
            database.execSQL("ALTER TABLE notification ADD COLUMN read INTEGER NOT NULL DEFAULT 1")
        }
    }
}
