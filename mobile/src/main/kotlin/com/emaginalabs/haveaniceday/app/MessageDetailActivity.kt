package com.emaginalabs.haveaniceday.app

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.emabinalabs.haveaniceday.R
import com.emaginalabs.haveaniceday.app.notification.HappyNotificationMessaging
import com.emaginalabs.haveaniceday.core.model.Notification
import me.leolin.shortcutbadger.ShortcutBadger


class MessageDetailActivity : AppCompatActivity() {

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

        ShortcutBadger.applyCount(this, 0) //TODO move to a ShortcutBadgerManager
    }

    @OnClick(R.id.share_message)
    fun shareMessage() {
        val happyNotification = intent?.getParcelableExtra<Notification>(
                HappyNotificationMessaging.RECEIVED_NOTIFICATION)
        val sharingIntent = Intent(android.content.Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, happyNotification?.title)
        sharingIntent.putExtra(Intent.EXTRA_TEXT, happyNotification?.message)
        sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(happyNotification?.photoUrl))
        sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(sharingIntent, getString(R.string.share_choose_title)))
    }

}