package com.pierfrancescosoffritti.youtubeplayer.player;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class InternalYouTubePlayerListener implements YouTubePlayerListener {

    private List<YouTubePlayerListener> youTubePlayerListeners;

    InternalYouTubePlayerListener(YouTubePlayerListener ...args) {
        youTubePlayerListeners = new ArrayList<>();

        for (YouTubePlayerListener tubePlayerListener : args)
            addListener(tubePlayerListener);
    }

    boolean addListener(@NonNull YouTubePlayerListener listener) {
        return youTubePlayerListeners.add(listener);
    }

    boolean removeListener(@NonNull YouTubePlayerListener listener) {
        return youTubePlayerListeners.remove(listener);
    }

    @Override
    public void onReady() {
        for(int i = 0; i< youTubePlayerListeners.size(); i++)
            youTubePlayerListeners.get(i).onReady();
    }

    @Override
    public void onStateChange(int state) {
        for(int i = 0; i< youTubePlayerListeners.size(); i++)
            youTubePlayerListeners.get(i).onStateChange(state);
    }

    @Override
    public void onPlaybackQualityChange(String playbackQuality) {
        for(int i = 0; i< youTubePlayerListeners.size(); i++)
            youTubePlayerListeners.get(i).onPlaybackQualityChange(playbackQuality);
    }

    @Override
    public void onPlaybackRateChange(String playbackRate) {
        for(int i = 0; i< youTubePlayerListeners.size(); i++)
            youTubePlayerListeners.get(i).onPlaybackRateChange(playbackRate);
    }

    @Override
    public void onError(int error) {
        for(int i = 0; i< youTubePlayerListeners.size(); i++)
            youTubePlayerListeners.get(i).onError(error);
    }

    @Override
    public void onApiChange() {
        for(int i = 0; i< youTubePlayerListeners.size(); i++)
            youTubePlayerListeners.get(i).onApiChange();
    }

    @Override
    public void onCurrentSecond(float second) {
        for(int i = 0; i< youTubePlayerListeners.size(); i++)
            youTubePlayerListeners.get(i).onCurrentSecond(second);
    }

    @Override
    public void onVideoDuration(float duration) {
        for(int i = 0; i< youTubePlayerListeners.size(); i++)
            youTubePlayerListeners.get(i).onVideoDuration(duration);
    }

    @Override
    public void onMessage(String message) {
        for(int i = 0; i< youTubePlayerListeners.size(); i++)
            youTubePlayerListeners.get(i).onMessage(message);
    }

    @Override
    public void onVideoId(String videoId) {
        for(int i = 0; i< youTubePlayerListeners.size(); i++)
            youTubePlayerListeners.get(i).onVideoId(videoId);
    }
}
