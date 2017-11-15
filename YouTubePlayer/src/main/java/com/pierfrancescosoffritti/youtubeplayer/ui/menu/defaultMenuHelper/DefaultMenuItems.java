package com.pierfrancescosoffritti.youtubeplayer.ui.menu.defaultMenuHelper;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.pierfrancescosoffritti.youtubeplayer.R;
import com.pierfrancescosoffritti.youtubeplayer.player.PlayerConstants;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayer;
import com.pierfrancescosoffritti.youtubeplayer.ui.menu.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class DefaultMenuItems  {

    private final Context context;
    private final YouTubePlayer youTubePlayer;
    private PopupWindow popupWindow;

    public DefaultMenuItems(Context context, YouTubePlayer youTubePlayer) {
        this.context = context;
        this.youTubePlayer = youTubePlayer;
    }

    List<MenuItem> get() {
        List<MenuItem> menuItems = new ArrayList<>();

        MenuItem videoQuality = new MenuItem(context.getString(R.string.playback_quality), R.drawable.ic_settings_24dp, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout root = popupWindow.getContentView().findViewById(R.id.menu_root);
                root.removeAllViews();

                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                if(inflater == null)
                    throw new RuntimeException("can't access LAYOUT_INFLATER_SERVICE");

                View playbackQuality = inflater.inflate(R.layout.playback_quality, null);

                RadioButton radioQualityDefault = playbackQuality.findViewById(R.id.quality_default);
                RadioButton radioQualitySmall = playbackQuality.findViewById(R.id.quality_small);

                RadioGroup radioGroup = playbackQuality.findViewById(R.id.playback_quality_radio_group);
                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                        Log.d(getClass().getSimpleName(), "default clicked");
                        youTubePlayer.setPlaybackQuality(PlayerConstants.PlaybackQuality.SMALL);
                    }
                });

                root.addView(playbackQuality);
            }
        });

        menuItems.add(videoQuality);

        return menuItems;
    }

    void setPopupWindow(PopupWindow popupWindow) {
        this.popupWindow = popupWindow;
    }
}
