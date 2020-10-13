package com.dimi.moviedatabase

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.dimi.moviedatabase.util.ViewShownIdlingResource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.hamcrest.Matcher

@ExperimentalCoroutinesApi
@FlowPreview
abstract class BaseTest {

    // wait for a certain view to be shown.
    fun waitViewShown(matcher: Matcher<View>) {
        val idlingResource: IdlingResource = ViewShownIdlingResource(matcher, isDisplayed())
        try {
            IdlingRegistry.getInstance().register(idlingResource)
            onView(withId(0)).check(doesNotExist())
        } finally {
            IdlingRegistry.getInstance().unregister(idlingResource)
        }
    }
}













