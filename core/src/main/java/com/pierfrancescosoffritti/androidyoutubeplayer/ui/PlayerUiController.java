package com.pierfrancescosoffritti.androidyoutubeplayer.ui;

import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;

import com.pierfrancescosoffritti.androidyoutubeplayer.ui.menu.YouTubePlayerMenu;


public interface PlayerUiController {
    PlayerUiController showUi(boolean show);
    PlayerUiController showPlayPauseButton(boolean show);

    PlayerUiController showVideoTitle(boolean show);
    PlayerUiController setVideoTitle(@NonNull String videoTitle);

    PlayerUiController enableLiveVideoUi(boolean enable);

    PlayerUiController setCustomAction1(@NonNull Drawable icon, @Nullable View.OnClickListener clickListener);
    PlayerUiController setCustomAction2(@NonNull Drawable icon, @Nullable View.OnClickListener clickListener);
    PlayerUiController showCustomAction1(boolean show);
    PlayerUiController showCustomAction2(boolean show);

    PlayerUiController showFullscreenButton(boolean show);
    PlayerUiController setFullScreenButtonClickListener(@NonNull View.OnClickListener customFullScreenButtonClickListener);

    PlayerUiController showMenuButton(boolean show);
    PlayerUiController setMenuButtonClickListener(@NonNull View.OnClickListener customMenuButtonClickListener);

    PlayerUiController showCurrentTime(boolean show);
    PlayerUiController showDuration(boolean show);

    PlayerUiController showSeekBar(boolean show);
    PlayerUiController showBufferingProgress(boolean show);

    PlayerUiController showYouTubeButton(boolean show);

    /**
     * Adds a View to the top of the player
     * @param view View to be added
     */
    PlayerUiController addView(@NonNull View view);

    /**
     * Removes a View added with {@link #addView addView}
     * @param view View to be removed
     */
    PlayerUiController removeView(@NonNull View view);

    @Nullable YouTubePlayerMenu getMenu();
    PlayerUiController setMenu(@NonNull YouTubePlayerMenu youTubePlayerMenu);
}
