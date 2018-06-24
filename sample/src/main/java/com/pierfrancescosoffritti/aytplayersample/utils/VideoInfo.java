package com.pierfrancescosoffritti.aytplayersample.utils;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import javax.annotation.Nullable;

public class VideoInfo {
    @NonNull private final String videoTitle;
    @NonNull private final String channelTitle;
    @Nullable private final Bitmap thumbnail;

    public VideoInfo(@NonNull String videoTitle, @NonNull String channelTitle, @Nullable Bitmap thumbnail) {
        this.videoTitle = videoTitle;
        this.channelTitle = channelTitle;
        this.thumbnail = thumbnail;
    }

    @NonNull
    public String getVideoTitle() {
        return videoTitle;
    }

    @NonNull
    public String getChannelTitle() {
        return channelTitle;
    }

    @Nullable
    public Bitmap getThumbnail() {
        return thumbnail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VideoInfo videoInfo = (VideoInfo) o;

        if (!videoTitle.equals(videoInfo.videoTitle)) return false;
        if (!channelTitle.equals(videoInfo.channelTitle)) return false;
        return thumbnail != null ? thumbnail.equals(videoInfo.thumbnail) : videoInfo.thumbnail == null;
    }

    @Override
    public int hashCode() {
        int result = videoTitle.hashCode();
        result = 31 * result + channelTitle.hashCode();
        result = 31 * result + (thumbnail != null ? thumbnail.hashCode() : 0);
        return result;
    }
}
