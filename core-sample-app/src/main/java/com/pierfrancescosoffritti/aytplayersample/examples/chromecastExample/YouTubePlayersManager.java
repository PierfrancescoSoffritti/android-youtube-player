package com.pierfrancescosoffritti.aytplayersample.examples.chromecastExample;

import android.view.View;
import android.widget.Button;

import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.chromecastsender.ChromecastYouTubePlayerContext;
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.chromecastsender.io.infrastructure.ChromecastConnectionListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.views.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.YouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.utils.YouTubePlayerTracker;
import com.pierfrancescosoffritti.aytplayersample.R;
import com.pierfrancescosoffritti.aytplayersample.examples.chromecastExample.ui.SimpleChromeCastUIController;
import com.pierfrancescosoffritti.aytplayersample.utils.VideoIdsProvider;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


/**
 * Class used to manage the two YouTubePlayers, local and cast.
 * The local YouTubePlayer is supposed to stop playing when the cast player stars and vice versa.
 * When one of the two players stops, the other has to resume the playback from where the previous player stopped.
 */
public class YouTubePlayersManager implements ChromecastConnectionListener {

    private final YouTubePlayerView youtubePlayerView;
    private final YouTubePlayerListener chromecastPlayerListener;

    private final SimpleChromeCastUIController chromecastUIController;

    @Nullable private YouTubePlayer localYouTubePlayer = null;
    @Nullable private YouTubePlayer chromecastYouTubePlayer = null;

    private final YouTubePlayerTracker chromecastPlayerStateTracker = new YouTubePlayerTracker();
    private final YouTubePlayerTracker localPlayerStateTracker = new YouTubePlayerTracker();

    private boolean playingOnCastPlayer = false;

    YouTubePlayersManager(LocalYouTubePlayerInitListener localYouTubePlayerInitListener,
                          YouTubePlayerView youtubePlayerView, View chromecastControls,
                          YouTubePlayerListener chromecastPlayerListener) {
        this.youtubePlayerView = youtubePlayerView;
        this.chromecastPlayerListener = chromecastPlayerListener;

        Button nextVideoButton = chromecastControls.findViewById(R.id.next_video_button);
        chromecastUIController = new SimpleChromeCastUIController(chromecastControls);

        initLocalYouTube(localYouTubePlayerInitListener);
        nextVideoButton.setOnClickListener(view -> {
            if(chromecastYouTubePlayer != null)
                chromecastYouTubePlayer.loadVideo(VideoIdsProvider.getNextVideoId(), 0f);
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
            if (chromecastPlayerStateTracker.getState() == PlayerConstants.PlayerState.PLAYING)
                localYouTubePlayer.loadVideo(chromecastPlayerStateTracker.getVideoId(), chromecastPlayerStateTracker.getCurrentSecond());
            else
                localYouTubePlayer.cueVideo(chromecastPlayerStateTracker.getVideoId(), chromecastPlayerStateTracker.getCurrentSecond());
        }

        chromecastUIController.resetUI();
        playingOnCastPlayer = false;
    }

    public SimpleChromeCastUIController getChromecastUIController() {
        return chromecastUIController;
    }

    public void togglePlayback() {
        if(playingOnCastPlayer && chromecastYouTubePlayer != null)
            if(chromecastPlayerStateTracker.getState() == PlayerConstants.PlayerState.PLAYING)
                chromecastYouTubePlayer.pause();
            else
                chromecastYouTubePlayer.play();
        else if(localYouTubePlayer != null)
            if(localPlayerStateTracker.getState() == PlayerConstants.PlayerState.PLAYING)
                localYouTubePlayer.pause();
            else
                localYouTubePlayer.play();
    }

    private void initLocalYouTube(LocalYouTubePlayerInitListener localYouTubePlayerInitListener) {
        youtubePlayerView.initialize(new AbstractYouTubePlayerListener() {
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                localYouTubePlayer = youTubePlayer;
                youTubePlayer.addListener(localPlayerStateTracker);

                if (!playingOnCastPlayer)
                    youTubePlayer.loadVideo(VideoIdsProvider.getNextVideoId(), chromecastPlayerStateTracker.getCurrentSecond());

                localYouTubePlayerInitListener.onLocalYouTubePlayerInit();
            }

            public void onCurrentSecond(@NonNull YouTubePlayer youTubePlayer, float second){
                if (playingOnCastPlayer && localPlayerStateTracker.getState() == PlayerConstants.PlayerState.PLAYING)
                    youTubePlayer.pause();
            }
        }, true);
    }

    private void initializeCastPlayer(ChromecastYouTubePlayerContext chromecastYouTubePlayerContext) {
        chromecastYouTubePlayerContext.initialize(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                chromecastYouTubePlayer = youTubePlayer;

                chromecastUIController.setYouTubePlayer(youTubePlayer);

                youTubePlayer.addListener(chromecastPlayerListener);
                youTubePlayer.addListener(chromecastPlayerStateTracker);
                youTubePlayer.addListener(chromecastUIController);

                if(localPlayerStateTracker.getVideoId() != null)
                    youTubePlayer.loadVideo(localPlayerStateTracker.getVideoId(), localPlayerStateTracker.getCurrentSecond());
            }
        });
    }

    /**
     * Interface used to notify its listeners than the local YouTubePlayer is ready to play videos.
     */
    interface LocalYouTubePlayerInitListener {
        void onLocalYouTubePlayerInit();
    }
}
