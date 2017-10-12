package com.emaginalabs.haveaniceday.app

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.emabinalabs.haveaniceday.R
import com.emaginalabs.haveaniceday.app.notification.HappyNotificationMessaging
import com.emaginalabs.haveaniceday.core.model.Notification


class ReceivedMessageActivity : AppCompatActivity() {

    @BindView(R.id.received_message)
    lateinit var textContainer: TextView
    @BindView(R.id.received_message_title)
    lateinit var titleContainer: TextView
    @BindView(R.id.received_image)
    lateinit var imageContainer: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_received_message)
        ButterKnife.bind(this)

        val happyNotification = intent?.getParcelableExtra<Notification>(
                HappyNotificationMessaging.RECEIVED_NOTIFICATION)

        titleContainer.text = happyNotification?.title
        textContainer.text = happyNotification?.message
        GlideApp.with(this)
                .load(happyNotification?.photoUrl)
                .placeholder(R.mipmap.happy_loader)
                .into(imageContainer)

    }

}
