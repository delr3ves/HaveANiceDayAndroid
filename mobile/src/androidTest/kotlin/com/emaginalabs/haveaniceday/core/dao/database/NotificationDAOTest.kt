package com.emaginalabs.haveaniceday.core.dao.database

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.emaginalabs.haveaniceday.core.dao.NotificationDAO
import com.emaginalabs.haveaniceday.core.model.Notification
import org.hamcrest.Matchers.*
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class NotificationDAOTest {

    private lateinit var notificationDAO: NotificationDAO
    private lateinit var database: HaveANiceDayDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getTargetContext()
        database = Room.inMemoryDatabaseBuilder(context, HaveANiceDayDatabase::class.java).build()
        notificationDAO = database.notificationDAO()
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun findAllNotificationsShouldReturnEmptyWhenNoNotificationsAreStored() {
        assertTrue(notificationDAO.findAll().isEmpty())
    }

    @Test
    fun storedNotificationShouldBeInFindAllList() {
        val storedNotification = givenAnStoredNotification()

        assertThat(notificationDAO.findAll(), contains(storedNotification))
    }

    @Test
    fun nonStoredNotificationShouldNotBeInNotificationList() {
        val inMemmoryNotification = givenAnInMemoryNotification()

        assertThat(notificationDAO.findAll(), not(contains(inMemmoryNotification)))
    }

    @Test
    fun countAllUnreadNotificationsShouldReturnZeroWhenAllNotificationsAreRead() {
        givenAnStoredNotification(isRead = true)

        assertThat(notificationDAO.countUnread(), equalTo(0))
    }

    @Test
    fun countAllUnreadNotificationsShouldReturn1WhenAllNotificationsAreRead() {
        givenAnStoredNotification(isRead = false)

        assertThat(notificationDAO.countUnread(), equalTo(1))
    }

    private fun givenAnStoredNotification(isRead: Boolean = true): Notification {
        val notification = givenAnInMemoryNotification(isRead)
        val createdNotification = notification.copy(id = notificationDAO.insert(notification))
        return createdNotification
    }

    private fun givenAnInMemoryNotification(isRead: Boolean = true): Notification {
        val notification = Notification(
                title = "any title",
                message = "any message",
                photoUrl = "photo url",
                createdAt = 0,
                read = isRead
        )
        return notification
    }

}