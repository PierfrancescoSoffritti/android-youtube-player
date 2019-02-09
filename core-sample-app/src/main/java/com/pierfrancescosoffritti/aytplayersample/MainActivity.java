package com.pierfrancescosoffritti.aytplayersample;

import android.content.Intent;
import android.os.Bundle;

import com.pierfrancescosoffritti.aytplayersample.examples.basicExample.BasicExampleActivity;
import com.pierfrancescosoffritti.aytplayersample.examples.chromecastExample.ChromeCastExampleActivity;
import com.pierfrancescosoffritti.aytplayersample.examples.customUIExample.CustomUIActivity;
import com.pierfrancescosoffritti.aytplayersample.examples.fragmentExample.FragmentExampleActivity;
import com.pierfrancescosoffritti.aytplayersample.examples.iFramePlayerOptionsExample.IFramePlayerOptionsExampleActivity;
import com.pierfrancescosoffritti.aytplayersample.examples.liveVideoExample.LiveVideoActivity;
import com.pierfrancescosoffritti.aytplayersample.examples.pictureInPictureExample.PictureInPictureActivity;
import com.pierfrancescosoffritti.aytplayersample.examples.playerStateExample.PlayerStateActivity;
import com.pierfrancescosoffritti.aytplayersample.examples.recyclerViewExample.RecyclerViewActivity;
import com.pierfrancescosoffritti.aytplayersample.examples.viewPagerExample.ViewPagerActivity;
import com.pierfrancescosoffritti.aytplayersample.examples.webUIExample.WebUIExampleActivity;
import com.psoffritti.librarysampleapptemplate.core.Constants;
import com.psoffritti.librarysampleapptemplate.core.SampleAppTemplateActivity;
import com.psoffritti.librarysampleapptemplate.core.utils.ExampleActivityDetails;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, SampleAppTemplateActivity.class);

        intent.putExtra(Constants.TITLE.name(), getString(R.string.app_name));
        intent.putExtra(Constants.GITHUB_URL.name(), "https://github.com/PierfrancescoSoffritti/android-youtube-player/");
        intent.putExtra(Constants.HOMEPAGE_URL.name(), "https://pierfrancescosoffritti.github.io/android-youtube-player/");
        intent.putExtra(Constants.PLAYSTORE_PACKAGE_NAME.name(), "com.pierfrancescosoffritti.aytplayersample");

        ExampleActivityDetails[] examples = new ExampleActivityDetails[]{
                new ExampleActivityDetails(R.string.basic_example,null, BasicExampleActivity.class),
                new ExampleActivityDetails(R.string.web_ui_example,null, WebUIExampleActivity.class),
                new ExampleActivityDetails(R.string.custom_ui_example,null, CustomUIActivity.class),
                new ExampleActivityDetails(R.string.recycler_view_example,null, RecyclerViewActivity.class),
                new ExampleActivityDetails(R.string.view_pager_example,null, ViewPagerActivity.class),
                new ExampleActivityDetails(R.string.fragment_example,null, FragmentExampleActivity.class),
                new ExampleActivityDetails(R.string.live_video_example,null, LiveVideoActivity.class),
                new ExampleActivityDetails(R.string.player_status_example,null, PlayerStateActivity.class),
                new ExampleActivityDetails(R.string.picture_in_picture_example,null, PictureInPictureActivity.class),
                new ExampleActivityDetails(R.string.chromecast_example,null, ChromeCastExampleActivity.class),
                new ExampleActivityDetails(R.string.iframe_player_options_example,null, IFramePlayerOptionsExampleActivity.class)
        };

        intent.putExtra(Constants.EXAMPLES.name(), examples);

        startActivity(intent);
        finish();
    }
}
