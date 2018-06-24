package com.pierfrancescosoffritti.aytplayersample.examples.chromecastExample;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.MediaRouteButton;
import android.view.View;

import com.google.android.gms.cast.framework.CastContext;
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.chromecastsender.ChromecastYouTubePlayerContext;
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.chromecastsender.io.infrastructure.ChromecastConnectionListener;
import com.pierfrancescosoffritti.aytplayersample.R;
import com.pierfrancescosoffritti.aytplayersample.examples.chromecastExample.utils.MediaRouteButtonUtils;
import com.pierfrancescosoffritti.aytplayersample.examples.chromecastExample.notifications.NotificationManager;
import com.pierfrancescosoffritti.aytplayersample.examples.chromecastExample.utils.PlayServicesUtils;
import com.pierfrancescosoffritti.aytplayersample.examples.chromecastExample.notifications.PlaybackControllerBroadcastReceiver;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerView;

/**
 * Example Activity used to showcase how to use the chromecast-youtube-library extension to cast videos to a Chromecast device.
 * See documentation here: <a href="https://github.com/PierfrancescoSoffritti/chromecast-youtube-player">chromecast-youtube-player</a>
 */
public class ChromecastExampleActivity extends AppCompatActivity implements YouTubePlayersManager.LocalYouTubePlayerInitListener, ChromecastConnectionListener {

    private int googlePlayServicesAvailabilityRequestCode = 1;

    private YouTubePlayersManager youTubePlayersManager;
    private MediaRouteButton mediaRouteButton;

    private NotificationManager notificationManager;
    private PlaybackControllerBroadcastReceiver playbackControllerBroadcastReceiver;

    private YouTubePlayerView youTubePlayerView;
    private View chromecastControlsRoot;

    private boolean connectedToChromecast = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chromecast_example);

        youTubePlayerView = findViewById(R.id.youtube_player_view);
        chromecastControlsRoot = findViewById(R.id.chromecast_controls_root);

        getLifecycle().addObserver(youTubePlayerView);

        notificationManager = new NotificationManager(this, ChromecastExampleActivity.class);

        youTubePlayersManager = new YouTubePlayersManager(this, youTubePlayerView, chromecastControlsRoot, notificationManager);
        mediaRouteButton = MediaRouteButtonUtils.initMediaRouteButton(this);

        registerBroadcastReceiver();

        // can't use CastContext until I'm sure the user has GooglePlayServices
        PlayServicesUtils.checkGooglePlayServicesAvailability(this, googlePlayServicesAvailabilityRequestCode, this::initChromecast);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getApplicationContext().unregisterReceiver(playbackControllerBroadcastReceiver);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // can't use CastContext until I'm sure the user has GooglePlayServices
        if(requestCode == googlePlayServicesAvailabilityRequestCode)
            PlayServicesUtils.checkGooglePlayServicesAvailability(this, googlePlayServicesAvailabilityRequestCode, this::initChromecast);
    }

    private void initChromecast() {
        new ChromecastYouTubePlayerContext(
                CastContext.getSharedInstance(this).getSessionManager(),
                this, playbackControllerBroadcastReceiver, youTubePlayersManager
        );
    }

    @Override
    public void onChromecastConnecting() {
    }

    @Override
    public void onChromecastConnected(ChromecastYouTubePlayerContext chromecastYouTubePlayerContext) {
        connectedToChromecast = true;

        updateUI(true);
        notificationManager.showNotification();
    }

    @Override
    public void onChromecastDisconnected() {
        connectedToChromecast = false;

        updateUI(false);
        notificationManager.dismissNotification();
    }

    @Override
    public void onLocalYouTubePlayerInit() {
        if(connectedToChromecast)
            return;

        MediaRouteButtonUtils.addMediaRouteButtonToPlayerUI(
                mediaRouteButton, android.R.color.white,
                null, localPlayerUIMediaRouteButtonContainer
        );
    }

    private void registerBroadcastReceiver() {
        playbackControllerBroadcastReceiver = new PlaybackControllerBroadcastReceiver(youTubePlayersManager::togglePlayback);
        IntentFilter filter = new IntentFilter(PlaybackControllerBroadcastReceiver.TOGGLE_PLAYBACK);
        filter.addAction(PlaybackControllerBroadcastReceiver.STOP_CAST_SESSION);
        getApplicationContext().registerReceiver(playbackControllerBroadcastReceiver, filter);
    }

    private void updateUI(boolean connected) {
        MediaRouteButtonContainer disabledContainer = connected ? localPlayerUIMediaRouteButtonContainer : chromecastPlayerUIMediaRouteButtonContainer;
        MediaRouteButtonContainer enabledContainer = connected ? chromecastPlayerUIMediaRouteButtonContainer : localPlayerUIMediaRouteButtonContainer;
        int mediaRouteButtonColor = connected ? android.R.color.black : android.R.color.white;

        // the media route button has a single instance.
        // therefore it has to be moved from the local YouTube player UI to the chromecast YouTube player UI, and vice versa.
        MediaRouteButtonUtils.addMediaRouteButtonToPlayerUI(
                mediaRouteButton, mediaRouteButtonColor,
                disabledContainer, enabledContainer
        );

        youTubePlayerView.setVisibility(connected ? View.GONE : View.VISIBLE);
        chromecastControlsRoot.setVisibility(connected ? View.VISIBLE : View.GONE);
    }

    private MediaRouteButtonContainer chromecastPlayerUIMediaRouteButtonContainer = new MediaRouteButtonContainer() {
        public void addMediaRouteButton(MediaRouteButton mediaRouteButton) { youTubePlayersManager.getChromecastUIController().addView(mediaRouteButton); }
        public void removeMediaRouteButton(MediaRouteButton mediaRouteButton) { youTubePlayersManager.getChromecastUIController().removeView(mediaRouteButton); }
    };

    private MediaRouteButtonContainer localPlayerUIMediaRouteButtonContainer = new MediaRouteButtonContainer() {
        public void addMediaRouteButton(MediaRouteButton mediaRouteButton) { youTubePlayerView.getPlayerUIController().addView(mediaRouteButton); }
        public void removeMediaRouteButton(MediaRouteButton mediaRouteButton) { youTubePlayerView.getPlayerUIController().removeView(mediaRouteButton); }
    };

    public interface MediaRouteButtonContainer {
        void addMediaRouteButton(MediaRouteButton mediaRouteButton);
        void removeMediaRouteButton(MediaRouteButton mediaRouteButton);
    }
}
