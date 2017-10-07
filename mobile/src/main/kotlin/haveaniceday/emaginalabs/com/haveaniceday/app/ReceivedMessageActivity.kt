package haveaniceday.emaginalabs.com.haveaniceday.app

import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import android.widget.TextView
import haveaniceday.emaginalabs.com.haveaniceday.app.notification.HappyNotificationMessaging
import haveaniceday.emaginalabs.com.haveaniceday.core.model.Notification
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.view.View
import haveaniceday.emaginalabs.com.haveaniceday.R
import pl.droidsonroids.gif.GifTextView
import java.net.URL


class ReceivedMessageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_received_message)
        val happyNotification = intent?.getParcelableExtra<Notification>(HappyNotificationMessaging.RECEIVED_NOTIFICATION)

        val textContainer = findViewById(R.id.received_message) as TextView
        textContainer.text = happyNotification?.message ?: "You're so beautiful today :)"

        val imageContainer = findViewById(R.id.received_image) as ImageView
        val loader = findViewById(R.id.image_loader) as GifTextView
        ImageDownloader(happyNotification, imageContainer, loader).execute()
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
