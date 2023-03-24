package com.pierfrancescosoffritti.androidyoutubeplayer.core.customui.menu

import android.view.View

interface YouTubePlayerMenu {
  val itemCount: Int
  fun show(anchorView: View)
  fun dismiss()

  fun addItem(menuItem: MenuItem): YouTubePlayerMenu
  fun removeItem(itemIndex: Int): YouTubePlayerMenu
  fun removeItem(menuItem: MenuItem): YouTubePlayerMenu
}
