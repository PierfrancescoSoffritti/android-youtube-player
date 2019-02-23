package com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.sampleapp.utils

import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.util.Log
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL


object NetworkUtils {
    fun getBitmapFromURL(src: String): Bitmap? {
        return try {
            val url = URL(src)
            val connection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            BitmapFactory.decodeStream(connection.inputStream)
        } catch (e: IOException) {
            Log.e(javaClass.simpleName, "can't download bitmap")
            null
        }
    }
}