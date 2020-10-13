package com.dimi.moviedatabase.util

import android.annotation.SuppressLint
import android.view.View
import androidx.annotation.IdRes
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

fun <T : View> recyclerChildAction(@IdRes id: Int, block: T.() -> Unit): ViewAction {
    return object : ViewAction {
        override fun getConstraints(): Matcher<View>? {
            return null
        }

        override fun getDescription(): String {
            return "Click on a child view with specified id."
        }

        override fun perform(uiController: UiController, view: View) {
            view.findViewById<T>(id).block()
        }
    }
}

fun withIndex(matcher: Matcher<View?>, index: Int): Matcher<View?>? {
    return object : TypeSafeMatcher<View?>() {
        var currentIndex = 0
        var viewObjHash = 0

        @SuppressLint("DefaultLocale")
        override fun describeTo(description: Description) {
            description.appendText(String.format("with index: %d ", index))
            matcher.describeTo(description)
        }

        override fun matchesSafely(view: View?): Boolean {
            if (matcher.matches(view) && currentIndex++ == index) {
                viewObjHash = view.hashCode()
            }
            return view.hashCode() == viewObjHash
        }
    }
}