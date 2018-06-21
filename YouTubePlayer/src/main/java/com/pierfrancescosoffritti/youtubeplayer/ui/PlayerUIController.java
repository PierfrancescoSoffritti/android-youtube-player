package com.pierfrancescosoffritti.youtubeplayer.ui;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.pierfrancescosoffritti.youtubeplayer.ui.menu.YouTubePlayerMenu;

public interface PlayerUIController {
    void showUI(boolean show);
    void showPlayPauseButton(boolean show);

    void showVideoTitle(boolean show);
    void setVideoTitle(@NonNull String videoTitle);

    void enableLiveVideoUI(boolean enable);
    void enableLiveVODUI(boolean enable);

    void setCustomAction1(@NonNull Drawable icon, @Nullable View.OnClickListener clickListener);
    void setCustomAction2(@NonNull Drawable icon, @Nullable View.OnClickListener clickListener);
    void showCustomAction1(boolean show);
    void showCustomAction2(boolean show);

    void showFullscreenButton(boolean show);
    void setCustomFullScreenButtonClickListener(@NonNull View.OnClickListener customFullScreenButtonClickListener);

    void showMenuButton(boolean show);
    void setCustomMenuButtonClickListener(@NonNull View.OnClickListener customMenuButtonClickListener);

    void showCurrentTime(boolean show);
    void showDuration(boolean show);

    void showSeekBar(boolean show);
    void showBufferingProgress(boolean show);

    void showYouTubeButton(boolean show);

    /**
     * Adds a View to the top of the player
     * @param view View to be added
     */
    void addView(@NonNull View view);

    /**
     * Removes a View added with {@link #addView addView}
     * @param view View to be removed
     */
    void removeView(@NonNull View view);

    @Nullable YouTubePlayerMenu getMenu();
    void setMenu(@NonNull YouTubePlayerMenu youTubePlayerMenu);
}
