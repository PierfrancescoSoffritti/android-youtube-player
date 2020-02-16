package com.pierfrancescosoffritti.androidyoutubeplayer.core

import android.app.Activity
import android.view.MotionEvent
import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.testActivity.TestActivity
import com.pierfrancescosoffritti.androidyoutubeplayer.test.R
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import java.util.*

@LargeTest
class YouTubePlayerViewClickTest {

    private lateinit var testActivity: Activity
    private lateinit var youTubePlayerView: YouTubePlayerView

    @Rule
    @JvmField
    val activityRule = ActivityTestRule(TestActivity::class.java)

    @Before
    fun setup() {
        testActivity = activityRule.activity
        youTubePlayerView = testActivity.findViewById(R.id.youtube_player_view)
    }

    @Test
    fun testSingleClickListener() {
        // Prepare
        val mockClickListener = mock(View.OnClickListener::class.java)
        youTubePlayerView.setOnClickListener(mockClickListener)

        // Act
        onView(withId(R.id.youtube_player_view)).perform(click())

        // Assert
        verify(mockClickListener).onClick(youTubePlayerView)
    }

    @Test
    fun testLongClickListener() {
        // Prepare
        val mockLongClickListener = mock(View.OnLongClickListener::class.java)
        youTubePlayerView.setOnLongClickListener(mockLongClickListener)

        // Act
        onView(withId(R.id.youtube_player_view)).perform(longClick())

        // Assert
        verify(mockLongClickListener).onLongClick(youTubePlayerView)
    }

    @Test
    fun testTouchSwipeDownListener() {
        // Prepare
        val touchEventQueue = LinkedList<MotionEvent>()
        var onTouchWorks = false
        youTubePlayerView.setOnTouchListener { _, motionEvent ->
            touchEventQueue.add(MotionEvent.obtain(motionEvent))
            onTouchWorks = true
            false
        }

        // Act
        onView(withId(R.id.youtube_player_view)).perform(swipeDown())

        // Assert
        assertTrue(onTouchWorks)

        var prevMotionEvent: MotionEvent? = null
        while (touchEventQueue.isNotEmpty()) {
            val currentEventAction = touchEventQueue.poll()!!
            if (currentEventAction.action == MotionEvent.ACTION_DOWN) {
                prevMotionEvent = currentEventAction
            } else if (currentEventAction.action == MotionEvent.ACTION_MOVE) {
                assertEquals(prevMotionEvent!!.x, currentEventAction.x)
                assertTrue(prevMotionEvent.y < currentEventAction.y)

                prevMotionEvent = currentEventAction
            }
        }

    }

    @Test
    fun testTouchSwipeRightListener() {
        // Prepare
        val touchEventQueue = LinkedList<MotionEvent>()
        var onTouchWorks = false
        youTubePlayerView.setOnTouchListener { _, motionEvent ->
            touchEventQueue.add(MotionEvent.obtain(motionEvent))
            onTouchWorks = true
            false
        }

        // Act
        onView(withId(R.id.youtube_player_view)).perform(swipeRight())

        // Assert
        assertTrue(onTouchWorks)

        var prevMotionEvent: MotionEvent? = null
        while (touchEventQueue.isNotEmpty()) {
            val currentEventAction = touchEventQueue.poll()!!

            if (currentEventAction.action == MotionEvent.ACTION_DOWN) {
                prevMotionEvent = currentEventAction
            } else if (currentEventAction.action == MotionEvent.ACTION_MOVE) {
                assertEquals(prevMotionEvent!!.y, currentEventAction.y)
                assertTrue(prevMotionEvent.x < currentEventAction.x)

                prevMotionEvent = currentEventAction
            }
        }
    }
}