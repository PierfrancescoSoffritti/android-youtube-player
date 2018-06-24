package com.pierfrancescosoffritti.aytplayersample.examples.chromecastExample.utils;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.arch.lifecycle.LifecycleObserver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.graphics.Palette;
import android.util.Log;

import com.pierfrancescosoffritti.aytplayersample.R;
import com.pierfrancescosoffritti.aytplayersample.utils.VideoInfo;
import com.pierfrancescosoffritti.aytplayersample.utils.YouTubeDataEndpoint;
import com.pierfrancescosoffritti.youtubeplayer.player.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.youtubeplayer.player.PlayerConstants;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class NotificationManager extends AbstractYouTubePlayerListener implements LifecycleObserver {

    private Context context;
    private Class notificationHostActivity;

    private int notificationId = 1;
    private String channelId = "CHANNEL_ID";

    private NotificationCompat.Builder notificationBuilder;

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

    private  NotificationCompat.Builder initNotificationBuilder() {
        Intent openActivityExplicitIntent = new Intent(context.getApplicationContext(), notificationHostActivity);
        PendingIntent openActivityPendingIntent = PendingIntent.getActivity(context.getApplicationContext(), 0, openActivityExplicitIntent, 0);

        Intent togglePlaybackImplicitIntent = new Intent(PlaybackControllerBroadcastReceiver.TOGGLE_PLAYBACK);
        PendingIntent togglePlaybackPendingIntent = PendingIntent.getBroadcast(context, 0, togglePlaybackImplicitIntent, 0);

        Intent stopCastSessionImplicitIntent = new Intent(PlaybackControllerBroadcastReceiver.STOP_CAST_SESSION);
        PendingIntent stopCastSessionPendingIntent = PendingIntent.getBroadcast(context, 0, stopCastSessionImplicitIntent, 0);

        return new NotificationCompat.Builder(context, channelId)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSmallIcon(R.drawable.ic_cast_connected_24dp)
                .setContentIntent(openActivityPendingIntent)
                .setOngoing(true)
                .addAction(R.drawable.ic_play_arrow_24dp, "Toggle Playback", togglePlaybackPendingIntent)
                .addAction(R.drawable.ic_cast_connected_24dp, "Disconnect from chromecast", stopCastSessionPendingIntent)
                .setStyle(new android.support.v4.media.app.NotificationCompat.MediaStyle().setShowActionsInCompactView(0, 1));
    }

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
    public void onStateChange(int state) {
        switch (state) {
            case PlayerConstants.PlayerState.PLAYING:
                notificationBuilder.mActions.get(0).icon = R.drawable.ic_pause_24dp;
                break;

            default:
                notificationBuilder.mActions.get(0).icon = R.drawable.ic_play_arrow_24dp;
                break;
        }

        showNotification();
    }

    @SuppressLint("CheckResult")
    @Override
    public void onVideoId(@NonNull String videoId) {
        Single<VideoInfo> observable = YouTubeDataEndpoint.getVideoInfoFromYouTubeDataAPIs(videoId);

        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        videoInfo -> {
                            notificationBuilder.setContentTitle(videoInfo.getVideoTitle());
                            notificationBuilder.setContentText(videoInfo.getChannelTitle());
                            notificationBuilder.setLargeIcon(videoInfo.getThumbnail());

                            int color = Palette.from(videoInfo.getThumbnail()).generate().getDominantColor(ContextCompat.getColor(context, android.R.color.black));
                            notificationBuilder.setColor(color);
                            showNotification();
                        },
                        error -> Log.e(getClass().getSimpleName(), "Can't retrieve video title, are you connected to the internet?")
                );
    }
}
