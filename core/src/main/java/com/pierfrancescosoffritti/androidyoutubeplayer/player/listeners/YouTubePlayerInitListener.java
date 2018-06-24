package com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners;

import android.support.annotation.NonNull;

import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayer;

public interface YouTubePlayerInitListener {
    void onInitSuccess(@NonNull YouTubePlayer youTubePlayer);
}
