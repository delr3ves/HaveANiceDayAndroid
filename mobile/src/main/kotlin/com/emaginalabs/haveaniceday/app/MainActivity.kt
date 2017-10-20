package com.emaginalabs.haveaniceday.app

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.request.RequestOptions
import com.emabinalabs.haveaniceday.R
import com.emaginalabs.haveaniceday.app.notification.HappyNotificationMessaging
import com.emaginalabs.haveaniceday.core.dao.NotificationDAO
import com.emaginalabs.haveaniceday.core.model.Notification
import com.emaginalabs.haveaniceday.core.usecase.MarkNotificationAsRead
import com.emaginalabs.haveaniceday.core.usecase.ShareNotification
import com.github.salomonbrys.kodein.LazyKodein
import com.github.salomonbrys.kodein.LazyKodeinAware
import com.github.salomonbrys.kodein.android.appKodein
import com.github.salomonbrys.kodein.instance
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.joda.time.Instant

class MainActivity : AppCompatActivity(), LazyKodeinAware {

    override val kodein = LazyKodein(appKodein)

    val notificationDAO: NotificationDAO by instance()
    val shareNotification: ShareNotification by instance()
    val markAsRead: MarkNotificationAsRead by instance()

    @BindView(R.id.notification_list)
    lateinit var notificationList: ListView

    @BindView(R.id.refresh_notification_list)
    lateinit var notificationRefresh: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        loadAllNotifications()
        notificationRefresh.setOnRefreshListener {
            loadAllNotifications()
        }
    }

    private fun loadAllNotifications() {
        doAsync {
            val notifications = notificationDAO.findAll()
            uiThread {
                notificationList.adapter =
                        NotificationListAdapter(it, shareNotification, markAsRead, notifications)
                notificationRefresh.isRefreshing = false
            }
        }
    }

    private class NotificationListAdapter(val context: Context,
                                          val shareNotification: ShareNotification,
                                          val markAsRead: MarkNotificationAsRead,
                                          val notifications: List<Notification>) : BaseAdapter() {

        private val inflator: LayoutInflater

        init {
            this.inflator = LayoutInflater.from(context)
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
            val view: View?
            val vh: ListRowHolder
            if (convertView == null) {
                view = this.inflator.inflate(R.layout.notification_row_big, parent, false)
                vh = ListRowHolder(view)
                view.tag = vh
            } else {
                view = convertView
                vh = view.tag as ListRowHolder
            }
            val notification = getItem(position) as Notification

            val timeAgo = DateUtils.getRelativeTimeSpanString(
                    notification.createdAt,
                    Instant.now().millis,
                    DateUtils.MINUTE_IN_MILLIS)

            vh.title?.text = notification.title
            vh.message?.text = notification.message
            vh.date?.text = timeAgo
            if (!notification.read) {
                vh.container?.background = ContextCompat.getDrawable(context, R.drawable.unread_message_row_box)
                doAsync {
                    markAsRead.execute(notification)
                }
            }

            GlideApp.with(context)
                    .load(notification.photoUrl)
                    .placeholder(R.drawable.image_placeholder)
                    .into(vh.image)

            vh.shareButton?.setOnClickListener { clickedButton ->
                shareNotification.execute(notification)
            }
            return view
        }

        override fun getItem(pos: Int): Any {
            return notifications[pos]
        }

        override fun getItemId(pos: Int): Long {
            return (getItem(pos) as Notification).let {
                it.id
            }!!
        }

        override fun getCount(): Int {
            return notifications.count()
        }
    }

    private class ListRowHolder(row: View?) {
        val title: TextView?
        val image: ImageView?
        val message: TextView?
        val date: TextView?
        val container: RelativeLayout?
        val shareButton: FloatingActionButton?

        init {
            this.title = row?.findViewById(R.id.notifciation_row_title)
            this.image = row?.findViewById(R.id.notifciation_row_photo)
            this.date = row?.findViewById(R.id.notifciation_row_received_at)
            this.message = row?.findViewById(R.id.notifciation_row_message)
            this.container = row?.findViewById(R.id.received_message_item_container)
            this.shareButton = row?.findViewById(R.id.share_message)
        }
    }
}
