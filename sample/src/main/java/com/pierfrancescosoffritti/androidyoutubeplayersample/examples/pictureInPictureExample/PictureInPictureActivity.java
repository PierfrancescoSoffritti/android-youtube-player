package com.pierfrancescosoffritti.androidyoutubeplayersample.examples.pictureInPictureExample;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.pierfrancescosoffritti.androidyoutubeplayersample.R;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerView;

public class PictureInPictureActivity extends AppCompatActivity {

    YouTubePlayerView youTubePlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_in_picture_example);

        youTubePlayerView = findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youTubePlayerView);

        Button pipButton = new Button(this);

        pipButton.setOnClickListener( view -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                enterPictureInPictureMode();
            else
                Log.e(getClass().getSimpleName(), "can't access pip, SDK required to be >= N");
        });

        youTubePlayerView.getPlayerUIController().addView( pipButton );
    }

    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode, Configuration newConfig) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig);

        if(isInPictureInPictureMode)
            youTubePlayerView.enterFullScreen();
        else
            youTubePlayerView.exitFullScreen();
    }
}
