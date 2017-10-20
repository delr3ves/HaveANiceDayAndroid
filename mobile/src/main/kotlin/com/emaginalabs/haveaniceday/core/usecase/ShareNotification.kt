package com.emaginalabs.haveaniceday.core.usecase

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment.DIRECTORY_PICTURES
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.emabinalabs.haveaniceday.R
import com.emaginalabs.haveaniceday.core.model.Notification
import org.jetbrains.anko.runOnUiThread
import java.io.File
import java.io.FileOutputStream


class ShareNotification(private val context: Context) {

    fun execute(notification: Notification): Notification {
        context.runOnUiThread {
            Glide.with(context)
                    .asBitmap()
                    .load(notification.photoUrl)
                    .into(object : SimpleTarget<Bitmap>() {
                        override fun onResourceReady(image: Bitmap?, transition: Transition<in Bitmap>?) {
                            val sharingIntent = Intent(Intent.ACTION_SEND)
                            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, notification?.title)
                            val message = "${context.getString(R.string.app_name)} ;) - ${notification?.message}"
                            sharingIntent.putExtra(Intent.EXTRA_TEXT, message)
                            if (image != null) {
                                val file = File(getExternalFilesDir(DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png")
                                val out = FileOutputStream(file)
                                image?.compress(Bitmap.CompressFormat.PNG, 90, out)
                                out.close()
                                sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file))
                                sharingIntent.type = "image/png"
                            } else {
                                sharingIntent.type = "plain/text"
                            }
                            context.startActivity(Intent.createChooser(sharingIntent, context.getString(R.string.share_choose_title)))
                        }
                    })
        }

        return notification

    }
}
