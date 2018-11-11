package com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners;

import androidx.annotation.NonNull;

import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayer;

public interface YouTubePlayerInitListener {
    void onInitSuccess(@NonNull YouTubePlayer youTubePlayer);
}
