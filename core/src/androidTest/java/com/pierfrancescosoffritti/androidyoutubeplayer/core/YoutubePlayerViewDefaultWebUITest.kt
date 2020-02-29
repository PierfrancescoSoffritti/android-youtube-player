package com.pierfrancescosoffritti.androidyoutubeplayer.core

import android.app.Activity
import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import androidx.test.rule.ActivityTestRule
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.testActivity.TestActivity
import com.pierfrancescosoffritti.androidyoutubeplayer.test.R
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.not
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
    private lateinit var youTubePlayerViewInitializeWithPlayerListener: YouTubePlayerView
    private lateinit var youTubePlayerViewInitializeWithPlayerListenerAndHandleNetworkEvents: YouTubePlayerView
    private lateinit var youTubePlayerViewInitializeWithPlayerListenerAndHandleNetworkEventsAndIframeOptions: YouTubePlayerView
    private lateinit var youTubePlayerViewInitializeWithNativeUi: YouTubePlayerView

    @Rule
    @JvmField
    val activityRule = ActivityTestRule(TestActivity::class.java)

    @Before
    fun setup() {
        testActivity = activityRule.activity
        /**
         * youTubePlayerViewInitializeWithPlayerListener is manually initialized using:
         *  initialize(object : AbstractYouTubePlayerListener() {})
         */
        youTubePlayerViewInitializeWithPlayerListener = testActivity.findViewById(R.id.youtube_player_view_manual_initialize_player_listener)
        /**
         * youTubePlayerViewInitializeWithPlayerListenerAndHandleNetworkEvents is manually initialized using:
         *  initialize(object : AbstractYouTubePlayerListener() {}, true)
         */
        youTubePlayerViewInitializeWithPlayerListenerAndHandleNetworkEvents = testActivity.findViewById(R.id.youtube_player_view_manual_initialize_player_listener_and_handle_network_events)
        /**
         * youTubePlayerViewInitializeWithPlayerListenerAndHandleNetworkEventsAndIframeOptions is manually initialized using:
         *  initialize(object : AbstractYouTubePlayerListener() {}, true, IFramePlayerOptions.Builder().controls(1).build())
         */
        youTubePlayerViewInitializeWithPlayerListenerAndHandleNetworkEventsAndIframeOptions = testActivity.findViewById(R.id.youtube_player_view_manual_initialize_player_listener_and_handle_network_events_and_iframe_options)
        /**
         * youTubePlayerViewInitializeWithNativeUi is manually initialized using:
         *  initializeWithNativeUi(object : AbstractYouTubePlayerListener() {}, true)
         */
        youTubePlayerViewInitializeWithNativeUi = testActivity.findViewById(R.id.youtube_player_view_manual_initialize_native)
    }


    /**
     * Returns the FrameLayout of the Native UI for the given [youtubePlayerId] of a player.
     * @param youtubePlayerId Resource id of a YouTubePlayerView
     * @return ViewMatcher of Native UI FrameLayout
     */
    private fun getNativeUiViewGroup(youtubePlayerId: Int): Matcher<View> {
        return allOf(
                isDescendantOfA(withId(youtubePlayerId)),
                withId(R.id.ayp_default_native_ui_layout)
        )
    }


    @Test
    fun testDefaultIsWebUi() {
        // Prepare

        // Act

        // Assert
        onView(getNativeUiViewGroup(R.id.youtube_player_view)).check(doesNotExist())
    }

    @Test
    fun testNativeUiIsEnabledWithXmlAttribute() {
        // Prepare

        // Act

        // Assert
        onView(getNativeUiViewGroup(R.id.youtube_player_view_native_ui)).check(matches(not(doesNotExist())))
    }

    @Test
    fun testWebUiIsDefaultWhenViewInitializedProgrammaticallyUsingPlayerListener() {
        // Prepare

        // Act
        runOnUiThread {
            youTubePlayerViewInitializeWithPlayerListener.initialize(object : AbstractYouTubePlayerListener() { })
        }

        // Assert
        onView(getNativeUiViewGroup(R.id.youtube_player_view_manual_initialize_player_listener)).check(doesNotExist())
    }

    @Test
    fun testWebUiIsDefaultWhenViewInitializedProgrammaticallyUsingPlayerListenerAndHandleNetworkEvents() {
        // Prepare

        // Act
        runOnUiThread {
            youTubePlayerViewInitializeWithPlayerListenerAndHandleNetworkEvents.initialize(object : AbstractYouTubePlayerListener() {

            }, true)
        }

        // Assert
        onView(getNativeUiViewGroup(R.id.youtube_player_view_manual_initialize_player_listener_and_handle_network_events)).check(doesNotExist())
    }

    @Test
    fun testWebUiIsDefaultWhenViewInitializedProgrammaticallyUsingPlayerListenerAndHandleNetworkEventsAndIframeOptions() {
        // Prepare

        // Act
        runOnUiThread {
            youTubePlayerViewInitializeWithPlayerListenerAndHandleNetworkEventsAndIframeOptions.initialize(object : AbstractYouTubePlayerListener() {

            }, true, IFramePlayerOptions.Builder().controls(1).build())
        }

        // Assert
        onView(getNativeUiViewGroup(R.id.youtube_player_view_manual_initialize_player_listener_and_handle_network_events_and_iframe_options)).check(doesNotExist())
    }

    @Test
    fun testNativeUiInitializedProgrammatically() {
        // Prepare
        youTubePlayerViewInitializeWithNativeUi.initializeWithNativeUi(object : AbstractYouTubePlayerListener() {

        }, true)
        // Act

        // Assert
        onView(withId(R.id.youtube_player_view_manual_initialize_native)).check(matches(not(doesNotExist())))
    }
}