package com.emaginalabs.haveaniceday.core.usecase

import android.content.Context
import com.emaginalabs.haveaniceday.core.dao.NotificationDAO
import me.leolin.shortcutbadger.ShortcutBadger

class UpdateNotificationBudget(val notificationDAO: NotificationDAO, val context: Context) {

    fun execute(): Int {
        val unread = notificationDAO.countUnread()
        ShortcutBadger.applyCount(context, unread)
        return unread
    }

}