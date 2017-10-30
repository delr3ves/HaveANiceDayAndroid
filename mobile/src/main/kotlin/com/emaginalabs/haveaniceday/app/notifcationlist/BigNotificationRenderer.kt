package com.emaginalabs.haveaniceday.app.notifcationlist

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.emabinalabs.haveaniceday.R
import com.emaginalabs.haveaniceday.app.GlideApp
import com.emaginalabs.haveaniceday.core.model.Notification
import com.emaginalabs.haveaniceday.core.usecase.MarkNotificationAsRead
import com.emaginalabs.haveaniceday.core.usecase.ShareNotification
import com.pedrogomez.renderers.Renderer
import org.jetbrains.anko.doAsync
import org.joda.time.Instant


class BigNotificationRenderer(val markNotificationAsRead: MarkNotificationAsRead,
                              val shareNotification: ShareNotification) : Renderer<Notification>() {

    val layoutId = R.layout.notification_row_big
    @BindView(R.id.notifciation_row_title)
    lateinit var title: TextView

    @BindView(R.id.notifciation_row_message)
    lateinit var message: TextView

    @BindView(R.id.notifciation_row_photo)
    lateinit var photo: ImageView

    @BindView(R.id.notifciation_row_received_at)
    lateinit var date: TextView

    override fun inflate(inflater: LayoutInflater, parent: ViewGroup): View =
            inflater.inflate(layoutId, parent, false)


    override fun render() {
        val notification = content
        doAsync {
            markNotificationAsRead.execute(notification)
        }
        showImage(notification)
        showText(notification)
        showDate(notification)
    }

    override fun setUpView(rootView: View) {
        ButterKnife.bind(this, rootView)
    }

    override fun hookListeners(rootView: View) {
    }

    fun showImage(notification: Notification) {
        GlideApp.with(context)
                .load(notification.photoUrl)
                .placeholder(R.drawable.image_placeholder)
                .into(photo)
    }

    fun showText(notification: Notification) {
        title.text = notification.title
        message.text = notification.message
    }

    fun showDate(notification: Notification) {
        val timeAgo = DateUtils.getRelativeTimeSpanString(
                notification.createdAt,
                Instant.now().millis,
                DateUtils.MINUTE_IN_MILLIS)
        date.text = timeAgo
    }

    @OnClick(R.id.share_message)
    fun configureShareButton() {
        shareNotification.execute(content)
    }
}