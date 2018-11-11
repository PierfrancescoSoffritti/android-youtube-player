package com.pierfrancescosoffritti.aytplayersample.examples.pictureInPictureExample;

import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;

import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.aytplayersample.R;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Lifecycle;

public class PictureInPictureActivity extends AppCompatActivity {

    private YouTubePlayerView youTubePlayerView;
    private String[] videoIds = {"6JYIGclVQdw"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_in_picture_example);

        initYouTubePlayerView();
    }

    private void initYouTubePlayerView() {
        youTubePlayerView = findViewById(R.id.youtube_player_view);
        youTubePlayerView.getPlayerUIController().showFullscreenButton(false);

        getLifecycle().addObserver(youTubePlayerView);
        initPictureInPicture(youTubePlayerView);

        youTubePlayerView.initialize(youTubePlayer -> {
            youTubePlayer.addListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady() {
                    loadVideo(youTubePlayer, videoIds[0]);
                }
            });
        }, true);
    }

    private void initPictureInPicture(YouTubePlayerView youTubePlayerView) {
        ImageView pictureInPictureView = new ImageView(this);
        pictureInPictureView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_picture_in_picture_24dp));

        pictureInPictureView.setOnClickListener( view -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                boolean supportsPIP = getPackageManager().hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE);
                if(supportsPIP)
                    enterPictureInPictureMode();
            } else {
                new AlertDialog.Builder(this)
                        .setTitle("Can't enter picture in picture mode")
                        .setMessage("In order to enter picture in picture mode you need a SDK version >= N.")
                        .show();
            }
        });

        youTubePlayerView.getPlayerUIController().addView( pictureInPictureView );
    }

    private void loadVideo(YouTubePlayer youTubePlayer, String videoId) {
        if(getLifecycle().getCurrentState() == Lifecycle.State.RESUMED)
            youTubePlayer.loadVideo(videoId, 0);
        else
            youTubePlayer.cueVideo(videoId, 0);
    }

    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode, Configuration newConfig) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig);

        if(isInPictureInPictureMode) {
            youTubePlayerView.enterFullScreen();
            youTubePlayerView.getPlayerUIController().showUI(false);
        } else {
            youTubePlayerView.exitFullScreen();
            youTubePlayerView.getPlayerUIController().showUI(true);
        }
    }
}
