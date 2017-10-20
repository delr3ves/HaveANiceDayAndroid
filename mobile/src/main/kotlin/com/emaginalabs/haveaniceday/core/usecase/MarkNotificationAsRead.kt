package com.emaginalabs.haveaniceday.core.usecase

import com.emaginalabs.haveaniceday.core.dao.NotificationDAO
import com.emaginalabs.haveaniceday.core.model.Notification

class MarkNotificationAsRead(val notificationDAO: NotificationDAO,
                             val updateNotificationBudget: UpdateNotificationBudget) {

    fun execute(notification: Notification): Notification {
        if (!notification.read) {
            val readNotification = notification.copy(read = true)
            notificationDAO.update(readNotification)
            updateNotificationBudget.execute()
            return readNotification
        }
        return notification
    }
}