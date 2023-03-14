package com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners

import android.view.View

/**
 * Interface used to keep track of full screen events
 */
interface FullScreenListener {
  /**
   * Notify the host application that the player has entered full screen mode
   * (the full screen button in the player UI has been clicked).
   * After this call, the video will no longer be rendered in the [YouTubePlayerView],
   * but will instead be rendered in [fullScreenView].
   * The host application should add this View to a container that fills the screen
   * in order to actually display the video full screen.
   *
   * The application can explicitly exit fullscreen mode by invoking [exitFullScreen]
   * (for example when the user presses the back button).
   * However, the player will show its own UI to exist fullscreen.
   * Regardless of how the player exits fullscreen mode, [onEnterFullScreen] will be invoked,
   * signaling for the application to remove the custom View.
   */
  fun onEnterFullScreen(fullScreenView: View, exitFullScreen: () -> Unit)

  /**
   * Notify the host application that the player has exited full screen mode.
   * The host application must hide the custom View (the View which was previously passed to
   * [onEnterFullScreen]). After this call, the video will render in the player again.
   */
  fun onExitFullScreen()
}