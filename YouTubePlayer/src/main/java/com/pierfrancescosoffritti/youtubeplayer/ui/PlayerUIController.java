package com.pierfrancescosoffritti.youtubeplayer.ui;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

public interface PlayerUIController {
    void showUI(boolean show);

    void showVideoTitle(boolean show);
    void setVideoTitle(@NonNull String videoTitle);

    void enableLiveVideoIndicator(boolean enable);

    void setCustomAction1(@NonNull Drawable icon, @Nullable View.OnClickListener clickListener);
    void setCustomAction2(@NonNull Drawable icon, @Nullable View.OnClickListener clickListener);
    void showCustomAction1(boolean show);
    void showCustomAction2(boolean show);

    void showFullscreenButton(boolean show);
    void setCustomFullScreenButtonListener(@NonNull View.OnClickListener customFullScreenButtonListener);
}
