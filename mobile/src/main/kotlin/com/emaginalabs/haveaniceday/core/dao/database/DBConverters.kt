package com.emaginalabs.haveaniceday.core.dao.database

import android.arch.persistence.room.TypeConverter
import com.emaginalabs.haveaniceday.core.model.Notification.Companion.Status

object DBConverters {

    class StatusConverter {
        @TypeConverter
        fun toStatus(status: String): Status =
                Status.valueOf(status)

        @TypeConverter
        fun toString(status: Status): String =
                status.toString()
    }

}
