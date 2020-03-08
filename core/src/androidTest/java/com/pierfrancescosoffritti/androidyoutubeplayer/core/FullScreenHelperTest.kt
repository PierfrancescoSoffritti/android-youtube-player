package com.pierfrancescosoffritti.androidyoutubeplayer.core

import android.app.Activity
import android.view.ViewGroup
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import androidx.test.rule.ActivityTestRule
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.FullScreenHelper
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.testActivity.TestActivity
import com.pierfrancescosoffritti.androidyoutubeplayer.test.R
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*

/**
 * This class tests if the FullScreenHelper's enterFullScreen(), exitFullScreen(), toggleFullScreen(),
 * addFullScreenListener() and removeFullScreenListener() methods work as expected
 */
@LargeTest
class FullScreenHelperTest {

    private lateinit var testActivity: Activity
    private lateinit var youTubePlayerView: YouTubePlayerView

    private fun assertIsFullScreen(fullScreenHelper: FullScreenHelper, youTubePlayerView: YouTubePlayerView) {
        assertTrue("Testing if isFullScreen boolean is set to true", fullScreenHelper.isFullScreen)
        assertTrue("Testing if height is MATCH_PARENT", youTubePlayerView.layoutParams.height == ViewGroup.LayoutParams.MATCH_PARENT)
        assertTrue("Testing if width is MATCH_PARENT", youTubePlayerView.layoutParams.width == ViewGroup.LayoutParams.MATCH_PARENT)
    }

    private fun assertIsNotFullScreen(fullScreenHelper: FullScreenHelper, youTubePlayerView: YouTubePlayerView) {
        assertTrue("Testing if isFullScreen boolean is set to false", !fullScreenHelper.isFullScreen)
        assertTrue("Testing if height is WRAP_CONTENT", youTubePlayerView.layoutParams.height == ViewGroup.LayoutParams.WRAP_CONTENT)
        assertTrue("Testing if width is MATCH_PARENT", youTubePlayerView.layoutParams.width == ViewGroup.LayoutParams.MATCH_PARENT)
    }

    @Rule
    @JvmField
    val activityRule = ActivityTestRule(TestActivity::class.java)

    @Before
    fun setup() {
        testActivity = activityRule.activity
        youTubePlayerView = testActivity.findViewById(R.id.youtube_player_view)
    }

    @Test
    fun testEnterFullScreen() {
        // Prepare
        val fullScreenHelper = FullScreenHelper(youTubePlayerView)

        // Act
        runOnUiThread {
            fullScreenHelper.enterFullScreen()
        }

        // Assert
        assertIsFullScreen(fullScreenHelper, youTubePlayerView)
    }

    @Test
    fun testExitFullScreen() {
        // Prepare
        val fullScreenHelper = FullScreenHelper(youTubePlayerView)

        // Act
        runOnUiThread {
            fullScreenHelper.enterFullScreen() // enter full screen before attempting to exit
            fullScreenHelper.exitFullScreen()
        }

        // Assert
        assertIsNotFullScreen(fullScreenHelper, youTubePlayerView)
    }

    @Test
    fun testToggleFullScreen() {
        // Prepare
        val fullScreenHelper = FullScreenHelper(youTubePlayerView)

        runOnUiThread {
            // Act
            fullScreenHelper.toggleFullScreen()
            // Assert
            assertIsFullScreen(fullScreenHelper, youTubePlayerView)

            // Act
            fullScreenHelper.toggleFullScreen()
            // Assert
            assertIsNotFullScreen(fullScreenHelper, youTubePlayerView)
        }
    }

    @Test
    fun testAddAndRemoveFullScreenListener() {
        // Prepare
        val mockFullScreenListener = mock(YouTubePlayerFullScreenListener::class.java)

        val fullScreenHelper = FullScreenHelper(youTubePlayerView)
        fullScreenHelper.addFullScreenListener(mockFullScreenListener)

        runOnUiThread {
            // Act
            fullScreenHelper.enterFullScreen()
            // Assert
            verify(mockFullScreenListener).onYouTubePlayerEnterFullScreen() // check if listener is added

            // Act
            fullScreenHelper.removeFullScreenListener(mockFullScreenListener)
            fullScreenHelper.exitFullScreen() // if listener is removed successfully, this should not call onYouTubePlayerExitFullScreen()
            // Assert
            verify(mockFullScreenListener, never()).onYouTubePlayerExitFullScreen()
        }
    }
}