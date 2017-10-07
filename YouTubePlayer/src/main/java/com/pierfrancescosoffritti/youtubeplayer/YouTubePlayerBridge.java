package com.pierfrancescosoffritti.youtubeplayer;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;

/**
 * Bridge used to communicate from Javascript to Java.
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

    @NonNull private final YouTubePlayer youTubePlayer;
    @NonNull private final Handler mainThreadHandler;

    public YouTubePlayerBridge(@NonNull YouTubePlayer youTubePlayer) {
        this.youTubePlayer = youTubePlayer;
        mainThreadHandler = new Handler(Looper.getMainLooper());
    }

    @JavascriptInterface
    public void sendReady() {
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                for (YouTubePlayer.YouTubePlayerListener listener : youTubePlayer.getListeners())
                    listener.onReady();
            }
        });
    }

    @JavascriptInterface
    public void sendStateChange(final String state) {

        @YouTubePlayer.PlayerState.State final int playerState = parsePlayerState(state);

        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                for(YouTubePlayer.YouTubePlayerListener listener : youTubePlayer.getListeners())
                    listener.onStateChange(playerState);
            }
        });
    }

    @JavascriptInterface
    public void sendPlaybackQualityChange(final String quality) {

        @YouTubePlayer.PlaybackQuality.Quality final int playbackQuality = parsePlaybackQuality(quality);

        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                for(YouTubePlayer.YouTubePlayerListener listener : youTubePlayer.getListeners())
                    listener.onPlaybackQualityChange(playbackQuality);
            }
        });
    }

    @JavascriptInterface
    public void sendPlaybackRateChange(final String rate) {

        @YouTubePlayer.PlaybackRate.Rate final String playbackRate = parsePlaybackRate(rate);

        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                for (YouTubePlayer.YouTubePlayerListener listener : youTubePlayer.getListeners())
                    listener.onPlaybackRateChange(playbackRate);
            }
        });
    }

    @JavascriptInterface
    public void sendError(final String error) {

        @YouTubePlayer.PlayerError.Error final int playerError = parsePlayerError(error);

        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                for(YouTubePlayer.YouTubePlayerListener listener : youTubePlayer.getListeners())
                    listener.onError(playerError);
            }
        });
    }

    @JavascriptInterface
    public void sendApiChange() {
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                for(YouTubePlayer.YouTubePlayerListener listener : youTubePlayer.getListeners())
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
                for(YouTubePlayer.YouTubePlayerListener listener : youTubePlayer.getListeners())
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
                for (YouTubePlayer.YouTubePlayerListener listener : youTubePlayer.getListeners())
                    listener.onVideoDuration(videoDuration);
            }
        });
    }

    @JavascriptInterface
    public void sendVideoTitle(final String videoTitle) {
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                for(YouTubePlayer.YouTubePlayerListener listener : youTubePlayer.getListeners())
                    listener.onVideoTitle(videoTitle);
            }
        });
    }

    @JavascriptInterface
    public void sendVideoId(final String videoId) {
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                for(YouTubePlayer.YouTubePlayerListener listener : youTubePlayer.getListeners())
                    listener.onVideoId(videoId);
            }
        });
    }

    @JavascriptInterface
    public void sendMessage(final String message) {
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                for(YouTubePlayer.YouTubePlayerListener listener : youTubePlayer.getListeners())
                    listener.onMessage(message);
            }
        });
    }

    private int parsePlayerState(String state) {
        @YouTubePlayer.PlayerState.State int playerState;

        if (state.equalsIgnoreCase(STATE_UNSTARTED))
            playerState = YouTubePlayer.PlayerState.UNSTARTED;
        else if (state.equalsIgnoreCase(STATE_ENDED))
            playerState = YouTubePlayer.PlayerState.ENDED;
        else if (state.equalsIgnoreCase(STATE_PLAYING))
            playerState =  YouTubePlayer.PlayerState.PLAYING;
        else if (state.equalsIgnoreCase(STATE_PAUSED))
            playerState = YouTubePlayer.PlayerState.PAUSED;
        else if (state.equalsIgnoreCase(STATE_BUFFERING))
            playerState = YouTubePlayer.PlayerState.BUFFERING;
        else if (state.equalsIgnoreCase(STATE_CUED))
            playerState = YouTubePlayer.PlayerState.VIDEO_CUED;
        else
            playerState = YouTubePlayer.PlayerState.UNKNOWN;

        return playerState;
    }


    @YouTubePlayer.PlaybackQuality.Quality
    private int parsePlaybackQuality(String quality) {
        @YouTubePlayer.PlaybackQuality.Quality int playbackQuality;

        if (quality.equalsIgnoreCase(QUALITY_SMALL))
            playbackQuality = YouTubePlayer.PlaybackQuality.SMALL;
        else if (quality.equalsIgnoreCase(QUALITY_MEDIUM))
            playbackQuality = YouTubePlayer.PlaybackQuality.MEDIUM;
        else if (quality.equalsIgnoreCase(QUALITY_LARGE))
            playbackQuality = YouTubePlayer.PlaybackQuality.LARGE;
        else if (quality.equalsIgnoreCase(QUALITY_HD720))
            playbackQuality = YouTubePlayer.PlaybackQuality.HD720;
        else if (quality.equalsIgnoreCase(QUALITY_HD1080))
            playbackQuality = YouTubePlayer.PlaybackQuality.HD1080;
        else if (quality.equalsIgnoreCase(QUALITY_HIGH_RES))
            playbackQuality = YouTubePlayer.PlaybackQuality.HIGH_RES;
        else if (quality.equalsIgnoreCase(QUALITY_DEFAULT))
            playbackQuality = YouTubePlayer.PlaybackQuality.DEFAULT;
        else
            playbackQuality = YouTubePlayer.PlaybackQuality.UNKNOWN;

        return playbackQuality;
    }

    @YouTubePlayer.PlaybackRate.Rate
    private String parsePlaybackRate(String rate) {
        @YouTubePlayer.PlaybackRate.Rate final String playbackRate;

        if (rate.equalsIgnoreCase(RATE_0_25))
            playbackRate = YouTubePlayer.PlaybackRate.RATE_0_25;
        else if (rate.equalsIgnoreCase(RATE_0_5))
            playbackRate = YouTubePlayer.PlaybackRate.RATE_0_5;
        else if (rate.equalsIgnoreCase(RATE_1))
            playbackRate =  YouTubePlayer.PlaybackRate.RATE_1;
        else if (rate.equalsIgnoreCase(RATE_1_5))
            playbackRate = YouTubePlayer.PlaybackRate.RATE_1_5;
        else if (rate.equalsIgnoreCase(RATE_2))
            playbackRate = YouTubePlayer.PlaybackRate.RATE_2;
        else
            playbackRate = YouTubePlayer.PlaybackRate.UNKNOWN;

        return playbackRate;
    }

    @YouTubePlayer.PlayerError.Error
    private int parsePlayerError(String error) {
        @YouTubePlayer.PlayerError.Error int playerError;

        if (error.equalsIgnoreCase(ERROR_INVALID_PARAMETER_IN_REQUEST))
            playerError = YouTubePlayer.PlayerError.INVALID_PARAMETER_IN_REQUEST;
        else if (error.equalsIgnoreCase(ERROR_HTML_5_PLAYER))
            playerError = YouTubePlayer.PlayerError.HTML_5_PLAYER;
        else if (error.equalsIgnoreCase(ERROR_VIDEO_NOT_FOUND))
            playerError = YouTubePlayer.PlayerError.VIDEO_NOT_FOUND;
        else if (error.equalsIgnoreCase(ERROR_VIDEO_NOT_PLAYABLE_IN_EMBEDDED_PLAYER1))
            playerError = YouTubePlayer.PlayerError.VIDEO_NOT_PLAYABLE_IN_EMBEDDED_PLAYER;
        else if (error.equalsIgnoreCase(ERROR_VIDEO_NOT_PLAYABLE_IN_EMBEDDED_PLAYER2))
            playerError = YouTubePlayer.PlayerError.VIDEO_NOT_PLAYABLE_IN_EMBEDDED_PLAYER;
        else
            playerError = YouTubePlayer.PlayerError.UNKNOWN;

        return playerError;
    }
}
