package com.emaginalabs.haveaniceday.app

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.widget.ListView
import butterknife.BindView
import butterknife.ButterKnife
import com.emabinalabs.haveaniceday.R
import com.emaginalabs.haveaniceday.app.notifcationlist.BigNotificationRenderer
import com.emaginalabs.haveaniceday.core.dao.NotificationDAO
import com.emaginalabs.haveaniceday.core.model.Notification
import com.github.salomonbrys.kodein.LazyKodein
import com.github.salomonbrys.kodein.LazyKodeinAware
import com.github.salomonbrys.kodein.android.appKodein
import com.github.salomonbrys.kodein.instance
import com.pedrogomez.renderers.ListAdapteeCollection
import com.pedrogomez.renderers.RendererAdapter
import com.pedrogomez.renderers.RendererBuilder
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


class MainActivity : AppCompatActivity(), LazyKodeinAware {

    override val kodein = LazyKodein(appKodein)


    val notificationDAO: NotificationDAO by instance()
    val notificationRenderer: BigNotificationRenderer by instance()

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
                val rendererBuilder = RendererBuilder<Notification>(notificationRenderer)

                val adapter = RendererAdapter<Notification>(
                        rendererBuilder,
                        ListAdapteeCollection(notifications))

                notificationList.adapter = adapter
                notificationRefresh.isRefreshing = false
            }
        }
    }
}
