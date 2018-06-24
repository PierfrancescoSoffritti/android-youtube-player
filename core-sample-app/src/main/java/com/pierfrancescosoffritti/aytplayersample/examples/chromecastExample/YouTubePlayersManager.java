package com.pierfrancescosoffritti.aytplayersample.examples.chromecastExample;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;

import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.chromecastsender.ChromecastYouTubePlayerContext;
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.chromecastsender.io.infrastructure.ChromecastConnectionListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.YouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.utils.YouTubePlayerStateTracker;
import com.pierfrancescosoffritti.aytplayersample.R;
import com.pierfrancescosoffritti.aytplayersample.examples.chromecastExample.ui.SimpleChromecastUIController;
import com.pierfrancescosoffritti.aytplayersample.utils.PlaybackUtils;

import javax.annotation.Nullable;

/**
 * Class used to manage the two YouTubePlayers, local and cast.
 * The local YouTubePlayer is supposed to stop playing when the cast player stars and vice versa.
 * When one of the two players stops, the other has to resume the playback from where the previous player stopped.
 */
public class YouTubePlayersManager implements ChromecastConnectionListener {

    private final YouTubePlayerView youtubePlayerView;
    private final YouTubePlayerListener chromecastPlayerListener;

    private final SimpleChromecastUIController chromecastUIController;

    @Nullable private YouTubePlayer localYouTubePlayer = null;
    @Nullable private YouTubePlayer chromecastYouTubePlayer = null;

    private final YouTubePlayerStateTracker chromecastPlayerStateTracker = new YouTubePlayerStateTracker();
    private final YouTubePlayerStateTracker localPlayerStateTracker = new YouTubePlayerStateTracker();

    private boolean playingOnCastPlayer = false;

    YouTubePlayersManager(LocalYouTubePlayerInitListener localYouTubePlayerInitListener,
                          YouTubePlayerView youtubePlayerView, View chromecastControls,
                          YouTubePlayerListener chromecastPlayerListener) {
        this.youtubePlayerView = youtubePlayerView;
        this.chromecastPlayerListener = chromecastPlayerListener;

        Button nextVideoButton = chromecastControls.findViewById(R.id.next_video_button);
        chromecastUIController = new SimpleChromecastUIController(chromecastControls);

        initLocalYouTube(localYouTubePlayerInitListener);
        nextVideoButton.setOnClickListener(view -> {
            if(chromecastYouTubePlayer != null)
                chromecastYouTubePlayer.loadVideo(PlaybackUtils.getNextVideoId(), 0f);
        });
    }

    @Override
    public void onChromecastConnecting() {
        if(localYouTubePlayer != null)
            localYouTubePlayer.pause();
    }

    @Override
    public void onChromecastConnected(@NonNull ChromecastYouTubePlayerContext chromecastYouTubePlayerContext) {
        initializeCastPlayer(chromecastYouTubePlayerContext);
        playingOnCastPlayer = true;
    }

    @Override
    public void onChromecastDisconnected() {
        if(localYouTubePlayer != null && chromecastPlayerStateTracker.getVideoId() != null) {
            if (chromecastPlayerStateTracker.getCurrentState() == PlayerConstants.PlayerState.PLAYING)
                localYouTubePlayer.loadVideo(chromecastPlayerStateTracker.getVideoId(), chromecastPlayerStateTracker.getCurrentSecond());
            else
                localYouTubePlayer.cueVideo(chromecastPlayerStateTracker.getVideoId(), chromecastPlayerStateTracker.getCurrentSecond());
        }

        chromecastUIController.resetUI();
        playingOnCastPlayer = false;
    }

    public SimpleChromecastUIController getChromecastUIController() {
        return chromecastUIController;
    }

    public void togglePlayback() {
        if(playingOnCastPlayer && chromecastYouTubePlayer != null)
            if(chromecastPlayerStateTracker.getCurrentState() == PlayerConstants.PlayerState.PLAYING)
                chromecastYouTubePlayer.pause();
            else
                chromecastYouTubePlayer.play();
        else if(localYouTubePlayer != null)
            if(localPlayerStateTracker.getCurrentState() == PlayerConstants.PlayerState.PLAYING)
                localYouTubePlayer.pause();
            else
                localYouTubePlayer.play();
    }

    private void initLocalYouTube(LocalYouTubePlayerInitListener localYouTubePlayerInitListener) {
        youtubePlayerView.initialize(youtubePlayer -> {
            this.localYouTubePlayer = youtubePlayer;
            youtubePlayer.addListener(localPlayerStateTracker);

            youtubePlayer.addListener(new AbstractYouTubePlayerListener() {
                public void onReady() {
                    if (!playingOnCastPlayer)
                        youtubePlayer.loadVideo(PlaybackUtils.getNextVideoId(), chromecastPlayerStateTracker.getCurrentSecond());

                    localYouTubePlayerInitListener.onLocalYouTubePlayerInit();
                }

                public void onCurrentSecond(float second){
                    if (playingOnCastPlayer && localPlayerStateTracker.getCurrentState() == PlayerConstants.PlayerState.PLAYING)
                        youtubePlayer.pause();
                }
            });
        }, true);
    }

    private void initializeCastPlayer(ChromecastYouTubePlayerContext chromecastYouTubePlayerContext) {
        chromecastYouTubePlayerContext.initialize( youtubePlayer -> {
            chromecastYouTubePlayer = youtubePlayer;

            chromecastUIController.setYouTubePlayer(youtubePlayer);

            youtubePlayer.addListener(chromecastPlayerListener);
            youtubePlayer.addListener(chromecastPlayerStateTracker);
            youtubePlayer.addListener(chromecastUIController);

            youtubePlayer.addListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady() {
                    if(localPlayerStateTracker.getVideoId() != null)
                        youtubePlayer.loadVideo(localPlayerStateTracker.getVideoId(), localPlayerStateTracker.getCurrentSecond());
                }
            });
        });
    }

    /**
     * Interface used to notify its listeners than the local YouTubePlayer is ready to play videos.
     */
    interface LocalYouTubePlayerInitListener {
        void onLocalYouTubePlayerInit();
    }
}
