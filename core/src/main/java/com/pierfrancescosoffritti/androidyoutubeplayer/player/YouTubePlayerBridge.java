package com.pierfrancescosoffritti.androidyoutubeplayer.player;

import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;

import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.YouTubePlayerListener;

import java.util.Collection;


/**
 * Bridge used for Javascript-Java communication.
 */
public class YouTubePlayerBridge {

    // these constant values correspond to the values in the Javascript player

    private static final String STATE_UNSTARTED = "UNSTARTED";
    private static final String STATE_ENDED = "ENDED";
    private static final String STATE_PLAYING = "PLAYING";
    private static final String STATE_PAUSED = "PAUSED";
    private static final String STATE_BUFFERING = "BUFFERING";
    private static final String STATE_CUED = "CUED";

    private static final String QUALITY_SMALL = "small";
    private static final String QUALITY_MEDIUM = "medium";
    private static final String QUALITY_LARGE = "large";
    private static final String QUALITY_HD720 = "hd720";
    private static final String QUALITY_HD1080 = "hd1080";
    private static final String QUALITY_HIGH_RES = "highres";
    private static final String QUALITY_DEFAULT = "default";

    private static final String RATE_0_25 = "0.25";
    private static final String RATE_0_5 = "0.5";
    private static final String RATE_1 = "1";
    private static final String RATE_1_5 = "1.5";
    private static final String RATE_2 = "2";

    private static final String ERROR_INVALID_PARAMETER_IN_REQUEST = "2";
    private static final String ERROR_HTML_5_PLAYER = "5";
    private static final String ERROR_VIDEO_NOT_FOUND = "100";
    private static final String ERROR_VIDEO_NOT_PLAYABLE_IN_EMBEDDED_PLAYER1 = "101";
    private static final String ERROR_VIDEO_NOT_PLAYABLE_IN_EMBEDDED_PLAYER2 = "150";

    @NonNull private final YouTubePlayerBridgeCallbacks youTubePlayer;
    @NonNull private final Handler mainThreadHandler;

    public interface YouTubePlayerBridgeCallbacks {
        void onYouTubeIframeAPIReady();
        Collection<YouTubePlayerListener> getListeners();
    }

    public YouTubePlayerBridge(@NonNull YouTubePlayerBridgeCallbacks youTubePlayer) {
        this.youTubePlayer = youTubePlayer;
        mainThreadHandler = new Handler(Looper.getMainLooper());
    }

