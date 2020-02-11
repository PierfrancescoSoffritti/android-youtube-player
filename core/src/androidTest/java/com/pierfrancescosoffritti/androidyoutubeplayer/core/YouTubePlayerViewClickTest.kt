package com.pierfrancescosoffritti.androidyoutubeplayer.core

import android.view.MotionEvent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.pierfrancescosoffritti.androidyoutubeplayer.R
import com.pierfrancescosoffritti.androidyoutubeplayer.core.helpers.MotionEventPrimitive
import com.pierfrancescosoffritti.androidyoutubeplayer.core.testActivity.TestActivity
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@LargeTest
class YouTubePlayerViewClickTest {

    @Rule @JvmField
    var activityRule = ActivityTestRule(TestActivity::class.java)

    @Test
    fun testSingleClickListener() {
        onView(withId(R.id.youtube_player_view)).perform(click())
        assertTrue("Checking if single click works",TestActivity.CLICK_WORKS)
    }

    @Test
    fun testLongClickListener() {
        onView(withId(R.id.youtube_player_view)).perform(longClick())
        assertTrue("Checking if long click works",TestActivity.LONG_CLICK_WORKS)
    }
    
    @Test
    fun testTouchSwipeDownListener() {
        onView(withId(R.id.youtube_player_view)).perform(swipeDown())

        var prevMotionEvent: MotionEventPrimitive? = null
        while(TestActivity.TOUCH_EVENT_QUEUE.isNotEmpty()){
            val actualEventAction = TestActivity.TOUCH_EVENT_QUEUE.poll()

            if(actualEventAction!!.action == MotionEvent.ACTION_DOWN) {
                prevMotionEvent = actualEventAction
            }
            else if(actualEventAction.action == MotionEvent.ACTION_MOVE)  {

                assertEquals(prevMotionEvent!!.X, actualEventAction.X)
                assertTrue(prevMotionEvent.Y < actualEventAction.Y)

                prevMotionEvent = actualEventAction

            }
        }
        Assert.assertNotNull(prevMotionEvent)
    }

    @Test
    fun testTouchSwipeRightListener() {
        onView(withId(R.id.youtube_player_view)).perform(swipeRight())

        var prevMotionEvent: MotionEventPrimitive? = null
        while(TestActivity.TOUCH_EVENT_QUEUE.isNotEmpty()){
            val actualEventAction = TestActivity.TOUCH_EVENT_QUEUE.poll()

            if(actualEventAction.action == MotionEvent.ACTION_DOWN) {
                prevMotionEvent = actualEventAction
            }
            else if(actualEventAction.action == MotionEvent.ACTION_MOVE)  {

                assertEquals(prevMotionEvent?.Y, actualEventAction.Y)
                assertTrue(prevMotionEvent?.X!! < actualEventAction.X)

                prevMotionEvent = actualEventAction

            }
        }
        Assert.assertNotNull(prevMotionEvent)
    }
}