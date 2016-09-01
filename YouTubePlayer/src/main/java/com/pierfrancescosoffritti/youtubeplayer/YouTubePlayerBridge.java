package com.pierfrancescosoffritti.youtubeplayer;

import android.support.annotation.NonNull;
import android.webkit.JavascriptInterface;

import java.util.HashSet;
import java.util.Set;

/**
 * WEB TO APP bridge
 */
public class YouTubePlayerBridge {

    @NonNull private final YouTubePlayer youTubePlayer;

    public YouTubePlayerBridge(@NonNull YouTubePlayer youTubePlayer) {
        this.youTubePlayer = youTubePlayer;
    }

    @JavascriptInterface
    public void onReady() {
        for(YouTubePlayer.YouTubeListener listener : getListeners())
            listener.onReady(youTubePlayer);
    }

    @JavascriptInterface
    public void onStateChange(String state) {
        for(YouTubePlayer.YouTubeListener listener : getListeners())
            if ("UNSTARTED".equalsIgnoreCase(state))
                listener.onStateChange(YouTubePlayer.State.UNSTARTED, youTubePlayer);
            else if ("ENDED".equalsIgnoreCase(state))
                listener.onStateChange(YouTubePlayer.State.ENDED, youTubePlayer);
            else if ("PLAYING".equalsIgnoreCase(state))
                listener.onStateChange(YouTubePlayer.State.PLAYING, youTubePlayer);
            else if ("PAUSED".equalsIgnoreCase(state))
                listener.onStateChange(YouTubePlayer.State.PAUSED, youTubePlayer);
            else if ("BUFFERING".equalsIgnoreCase(state))
                listener.onStateChange(YouTubePlayer.State.BUFFERING, youTubePlayer);
            else if ("CUED".equalsIgnoreCase(state))
                listener.onStateChange(YouTubePlayer.State.VIDEO_CUED, youTubePlayer);
    }

    @JavascriptInterface
    public void onPlaybackQualityChange(String quality) {
        for(YouTubePlayer.YouTubeListener listener : getListeners())
            if ("small".equalsIgnoreCase(quality))
                listener.onPlaybackQualityChange(YouTubePlayer.PlaybackQuality.SMALL, youTubePlayer);
            else if ("medium".equalsIgnoreCase(quality))
                listener.onPlaybackQualityChange(YouTubePlayer.PlaybackQuality.MEDIUM, youTubePlayer);
            else if ("large".equalsIgnoreCase(quality))
                listener.onPlaybackQualityChange(YouTubePlayer.PlaybackQuality.LARGE, youTubePlayer);
            else if ("hd720".equalsIgnoreCase(quality))
                listener.onPlaybackQualityChange(YouTubePlayer.PlaybackQuality.HD720, youTubePlayer);
            else if ("hd1080".equalsIgnoreCase(quality))
                listener.onPlaybackQualityChange(YouTubePlayer.PlaybackQuality.HD1080, youTubePlayer);
            else if ("highres".equalsIgnoreCase(quality))
                listener.onPlaybackQualityChange(YouTubePlayer.PlaybackQuality.HIGH_RES, youTubePlayer);
            else if ("default".equalsIgnoreCase(quality))
                listener.onPlaybackQualityChange(YouTubePlayer.PlaybackQuality.DEFAULT, youTubePlayer);
    }

    /**
     *  The default playback rate is 1, which indicates that the video is playing at normal speed. Playback rates may include values like 0.25, 0.5, 1, 1.5, and 2.
     * @param rate 0.25, 0.5, 1, 1.5, 2
     */
    @JavascriptInterface
    public void onPlaybackRateChange(String rate) {
        double dRate = Double.parseDouble(rate);
        for(YouTubePlayer.YouTubeListener listener : getListeners())
            listener.onPlaybackRateChange(dRate, youTubePlayer);
    }

    @JavascriptInterface
    public void onError(String error) {
        for(YouTubePlayer.YouTubeListener listener : getListeners())
            listener.onError(error, youTubePlayer);
    }

    @JavascriptInterface
    public void onApiChange() {
        for(YouTubePlayer.YouTubeListener listener : getListeners())
            listener.onApiChange(youTubePlayer);
    }

    @JavascriptInterface
    public void currentSeconds(String seconds) {
        double dSeconds = Double.parseDouble(seconds);
        for(YouTubePlayer.YouTubeListener listener : getListeners())
            listener.onCurrentSecond(dSeconds, youTubePlayer);
    }

    @JavascriptInterface
    public void duration(String seconds) {
        double duration = Double.parseDouble(seconds);
        for(YouTubePlayer.YouTubeListener listener : getListeners())
            listener.onDuration(duration, youTubePlayer);
    }

    @JavascriptInterface
    public void onLog(String message) {
        for(YouTubePlayer.YouTubeListener listener : getListeners())
            listener.onLog(message, youTubePlayer);
    }

    private Set<YouTubePlayer.YouTubeListener> getListeners() {
        Set<YouTubePlayer.YouTubeListener> listeners = new HashSet<>();
        listeners.addAll(youTubePlayer.getYouTubeListeners());

        return listeners;
    }
}