    @JavascriptInterface
    public void sendYouTubeIframeAPIReady() {
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                youTubePlayer.onYouTubeIframeAPIReady();
            }
        });
    }

    @JavascriptInterface
    public void sendReady() {
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                for (YouTubePlayerListener listener : youTubePlayer.getListeners())
                    listener.onReady();
            }
        });
    }

    @JavascriptInterface
    public void sendStateChange(final String state) {

        final PlayerConstants.PlayerState playerState = parsePlayerState(state);

        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                for(YouTubePlayerListener listener : youTubePlayer.getListeners())
                    listener.onStateChange(playerState);
            }
        });
    }

    @JavascriptInterface
    public void sendPlaybackQualityChange(final String quality) {

        final PlayerConstants.PlaybackQuality playbackQuality = parsePlaybackQuality(quality);

        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                for(YouTubePlayerListener listener : youTubePlayer.getListeners())
                    listener.onPlaybackQualityChange(playbackQuality);
            }
        });
    }

    @JavascriptInterface
    public void sendPlaybackRateChange(final String rate) {

        final PlayerConstants.PlaybackRate playbackRate = parsePlaybackRate(rate);

        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                for (YouTubePlayerListener listener : youTubePlayer.getListeners())
                    listener.onPlaybackRateChange(playbackRate);
            }
        });
    }

    @JavascriptInterface
    public void sendError(final String error) {

        final PlayerConstants.PlayerError playerError = parsePlayerError(error);

        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                for(YouTubePlayerListener listener : youTubePlayer.getListeners())
                    listener.onError(playerError);
            }
        });
    }

    @JavascriptInterface
    public void sendApiChange() {
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                for(YouTubePlayerListener listener : youTubePlayer.getListeners())
                    listener.onApiChange();
            }
        });
    }

    @JavascriptInterface
    public void sendVideoCurrentTime(final String seconds) {
        final float currentTimeSeconds;
        try {
            currentTimeSeconds = Float.parseFloat(seconds);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return;
        }

        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                for(YouTubePlayerListener listener : youTubePlayer.getListeners())
                    listener.onCurrentSecond(currentTimeSeconds);
            }
        });
    }

    @JavascriptInterface
    public void sendVideoDuration(final String seconds) {
        final float videoDuration;
        try {
            String finalSeconds = TextUtils.isEmpty(seconds) ? "0" : seconds;
            videoDuration = Float.parseFloat(finalSeconds);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return;
        }

        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                for (YouTubePlayerListener listener : youTubePlayer.getListeners())
                    listener.onVideoDuration(videoDuration);
            }
        });
    }

    @JavascriptInterface
    public void sendVideoLoadedFraction(final String fraction) {
        final float loadedFraction;
        try {
            loadedFraction = Float.parseFloat(fraction);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return;
        }

        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                for(YouTubePlayerListener listener : youTubePlayer.getListeners())
                    listener.onVideoLoadedFraction(loadedFraction);
            }
        });
    }

    @JavascriptInterface
    public void sendVideoId(final String videoId) {
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                for(YouTubePlayerListener listener : youTubePlayer.getListeners())
                    listener.onVideoId(videoId);
            }
        });
    }

    @NonNull
    private PlayerConstants.PlayerState parsePlayerState(String state) {
        PlayerConstants.PlayerState playerState;

        if (state.equalsIgnoreCase(STATE_UNSTARTED))
            playerState = PlayerConstants.PlayerState.UNSTARTED;
        else if (state.equalsIgnoreCase(STATE_ENDED))
            playerState = PlayerConstants.PlayerState.ENDED;
        else if (state.equalsIgnoreCase(STATE_PLAYING))
            playerState = PlayerConstants.PlayerState.PLAYING;
        else if (state.equalsIgnoreCase(STATE_PAUSED))
            playerState = PlayerConstants.PlayerState.PAUSED;
        else if (state.equalsIgnoreCase(STATE_BUFFERING))
            playerState = PlayerConstants.PlayerState.BUFFERING;
        else if (state.equalsIgnoreCase(STATE_CUED))
            playerState = PlayerConstants.PlayerState.VIDEO_CUED;
        else
            playerState = PlayerConstants.PlayerState.UNKNOWN;

        return playerState;
    }


    @NonNull
    private PlayerConstants.PlaybackQuality parsePlaybackQuality(String quality) {
        PlayerConstants.PlaybackQuality playbackQuality;

        if (quality.equalsIgnoreCase(QUALITY_SMALL))
            playbackQuality = PlayerConstants.PlaybackQuality.SMALL;
        else if (quality.equalsIgnoreCase(QUALITY_MEDIUM))
            playbackQuality = PlayerConstants.PlaybackQuality.MEDIUM;
        else if (quality.equalsIgnoreCase(QUALITY_LARGE))
            playbackQuality = PlayerConstants.PlaybackQuality.LARGE;
        else if (quality.equalsIgnoreCase(QUALITY_HD720))
            playbackQuality = PlayerConstants.PlaybackQuality.HD720;
        else if (quality.equalsIgnoreCase(QUALITY_HD1080))
            playbackQuality = PlayerConstants.PlaybackQuality.HD1080;
        else if (quality.equalsIgnoreCase(QUALITY_HIGH_RES))
            playbackQuality = PlayerConstants.PlaybackQuality.HIGH_RES;
        else if (quality.equalsIgnoreCase(QUALITY_DEFAULT))
            playbackQuality = PlayerConstants.PlaybackQuality.DEFAULT;
        else
            playbackQuality = PlayerConstants.PlaybackQuality.UNKNOWN;

        return playbackQuality;
    }

    @NonNull
    private PlayerConstants.PlaybackRate parsePlaybackRate(String rate) {
        PlayerConstants.PlaybackRate playbackRate;

        if (rate.equalsIgnoreCase(RATE_0_25))
            playbackRate = PlayerConstants.PlaybackRate.RATE_0_25;
        else if (rate.equalsIgnoreCase(RATE_0_5))
            playbackRate = PlayerConstants.PlaybackRate.RATE_0_5;
        else if (rate.equalsIgnoreCase(RATE_1))
            playbackRate = PlayerConstants.PlaybackRate.RATE_1;
        else if (rate.equalsIgnoreCase(RATE_1_5))
            playbackRate = PlayerConstants.PlaybackRate.RATE_1_5;
        else if (rate.equalsIgnoreCase(RATE_2))
            playbackRate = PlayerConstants.PlaybackRate.RATE_2;
        else
            playbackRate = PlayerConstants.PlaybackRate.UNKNOWN;

        return playbackRate;
    }

    @NonNull
    private PlayerConstants.PlayerError parsePlayerError(String error) {
        PlayerConstants.PlayerError playerError;

        if (error.equalsIgnoreCase(ERROR_INVALID_PARAMETER_IN_REQUEST))
            playerError = PlayerConstants.PlayerError.INVALID_PARAMETER_IN_REQUEST;
        else if (error.equalsIgnoreCase(ERROR_HTML_5_PLAYER))
            playerError = PlayerConstants.PlayerError.HTML_5_PLAYER;
        else if (error.equalsIgnoreCase(ERROR_VIDEO_NOT_FOUND))
            playerError = PlayerConstants.PlayerError.VIDEO_NOT_FOUND;
        else if (error.equalsIgnoreCase(ERROR_VIDEO_NOT_PLAYABLE_IN_EMBEDDED_PLAYER1))
            playerError = PlayerConstants.PlayerError.VIDEO_NOT_PLAYABLE_IN_EMBEDDED_PLAYER;
        else if (error.equalsIgnoreCase(ERROR_VIDEO_NOT_PLAYABLE_IN_EMBEDDED_PLAYER2))
            playerError = PlayerConstants.PlayerError.VIDEO_NOT_PLAYABLE_IN_EMBEDDED_PLAYER;
        else
            playerError = PlayerConstants.PlayerError.UNKNOWN;

        return playerError;
    }
}
