package com.pierfrancescosoffritti.androidyoutubeplayer.core.testActivity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.pierfrancescosoffritti.androidyoutubeplayer.R;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.helpers.MotionEventPrimitive;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import java.util.LinkedList;
import java.util.Queue;

public class TestActivity extends AppCompatActivity {

    public static boolean CLICK_WORKS = false;
    public static boolean LONG_CLICK_WORKS = false;
    public static Queue<MotionEventPrimitive> TOUCH_EVENT_QUEUE = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity);

        YouTubePlayerView youTubePlayerView = findViewById(R.id.youtube_player_view);

        youTubePlayerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CLICK_WORKS = true;
            }
        });

        youTubePlayerView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                LONG_CLICK_WORKS = true;
                return false;
            }
        });

        youTubePlayerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                TOUCH_EVENT_QUEUE.add(
                        new MotionEventPrimitive(
                                motionEvent.getX(),
                                motionEvent.getY(),
                                motionEvent.getAction()
                        )
                );
                return false;
            }
        });

        getLifecycle().addObserver(youTubePlayerView);
    }
}