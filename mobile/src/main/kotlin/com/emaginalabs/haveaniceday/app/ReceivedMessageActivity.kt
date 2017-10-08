package com.emaginalabs.haveaniceday.app

import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.emabinalabs.haveaniceday.R
import com.emaginalabs.haveaniceday.app.notification.HappyNotificationMessaging
import com.emaginalabs.haveaniceday.core.model.Notification
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import pl.droidsonroids.gif.GifTextView
import java.net.URL


class ReceivedMessageActivity : AppCompatActivity() {

    @BindView(R.id.received_message)
    lateinit var textContainer: TextView
    @BindView(R.id.received_image)
    lateinit var imageContainer: ImageView
    @BindView(R.id.image_loader)
    lateinit var imageLoader: GifTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_received_message)
        ButterKnife.bind(this)

        val happyNotification = intent?.getParcelableExtra<Notification>(
                HappyNotificationMessaging.RECEIVED_NOTIFICATION)

        val message = happyNotification?.message
        message.let {
            textContainer.text = message
        }
        doAsync {
            try {
                happyNotification?.photoUrl?.let {
                    val imageStream = URL(happyNotification.photoUrl).openStream()
                    val image = BitmapFactory.decodeStream(imageStream)
                    uiThread {
                        image.let {
                            imageLoader.visibility = View.GONE
                            imageContainer.setImageBitmap(image)
                            imageContainer.visibility = View.VISIBLE
                        }
                    }
                }
            } catch (e: Throwable) {
                // log error
            }
        }
    }

}
