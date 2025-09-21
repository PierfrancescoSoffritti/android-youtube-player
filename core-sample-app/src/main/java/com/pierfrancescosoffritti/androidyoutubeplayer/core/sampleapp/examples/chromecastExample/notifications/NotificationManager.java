package com.pierfrancescosoffritti.androidyoutubeplayer.core.sampleapp.examples.chromecastExample.notifications;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.LifecycleObserver;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.aytplayersample.R;

public class NotificationManager extends AbstractYouTubePlayerListener implements LifecycleObserver {

  private final Context context;
  private final Class notificationHostActivity;

  private final int notificationId = 1;
  private final String channelId = "CHANNEL_ID";

  private final NotificationCompat.Builder notificationBuilder;

  public NotificationManager(Context context, Class notificationHostActivity) {
    this.context = context;
    this.notificationHostActivity = notificationHostActivity;

    initNotificationChannel();
    notificationBuilder = initNotificationBuilder();
  }

  private void initNotificationChannel() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      NotificationChannel channel = new NotificationChannel(channelId, "chromecast-youtube-player", android.app.NotificationManager.IMPORTANCE_DEFAULT);
      channel.setDescription("sample-app");
      android.app.NotificationManager notificationManager = context.getSystemService(android.app.NotificationManager.class);
      if (notificationManager != null)
        notificationManager.createNotificationChannel(channel);
      else
        Log.e(getClass().getSimpleName(), "Can't create notification channel");
    }
  }

  private NotificationCompat.Builder initNotificationBuilder() {
    Intent openActivityExplicitIntent = new Intent(context.getApplicationContext(), notificationHostActivity);
    Intent togglePlaybackImplicitIntent = new Intent(PlaybackControllerBroadcastReceiver.TOGGLE_PLAYBACK);
    Intent stopCastSessionImplicitIntent = new Intent(PlaybackControllerBroadcastReceiver.STOP_CAST_SESSION);

    int flags = 0;
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
      flags = PendingIntent.FLAG_IMMUTABLE;
    }

    PendingIntent openActivityPendingIntent = PendingIntent.getActivity(context.getApplicationContext(), 0, openActivityExplicitIntent, flags);
    PendingIntent togglePlaybackPendingIntent = PendingIntent.getBroadcast(context, 0, togglePlaybackImplicitIntent, flags);
    PendingIntent stopCastSessionPendingIntent = PendingIntent.getBroadcast(context, 0, stopCastSessionImplicitIntent, flags);

    return new NotificationCompat.Builder(context, channelId)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setSmallIcon(R.drawable.ic_cast_connected_24dp)
            .setContentIntent(openActivityPendingIntent)
            .setOngoing(true)
            .addAction(R.drawable.ic_play_arrow_24dp, "Toggle Playback", togglePlaybackPendingIntent)
            .addAction(R.drawable.ic_cast_connected_24dp, "Disconnect from chromecast", stopCastSessionPendingIntent)
            .setStyle(new androidx.media.app.NotificationCompat.MediaStyle().setShowActionsInCompactView(0, 1));
  }

  // TODO: fix
  @SuppressLint("MissingPermission")
  public void showNotification() {
    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
    notificationManager.notify(notificationId, notificationBuilder.build());
  }

  public void dismissNotification() {
    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
    notificationManager.cancel(notificationId);
  }

  @SuppressLint("SwitchIntDef")
  @Override
  public void onStateChange(@NonNull YouTubePlayer youTubePlayer, @NonNull PlayerConstants.PlayerState state) {
    switch (state) {
      case PLAYING:
        // TODO: fix
        // notificationBuilder.mActions.get(0).icon = R.drawable.ic_pause_24dp;
        break;

      default:
        // TODO: fix
        // notificationBuilder.mActions.get(0).icon = R.drawable.ic_play_arrow_24dp;
        break;
    }

    showNotification();
  }

  @SuppressLint("CheckResult")
  @Override
  public void onVideoId(@NonNull YouTubePlayer youTubePlayer, @NonNull String videoId) {
    notificationBuilder.setContentTitle(videoId);
    showNotification();
  }
}
