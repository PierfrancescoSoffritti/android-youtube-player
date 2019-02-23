package com.pierfrancescosoffritti.androidyoutubeplayer.core.sampleapp.examples.chromecastExample.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import android.util.Log;

import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.chromecastsender.ChromecastYouTubePlayerContext;
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.chromecastsender.io.infrastructure.ChromecastConnectionListener;

import java.util.Objects;

/**
 * This broadcast receiver is used to react to notification actions.
 */
public class PlaybackControllerBroadcastReceiver extends BroadcastReceiver implements ChromecastConnectionListener {

    public static final String TOGGLE_PLAYBACK = "com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.example.TOGGLE_PLAYBACK";
    public static final String STOP_CAST_SESSION = "com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.example.STOP_CAST_SESSION";

    private ChromecastYouTubePlayerContext chromecastYouTubePlayerContext;
    private final Runnable togglePlayback;

    public PlaybackControllerBroadcastReceiver(Runnable togglePlayback) {
        this.togglePlayback = togglePlayback;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(getClass().getSimpleName(), "intent received: " +intent.getAction());

        switch (Objects.requireNonNull(intent.getAction())) {
            case TOGGLE_PLAYBACK:
                togglePlayback.run();
                break;

            case STOP_CAST_SESSION:
                chromecastYouTubePlayerContext.endCurrentSession();
                break;
        }
    }

    @Override
    public void onChromecastConnected(@NonNull ChromecastYouTubePlayerContext chromecastYouTubePlayerContext) {
        this.chromecastYouTubePlayerContext = chromecastYouTubePlayerContext;
    }

    @Override
    public void onChromecastConnecting() {

    }

    @Override
    public void onChromecastDisconnected() {

    }
}
