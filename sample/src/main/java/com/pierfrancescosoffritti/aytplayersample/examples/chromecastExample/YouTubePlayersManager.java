package com.pierfrancescosoffritti.aytplayersample.examples.chromecastExample;

import android.view.View;
import android.widget.Button;

import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.chromecastsender.ChromecastYouTubePlayerContext;
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.chromecastsender.io.infrastructure.ChromecastConnectionListener;
import com.pierfrancescosoffritti.aytplayersample.R;
import com.pierfrancescosoffritti.youtubeplayer.player.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.youtubeplayer.player.PlayerConstants;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayer;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerListener;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerView;
import com.pierfrancescosoffritti.youtubeplayer.utils.YouTubePlayerStateTracker;

import javax.annotation.Nullable;

public class YouTubePlayersManager implements ChromecastConnectionListener {

    private final LocalYouTubePlayerInitListener localYouTubePlayerInitListener;
    private final YouTubePlayerView youtubePlayerView;
    private final View chromecastControls;
    private final YouTubePlayerListener chromecastPlayerListener;

    private final Button nextVideoButton;
    private final SimpleChromecastUIController chromecastUIController;

    @Nullable private YouTubePlayer localYouTubePlayer = null;
    @Nullable private YouTubePlayer chromecastYouTubePlayer = null;

    private final YouTubePlayerStateTracker chromecastPlayerStateTracker = new YouTubePlayerStateTracker();
    private final YouTubePlayerStateTracker localPlayerStateTracker = new YouTubePlayerStateTracker();

    private boolean playingOnCastPlayer = false;

    public YouTubePlayersManager(LocalYouTubePlayerInitListener localYouTubePlayerInitListener,
                                 YouTubePlayerView youtubePlayerView, View chromecastControls,
                                 YouTubePlayerListener chromecastPlayerListener) {
        this.localYouTubePlayerInitListener = localYouTubePlayerInitListener;
        this.youtubePlayerView = youtubePlayerView;
        this.chromecastControls = chromecastControls;
        this.chromecastPlayerListener = chromecastPlayerListener;

        nextVideoButton = chromecastControls.findViewById(R.id.next_video_button);
        chromecastUIController = new SimpleChromecastUIController(chromecastControls);

        initLocalYouTube(localYouTubePlayerInitListener);
        nextVideoButton.setOnClickListener( () -> chromecastYouTubePlayer.loadVideo(PlaybackUtils.getNextVideoId(), 0f));
    }

    @Override
    public void onChromecastConnecting() {
        localYouTubePlayer.pause();
    }

    @Override
    public void onChromecastConnected(ChromecastYouTubePlayerContext chromecastYouTubePlayerContext) {
        initializeCastPlayer(chromecastYouTubePlayerContext);

        playingOnCastPlayer = true;
    }

    @Override
    public void onChromecastDisconnected() {
        if(chromecastPlayerStateTracker.getCurrentState() == PlayerConstants.PlayerState.PLAYING)
            localYouTubePlayer.loadVideo(chromecastPlayerStateTracker.getVideoId(), chromecastPlayerStateTracker.getCurrentSecond());
        else
            localYouTubePlayer.cueVideo(chromecastPlayerStateTracker.getVideoId(), chromecastPlayerStateTracker.getCurrentSecond());

        chromecastUIController.resetUI();

        playingOnCastPlayer = false;
    }

    public void togglePlayback() {
        if(playingOnCastPlayer)
            if(chromecastPlayerStateTracker.getCurrentState() == PlayerConstants.PlayerState.PLAYING)
                chromecastYouTubePlayer.pause();
            else
                chromecastYouTubePlayer.play();
        else
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

                    localYouTubePlayerInitListener.onLocalYouTubePlayerInit()
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

            chromecastUIController.youTubePlayer = youtubePlayer;

            youtubePlayer.addListener(chromecastPlayerListener);
            youtubePlayer.addListener(chromecastPlayerStateTracker);
            youtubePlayer.addListener(chromecastUIController);

            youtubePlayer.addListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady() {
                    youtubePlayer.loadVideo(localPlayerStateTracker.getVideoId(), localPlayerStateTracker.getCurrentSecond());
                }
            });
        });
    }

    interface LocalYouTubePlayerInitListener {
        void onLocalYouTubePlayerInit();
    }
}
