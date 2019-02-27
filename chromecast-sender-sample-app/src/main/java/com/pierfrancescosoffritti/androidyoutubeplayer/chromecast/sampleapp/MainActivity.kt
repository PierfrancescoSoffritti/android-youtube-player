package com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.sampleapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.sampleapp.examples.basicExample.BasicExampleActivity
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.sampleapp.examples.localPlayerExample.LocalPlayerInitExampleActivity
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.sampleapp.examples.notificationExample.NotificationExampleActivity
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.sampleapp.examples.playerControlsExample.PlayerControlsExample
import com.pierfrancescosoffritti.cyplayersample.R
import com.psoffritti.librarysampleapptemplate.core.Constants
import com.psoffritti.librarysampleapptemplate.core.SampleAppTemplateActivity
import com.psoffritti.librarysampleapptemplate.core.utils.ExampleActivityDetails

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = Intent(this, SampleAppTemplateActivity::class.java)

        intent.putExtra(Constants.TITLE.name, getString(R.string.app_name))
        intent.putExtra(Constants.GITHUB_URL.name, "https://github.com/PierfrancescoSoffritti/android-youtube-player/")
        intent.putExtra(Constants.HOMEPAGE_URL.name, "https://pierfrancescosoffritti.github.io/android-youtube-player/")

        val examples = arrayOf(
                ExampleActivityDetails(R.string.basic_example, null, BasicExampleActivity::class.java),
                ExampleActivityDetails(R.string.player_controls_example, null, PlayerControlsExample::class.java),
                ExampleActivityDetails(R.string.notification_example, null, NotificationExampleActivity::class.java),
                ExampleActivityDetails(R.string.local_player_example, null, LocalPlayerInitExampleActivity::class.java)
        )

        intent.putExtra(Constants.EXAMPLES.name, examples)

        startActivity(intent)
        finish()
    }
}
