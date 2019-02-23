package com.pierfrancescosoffritti.androidyoutubeplayer.core.sampleapp.examples.chromecastExample;

import android.view.View;
import android.widget.Button;

import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.chromecastsender.ChromecastYouTubePlayerContext;
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.chromecastsender.io.infrastructure.ChromecastConnectionListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerUtils;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerTracker;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.sampleapp.examples.chromecastExample.ui.SimpleChromeCastUiController;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.sampleapp.utils.VideoIdsProvider;
import com.pierfrancescosoffritti.aytplayersample.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;


/**
 * Class used to manage the two YouTubePlayers, local and cast.
 * The local YouTubePlayer is supposed to stop playing when the cast player stars and vice versa.
 * When one of the two players stops, the other has to resume the playback from where the previous player stopped.
 */
public class YouTubePlayersManager implements ChromecastConnectionListener {

    private final YouTubePlayerView youtubePlayerView;
    private final YouTubePlayerListener chromecastPlayerListener;

    private final SimpleChromeCastUiController chromecastUiController;

    @Nullable private YouTubePlayer localYouTubePlayer = null;
    @Nullable private YouTubePlayer chromecastYouTubePlayer = null;

    private final YouTubePlayerTracker chromecastPlayerStateTracker = new YouTubePlayerTracker();
    private final YouTubePlayerTracker localPlayerStateTracker = new YouTubePlayerTracker();

    private final Lifecycle lifeCycle;

    private boolean playingOnCastPlayer = false;

    YouTubePlayersManager(LocalYouTubePlayerInitListener localYouTubePlayerInitListener,
                          YouTubePlayerView youtubePlayerView, View chromecastControls,
                          YouTubePlayerListener chromecastPlayerListener, Lifecycle lifeCycle) {
        this.youtubePlayerView = youtubePlayerView;
        this.chromecastPlayerListener = chromecastPlayerListener;

        this.lifeCycle = lifeCycle;

        Button nextVideoButton = chromecastControls.findViewById(R.id.next_video_button);
        chromecastUiController = new SimpleChromeCastUiController(chromecastControls);

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
                YouTubePlayerUtils.loadOrCueVideo(
                        localYouTubePlayer, lifeCycle,
                        chromecastPlayerStateTracker.getVideoId(),
                        chromecastPlayerStateTracker.getCurrentSecond()
                );
            else
                localYouTubePlayer.cueVideo(chromecastPlayerStateTracker.getVideoId(), chromecastPlayerStateTracker.getCurrentSecond());
        }

        chromecastUiController.resetUi();
        playingOnCastPlayer = false;
    }

    public SimpleChromeCastUiController getChromecastUiController() {
        return chromecastUiController;
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
        youtubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                localYouTubePlayer = youTubePlayer;
                youTubePlayer.addListener(localPlayerStateTracker);

                if (!playingOnCastPlayer)
                    YouTubePlayerUtils.loadOrCueVideo(
                            youTubePlayer, lifeCycle,
                            VideoIdsProvider.getNextVideoId(),chromecastPlayerStateTracker.getCurrentSecond()
                    );

                localYouTubePlayerInitListener.onLocalYouTubePlayerInit();
            }

            public void onCurrentSecond(@NonNull YouTubePlayer youTubePlayer, float second){
                if (playingOnCastPlayer && localPlayerStateTracker.getState() == PlayerConstants.PlayerState.PLAYING)
                    youTubePlayer.pause();
            }
        });
    }

    private void initializeCastPlayer(ChromecastYouTubePlayerContext chromecastYouTubePlayerContext) {
        chromecastYouTubePlayerContext.initialize(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                chromecastYouTubePlayer = youTubePlayer;

                chromecastUiController.setYouTubePlayer(youTubePlayer);

                youTubePlayer.addListener(chromecastPlayerListener);
                youTubePlayer.addListener(chromecastPlayerStateTracker);
                youTubePlayer.addListener(chromecastUiController);

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
