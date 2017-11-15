package com.pierfrancescosoffritti.youtubeplayer.ui.menu.defaultMenuHelper;

import android.content.Context;
import android.view.View;

import com.pierfrancescosoffritti.youtubeplayer.R;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayer;
import com.pierfrancescosoffritti.youtubeplayer.ui.menu.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class DefaultMenuItems {

    private final Context context;
    private final YouTubePlayer youTubePlayer;

    public DefaultMenuItems(Context context, YouTubePlayer youTubePlayer) {
        this.context = context;
        this.youTubePlayer = youTubePlayer;
    }

    public List<MenuItem> get() {
        List<MenuItem> menuItems = new ArrayList<>();

        MenuItem videoQuality = new MenuItem(context.getString(R.string.video_quality), R.drawable.ic_settings_24dp, new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        menuItems.add(videoQuality);

        return menuItems;
    }
}
