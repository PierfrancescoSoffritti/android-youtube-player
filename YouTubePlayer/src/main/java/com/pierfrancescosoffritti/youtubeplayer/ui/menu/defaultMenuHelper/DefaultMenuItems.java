package com.pierfrancescosoffritti.youtubeplayer.ui.menu.defaultMenuHelper;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.pierfrancescosoffritti.youtubeplayer.R;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayer;
import com.pierfrancescosoffritti.youtubeplayer.ui.menu.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class DefaultMenuItems {

    private final Context context;
    private final YouTubePlayer youTubePlayer;
    private PopupWindow popupWindow;

    public DefaultMenuItems(Context context, YouTubePlayer youTubePlayer) {
        this.context = context;
        this.youTubePlayer = youTubePlayer;
    }

    List<MenuItem> get() {
        List<MenuItem> menuItems = new ArrayList<>();

        MenuItem videoQuality = new MenuItem(context.getString(R.string.video_quality), R.drawable.ic_settings_24dp, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout root = popupWindow.getContentView().findViewById(R.id.menu_root);
                root.removeAllViews();

                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                if(inflater == null)
                    throw new RuntimeException("can't access LAYOUT_INFLATER_SERVICE");

                View videoQuality = inflater.inflate(R.layout.video_quality, null);

                TextView textView = new TextView(context);
                textView.setBackgroundColor(ContextCompat.getColor(context, R.color.red));
                textView.setText("hello");
                root.addView(videoQuality);
            }
        });

        menuItems.add(videoQuality);

        return menuItems;
    }

    void setPopupWindow(PopupWindow popupWindow) {
        this.popupWindow = popupWindow;
    }
}
