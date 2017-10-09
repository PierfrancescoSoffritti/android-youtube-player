package com.pierfrancescosoffritti.youtubeplayer.ui;

import android.graphics.drawable.Drawable;
import android.view.View;

public interface PlayerUIController {
    void showTitle(boolean show);
    void showUI(boolean show);
    void setCustomAction1(Drawable icon, View.OnClickListener clickListener);
    void setCustomAction2(Drawable icon, View.OnClickListener clickListener);
    void showCustomAction1(boolean show);
    void showCustomAction2(boolean show);
    void showFullscreenButton(boolean show);
    void setCustomFullScreenButtonListener(View.OnClickListener customFullScreenButtonListener);
}
