package com.pierfrancescosoffritti.youtubeplayersample;

import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.pierfrancescosoffritti.youtubeplayer.AbstractYouTubeListener;
import com.pierfrancescosoffritti.youtubeplayer.YouTubePlayerFullScreenListener;
import com.pierfrancescosoffritti.youtubeplayer.YouTubePlayerView;

public class MainActivity extends AppCompatActivity {

    private YouTubePlayerView youTubePlayerView;
    private FullScreenManager fullScreenManager;

    private LinearLayout root;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getWindow().setStatusBarColor(ContextCompat.getColor(this, android.R.color.transparent));

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        root = (LinearLayout) findViewById(R.id.root);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fullScreenManager = new FullScreenManager(this, toolbar);

        youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtube_player_view);

        findViewById(R.id.next_video_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                youTubePlayerView.loadVideo("LvetJ9U_tVY", 0);
            }
        });

        youTubePlayerView.initialize(new AbstractYouTubeListener() {

            @Override
            public void onReady() {
                youTubePlayerView.loadVideo("6JYIGclVQdw", 0);
            }

        }, true);


        youTubePlayerView.addFullScreenListener(new YouTubePlayerFullScreenListener() {
            @Override
            public void onYouTubePlayerEnterFullScreen() {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                fullScreenManager.enterFullScreen();

                youTubePlayerView.setCustomActionRight(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_pause_36dp), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        youTubePlayerView.pauseVideo();
                    }
                });

                drawerLayout.setFitsSystemWindows(false);
                drawerLayout.setStatusBarBackgroundColor(ContextCompat.getColor(MainActivity.this, android.R.color.transparent));

                DrawerLayout.LayoutParams rootParams = (DrawerLayout.LayoutParams) root.getLayoutParams();
                rootParams.setMargins(0,0,0,0);
                root.setLayoutParams(rootParams);
            }

            @Override
            public void onYouTubePlayerExitFullScreen() {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                fullScreenManager.exitFullScreen();

                youTubePlayerView.setCustomActionRight(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_pause_36dp), null);

                drawerLayout.setFitsSystemWindows(true);
                drawerLayout.setStatusBarBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimaryDark));
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        youTubePlayerView.release();
    }
}
