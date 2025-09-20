package com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.sampleapp.notifications

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.media.app.NotificationCompat.MediaStyle
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.cyplayersample.R


class NotificationManager(
  private val context: Context,
  private val notificationHostActivity: Class<*>
) : AbstractYouTubePlayerListener() {
  private val notificationId = 1
  private val channelId = "CHANNEL_ID"

  private val notificationBuilder: NotificationCompat.Builder

  init {
    initNotificationChannel()
    notificationBuilder = initNotificationBuilder()
  }

  private fun initNotificationChannel() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      val channel = NotificationChannel(
        channelId,
        "chromecast-youtube-player",
        NotificationManager.IMPORTANCE_DEFAULT
      )
      channel.description = "sample-app"
      val notificationManager = context.getSystemService(NotificationManager::class.java)
      notificationManager.createNotificationChannel(channel)
    }
  }

  private fun initNotificationBuilder(): NotificationCompat.Builder {
    var flags = 0
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      flags = PendingIntent.FLAG_IMMUTABLE
    }

    val openActivityExplicitIntent = Intent(context.applicationContext, notificationHostActivity)
    val openActivityPendingIntent =
      PendingIntent.getActivity(context.applicationContext, 0, openActivityExplicitIntent, flags)

    val togglePlaybackImplicitIntent = Intent(PlaybackControllerBroadcastReceiver.TOGGLE_PLAYBACK)
    val togglePlaybackPendingIntent =
      PendingIntent.getBroadcast(context, 0, togglePlaybackImplicitIntent, flags)

    val stopCastSessionImplicitIntent =
      Intent(PlaybackControllerBroadcastReceiver.STOP_CAST_SESSION)
    val stopCastSessionPendingIntent =
      PendingIntent.getBroadcast(context, 0, stopCastSessionImplicitIntent, flags)

    return NotificationCompat.Builder(context, channelId)
      .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
      .setSmallIcon(R.drawable.ic_cast_connected_24dp)
      .setContentIntent(openActivityPendingIntent)
      .setOngoing(true)
      .addAction(R.drawable.ic_play_arrow_24dp, "Toggle Playback", togglePlaybackPendingIntent)
      .addAction(
        R.drawable.ic_cast_connected_24dp,
        "Disconnect from chromecast",
        stopCastSessionPendingIntent
      )
      .setStyle(MediaStyle().setShowActionsInCompactView(0, 1))
  }

  // TODO: fix
  @SuppressLint("NotificationPermission", "MissingPermission")
  fun showNotification() {
    val notificationManager = NotificationManagerCompat.from(context)
    notificationManager.notify(notificationId, notificationBuilder.build())
  }

  fun dismissNotification() {
    val notificationManager = NotificationManagerCompat.from(context)
    notificationManager.cancel(notificationId)
  }

  @SuppressLint("SwitchIntDef")
  override fun onStateChange(youTubePlayer: YouTubePlayer, state: PlayerConstants.PlayerState) {
    // TODO: fix
//    when (state) {
//      PlayerConstants.PlayerState.PLAYING -> notificationBuilder.mActions[0].icon = R.drawable.ic_pause_24dp
//      else -> notificationBuilder.mActions[0].icon = R.drawable.ic_play_arrow_24dp
//    }

    showNotification()
  }
}