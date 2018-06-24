package com.pierfrancescosoffritti.youtubeplayer.player.listeners;

import android.support.annotation.NonNull;

import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayer;

public interface YouTubePlayerInitListener {
    void onInitSuccess(@NonNull YouTubePlayer youTubePlayer);
}
