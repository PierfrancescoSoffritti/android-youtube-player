package com.pierfrancescosoffritti.youtubeplayer.ui.menu;

import android.view.View;

public interface YouTubePlayerMenu {
    void showMenu(View anchorView);
    void addItem(MenuItem menuItem);
    void removeItem(int itemIndex);
    int getItemCount();
}
