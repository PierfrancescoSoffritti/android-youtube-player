package com.pierfrancescosoffritti.androidyoutubeplayer.core;

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.pierfrancescosoffritti.androidyoutubeplayer.core.testActivity.TestActivity
import com.pierfrancescosoffritti.androidyoutubeplayer.test.R
import org.junit.Rule
import org.junit.Test

@LargeTest
class HelloWorldTest {

    @Rule @JvmField
    val activityRule = ActivityTestRule(TestActivity::class.java)

    @Test
    fun testIsViewVisible() {
        onView(withId(R.id.youtube_player_view)).check(matches(isDisplayed()));
    }
}