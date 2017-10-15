package com.emaginalabs.haveaniceday.core.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.emaginalabs.haveaniceday.core.model.Notification

@Dao
interface NotificationDAO {

    @Query("SELECT * FROM notification order by id desc")
    fun findAll(): List<Notification>

    @Insert
    fun insert(notification: Notification): Long

    @Query("SELECT count(*) FROM notification where read=0 order by id desc")
    fun countUnread(): Int
}