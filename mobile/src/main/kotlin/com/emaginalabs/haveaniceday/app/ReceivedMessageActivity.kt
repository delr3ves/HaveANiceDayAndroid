package com.emaginalabs.haveaniceday.app

import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import android.widget.TextView
import com.emaginalabs.haveaniceday.core.model.Notification
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.view.View
import butterknife.BindView
import butterknife.ButterKnife
import com.emabinalabs.haveaniceday.R
import com.emaginalabs.haveaniceday.app.notification.HappyNotificationMessaging
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
        val happyNotification = intent?.getParcelableExtra<Notification>(HappyNotificationMessaging.RECEIVED_NOTIFICATION)

        textContainer.text = happyNotification?.message ?: "You're so beautiful today :)"
        ImageDownloader(happyNotification, imageContainer, imageLoader).execute()
    }

    inner class ImageDownloader(val notification: Notification?, val container: ImageView,
                                val loader: GifTextView) : AsyncTask<Void, Void, Bitmap?>() {
        override fun doInBackground(vararg params: Void?): Bitmap? {
            try {
                notification?.photoUrl?.let {
                    val imageStream = URL(notification.photoUrl).openStream()
                    val bitmap = BitmapFactory.decodeStream(imageStream)
                    return bitmap
                }
            } catch (e: Throwable) {
                // log error
            }
            return null
        }

        override fun onPostExecute(result: Bitmap??) {
            result.let {
                loader.visibility = View.GONE
                container.setImageBitmap(result)
                container.visibility = View.VISIBLE
            }
        }
    }
}
