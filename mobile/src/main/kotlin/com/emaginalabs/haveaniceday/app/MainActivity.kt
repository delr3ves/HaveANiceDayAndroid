package com.emaginalabs.haveaniceday.app

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.request.RequestOptions
import com.emabinalabs.haveaniceday.R
import com.emaginalabs.haveaniceday.app.notification.HappyNotificationMessaging
import com.emaginalabs.haveaniceday.core.dao.NotificationDAO
import com.emaginalabs.haveaniceday.core.model.Notification
import com.github.salomonbrys.kodein.LazyKodein
import com.github.salomonbrys.kodein.LazyKodeinAware
import com.github.salomonbrys.kodein.android.appKodein
import com.github.salomonbrys.kodein.instance
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class MainActivity : AppCompatActivity(), LazyKodeinAware {

    override val kodein = LazyKodein(appKodein)

    val notificationDAO: NotificationDAO by instance()

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
                        NotificationListAdapter(it, notifications)
                notificationRefresh.isRefreshing = false
            }
        }
    }

    private class NotificationListAdapter(val context: Context,
                                          val notifications: List<Notification>) : BaseAdapter() {

        private val inflator: LayoutInflater

        init {
            this.inflator = LayoutInflater.from(context)
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
            val view: View?
            val vh: ListRowHolder
            if (convertView == null) {
                view = this.inflator.inflate(R.layout.notification_row, parent, false)
                vh = ListRowHolder(view)
                view.tag = vh
            } else {
                view = convertView
                vh = view.tag as ListRowHolder
            }
            val notification = getItem(position) as Notification
            vh.title?.text = notification.title
            vh.message?.text = notification.message
            GlideApp.with(context)
                    .load(notification.photoUrl)
                    .placeholder(R.mipmap.ic_launcher)
                    .apply(RequestOptions.circleCropTransform())
                    .override(150, 150)
                    .fitCenter()
                    .into(vh.image)

            view?.setOnClickListener { view ->
                val context = view.context
                val intent = Intent(context, MessageDetailActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.putExtra(HappyNotificationMessaging.RECEIVED_NOTIFICATION, notification)
                ContextCompat.startActivity(context, intent, null)
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
        val message: TextView?
        val title: TextView?
        val image: ImageView?

        init {
            this.message = row?.findViewById(R.id.notifciation_row_message)
            this.title = row?.findViewById(R.id.notifciation_row_title)
            this.image = row?.findViewById(R.id.notifciation_row_photo)
        }
    }
}
