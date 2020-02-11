package com.pierfrancescosoffritti.androidyoutubeplayer.core

import android.app.Activity
import android.view.View
import android.widget.FrameLayout
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.LegacyYouTubePlayerView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.testActivity.TestActivity
import com.pierfrancescosoffritti.androidyoutubeplayer.test.R
import org.hamcrest.CoreMatchers.*
import org.hamcrest.Matcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * This class tests if the player's default ui is the web-based ui.
 * Additionally, it tests all 4 manual initialization methods to check
 * if they create their expected UI elements.
 */
@LargeTest
class YoutubePlayerViewDefaultWebUITest {

    private lateinit var testActivity: Activity
    private lateinit var youTubePlayerViewUsingInitializer1: YouTubePlayerView
    private lateinit var youTubePlayerViewUsingInitializer2: YouTubePlayerView
    private lateinit var youTubePlayerViewUsingInitializer3: YouTubePlayerView
    private lateinit var youTubePlayerViewUsingInitializeWithNativeUi: YouTubePlayerView

    @Rule
    @JvmField
    val activityRule = ActivityTestRule(TestActivity::class.java)

    @Before
    fun setup() {
        testActivity = activityRule.activity
        youTubePlayerViewUsingInitializer1 = testActivity.findViewById(R.id.youtube_player_view_manual_initialize_1)
        youTubePlayerViewUsingInitializer2 = testActivity.findViewById(R.id.youtube_player_view_manual_initialize_2)
        youTubePlayerViewUsingInitializer3 = testActivity.findViewById(R.id.youtube_player_view_manual_initialize_3)
        youTubePlayerViewUsingInitializeWithNativeUi = testActivity.findViewById(R.id.youtube_player_view_manual_initialize_native)
    }


    /**
     * Returns the FrameLayout of the Native UI for the given [youtubePlayerId] of a player.
     * @param youtubePlayerId Resource id of a YouTubePlayerView
     * @return ViewMatcher of Native UI FrameLayout
     */
    private fun getNativeUiViewGroup(youtubePlayerId: Int): Matcher<View> {
        return allOf(
                isDescendantOfA(
                        withId(youtubePlayerId)
                ),
                withClassName(
                        containsString(
                                FrameLayout::class.java.canonicalName)
                ),
                withParent(
                        withClassName(
                                containsString(
                                        LegacyYouTubePlayerView::class.java.canonicalName)
                        )
                )
        )
    }


    @Test
    fun testIfDefaultIsWebUi() {
        // Prepare

        // Act

        // Assert
        onView(
                getNativeUiViewGroup(R.id.youtube_player_view)
        ).check(matches(hasChildCount(0)))
    }

    @Test
    fun testIfNativeViewIsSetByXMLAttribute() {
        // Prepare

        // Act

        // Assert
        onView(
                getNativeUiViewGroup(R.id.youtube_player_view_native)
        ).check(
                matches(
                        not(hasChildCount(0))
                )
        )
    }

    @Test
    fun testIfDefaultWebUiInitializedProgrammaticallyUsingInitializer1() {
        // Prepare

        // Act
        testActivity.runOnUiThread {
            youTubePlayerViewUsingInitializer1.initialize(object : AbstractYouTubePlayerListener() {

            })
        }

        // Assert
        onView(
                getNativeUiViewGroup(R.id.youtube_player_view_manual_initialize_1)
        ).check(matches(hasChildCount(0)))
    }

    @Test
    fun testIfDefaultWebUiInitializedProgrammaticallyUsingInitializer2() {
        // Prepare

        // Act
        testActivity.runOnUiThread {
            youTubePlayerViewUsingInitializer2.initialize(object : AbstractYouTubePlayerListener() {

            }, true)
        }

        // Assert
        onView(getNativeUiViewGroup(R.id.youtube_player_view_manual_initialize_2)
        ).check(matches(hasChildCount(0)))
    }

    @Test
    fun testIfDefaultWebUiInitializedProgrammaticallyUsingInitializer3() {
        // Prepare

        // Act
        testActivity.runOnUiThread {
            youTubePlayerViewUsingInitializer3.initialize(object : AbstractYouTubePlayerListener() {

            }, true, IFramePlayerOptions.Builder().controls(1).build())
        }

        // Assert
        onView(
                getNativeUiViewGroup(R.id.youtube_player_view_manual_initialize_3)
        ).check(matches(hasChildCount(0)))
    }

    @Test
    fun testIfNativeUiInitializedProgrammatically() {
        // Prepare
        youTubePlayerViewUsingInitializeWithNativeUi.initializeWithNativeUi(object : AbstractYouTubePlayerListener() {

        }, true)
        // Act

        // Assert
        onView(
                getNativeUiViewGroup(R.id.youtube_player_view_manual_initialize_native)
        ).check(
                matches(
                        not(hasChildCount(0))
                )
        )
    }
}