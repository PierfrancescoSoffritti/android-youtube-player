package com.pierfrancescosoffritti.youtubeplayer;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;

/**
 * WEB TO APP bridge
 */
public class YouTubePlayerBridge {

    @NonNull private final YouTubePlayer youTubePlayer;
    @NonNull private final Handler mainThreadHandler;

    public YouTubePlayerBridge(@NonNull YouTubePlayer youTubePlayer) {
        this.youTubePlayer = youTubePlayer;
        mainThreadHandler = new Handler(Looper.getMainLooper());
    }

    @JavascriptInterface
    public void onReady() {
        for(YouTubePlayer.YouTubeListener listener : youTubePlayer.getListeners())
            listener.onReady();
    }

    @JavascriptInterface
    public void onStateChange(final String state) {
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                for(YouTubePlayer.YouTubeListener listener : youTubePlayer.getListeners())
                    if ("UNSTARTED".equalsIgnoreCase(state))
                        listener.onStateChange(YouTubePlayer.State.UNSTARTED);
                    else if ("ENDED".equalsIgnoreCase(state))
                        listener.onStateChange(YouTubePlayer.State.ENDED);
                    else if ("PLAYING".equalsIgnoreCase(state))
                        listener.onStateChange(YouTubePlayer.State.PLAYING);
                    else if ("PAUSED".equalsIgnoreCase(state))
                        listener.onStateChange(YouTubePlayer.State.PAUSED);
                    else if ("BUFFERING".equalsIgnoreCase(state))
                        listener.onStateChange(YouTubePlayer.State.BUFFERING);
                    else if ("CUED".equalsIgnoreCase(state))
                        listener.onStateChange(YouTubePlayer.State.VIDEO_CUED);
            }
        });
    }

    @JavascriptInterface
    public void onPlaybackQualityChange(final String quality) {
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                for(YouTubePlayer.YouTubeListener listener : youTubePlayer.getListeners())
                    if ("small".equalsIgnoreCase(quality))
                        listener.onPlaybackQualityChange(YouTubePlayer.PlaybackQuality.SMALL);
                    else if ("medium".equalsIgnoreCase(quality))
                        listener.onPlaybackQualityChange(YouTubePlayer.PlaybackQuality.MEDIUM);
                    else if ("large".equalsIgnoreCase(quality))
                        listener.onPlaybackQualityChange(YouTubePlayer.PlaybackQuality.LARGE);
                    else if ("hd720".equalsIgnoreCase(quality))
                        listener.onPlaybackQualityChange(YouTubePlayer.PlaybackQuality.HD720);
                    else if ("hd1080".equalsIgnoreCase(quality))
                        listener.onPlaybackQualityChange(YouTubePlayer.PlaybackQuality.HD1080);
                    else if ("highres".equalsIgnoreCase(quality))
                        listener.onPlaybackQualityChange(YouTubePlayer.PlaybackQuality.HIGH_RES);
                    else if ("default".equalsIgnoreCase(quality))
                        listener.onPlaybackQualityChange(YouTubePlayer.PlaybackQuality.DEFAULT);
            }
        });
    }

    /**
     *  The default playback rate is 1, which indicates that the video is playing at normal speed. Playback rates may include values like 0.25, 0.5, 1, 1.5, and 2.
     *  <br/><br/>TODO add constants
     * @param rate 0.25, 0.5, 1, 1.5, 2
     */
    @JavascriptInterface
    public void onPlaybackRateChange(final String rate) {
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    double dRate = Double.parseDouble(rate);
                    for (YouTubePlayer.YouTubeListener listener : youTubePlayer.getListeners())
                        listener.onPlaybackRateChange(dRate);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @JavascriptInterface
    public void onError(final String error) {
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                for(YouTubePlayer.YouTubeListener listener : youTubePlayer.getListeners()) {
                    if ("2".equalsIgnoreCase(error))
                        listener.onError(YouTubePlayer.Error.INVALID_PARAMENTER_IN_REQUEST);
                    else if ("5".equalsIgnoreCase(error))
                        listener.onError(YouTubePlayer.Error.HTML_5_PLAYER);
                    else if ("100".equalsIgnoreCase(error))
                        listener.onError(YouTubePlayer.Error.VIDEO_NOT_FOUND);
                    else if ("101".equalsIgnoreCase(error))
                        listener.onError(YouTubePlayer.Error.VIDEO_NOT_PLAYABLE_IN_EMBEDDED_PLAYER);
                    else if ("150".equalsIgnoreCase(error))
                        listener.onError(YouTubePlayer.Error.VIDEO_NOT_PLAYABLE_IN_EMBEDDED_PLAYER);
                }
            }
        });
    }

    @JavascriptInterface
    public void onApiChange() {
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                for(YouTubePlayer.YouTubeListener listener : youTubePlayer.getListeners())
                    listener.onApiChange();
            }
        });
    }

    @JavascriptInterface
    public void currentSeconds(final String seconds) {
        final float fSeconds;
        try {
            fSeconds = Float.parseFloat(seconds);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return;
        }

        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                for(YouTubePlayer.YouTubeListener listener : youTubePlayer.getListeners())
                    listener.onCurrentSecond(fSeconds);
            }
        });
    }

    @JavascriptInterface
    public void onVideoTitle(final String videoTitle) {
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                for(YouTubePlayer.YouTubeListener listener : youTubePlayer.getListeners())
                    listener.onVideoTitle(videoTitle);
            }
        });
    }

    @JavascriptInterface
    public void onVideoId(final String videoId) {
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                for(YouTubePlayer.YouTubeListener listener : youTubePlayer.getListeners())
                    listener.onVideoId(videoId);
            }
        });
    }

    @JavascriptInterface
    public void onVideoDuration(final String seconds) {
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    String finalSeconds = TextUtils.isEmpty(seconds) ? "0" : seconds;
                    float videoDuration = Float.parseFloat(finalSeconds);
                    for (YouTubePlayer.YouTubeListener listener : youTubePlayer.getListeners())
                        listener.onVideoDuration(videoDuration);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @JavascriptInterface
    public void onLog(final String message) {
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                for(YouTubePlayer.YouTubeListener listener : youTubePlayer.getListeners())
                    listener.onLog(message);
            }
        });
    }
}
