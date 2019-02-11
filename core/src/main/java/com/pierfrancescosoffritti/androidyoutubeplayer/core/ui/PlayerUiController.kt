package com.pierfrancescosoffritti.androidyoutubeplayer.core.ui

import android.graphics.drawable.Drawable
import android.view.View

import com.pierfrancescosoffritti.androidyoutubeplayer.core.ui.menu.YouTubePlayerMenu


interface PlayerUiController {
    fun showUi(show: Boolean): PlayerUiController
    fun showPlayPauseButton(show: Boolean): PlayerUiController

    fun showVideoTitle(show: Boolean): PlayerUiController
    fun setVideoTitle(videoTitle: String): PlayerUiController

    fun enableLiveVideoUi(enable: Boolean): PlayerUiController

    /**
     * Set custom action to the left of the Play/Pause button
     */
    fun setCustomAction1(icon: Drawable, clickListener: View.OnClickListener?): PlayerUiController
    /**
     * Set custom action to the right of the Play/Pause button
     */
    fun setCustomAction2(icon: Drawable, clickListener: View.OnClickListener?): PlayerUiController
    fun showCustomAction1(show: Boolean): PlayerUiController
    fun showCustomAction2(show: Boolean): PlayerUiController

    fun showFullscreenButton(show: Boolean): PlayerUiController
    fun setFullScreenButtonClickListener(customFullScreenButtonClickListener: View.OnClickListener): PlayerUiController

    fun showMenuButton(show: Boolean): PlayerUiController
    fun setMenuButtonClickListener(customMenuButtonClickListener: View.OnClickListener): PlayerUiController

    fun showCurrentTime(show: Boolean): PlayerUiController
    fun showDuration(show: Boolean): PlayerUiController

    fun showSeekBar(show: Boolean): PlayerUiController
    fun showBufferingProgress(show: Boolean): PlayerUiController

    fun showYouTubeButton(show: Boolean): PlayerUiController

    /**
     * Adds a View to the top of the player
     * @param view View to be added
     */
    fun addView(view: View): PlayerUiController

    /**
     * Removes a View added with [PlayerUiController.addView]
     * @param view View to be removed
     */
    fun removeView(view: View): PlayerUiController

    fun getMenu(): YouTubePlayerMenu?
}
