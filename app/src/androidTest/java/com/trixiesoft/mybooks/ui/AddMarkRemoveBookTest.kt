package com.trixiesoft.mybooks.ui

import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.trixiesoft.mybooks.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

// Need to use Espresso idling resources to check for element presence and timeout, instead of absolute TimeWait
// Need to create library of shared functions
// Need to separate out tests and organize into suites
// Need to add into Firebase Test Labs for multi device configurations automation
// Need to add better testLogging results to show names of tests passed/failed

@LargeTest
@RunWith(AndroidJUnit4::class)
class AddMarkRemoveBookTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun addMarkRemoveBook() {

        // Click add book button
        getAddButton().perform(click())

        // Type in search keyword "qis"
        getSearchField().perform(replaceText("qis"), closeSoftKeyboard())

        Thread.sleep(1000)

        // Verify book result 0 exists
        getBookResult(1, 0).check(matches(isDisplayed()))

        // Verify book result 1 exists
        getBookResult(1, 1).check(matches(isDisplayed()))

        // Verify book result 2 exists
        getBookResult(1, 2).check(matches(isDisplayed()))

        // Click on book result 0 to add to list
        getBookResult(1, 0).perform(click())

        // Verify book has been added
        getBookListItem(0).check(matches(isDisplayed()))

        // Right click on book item 0 to open options
        getBookMenu(0).perform(click())

        Thread.sleep(250)

        // Click on "Mark as Read"
        getBookOption("Mark As Read").perform(click())

        // Verify book has been removed from current category
        getBookListItem(0).check(ViewAssertions.doesNotExist())

        // Change tabs
        getTab(1).perform(click())

        // Right click on book item to open options
        getBookMenu(0).perform(click())

        Thread.sleep(250)

        // Click to remove book
        getBookOption("Remove Book").perform(click())

        // Verify book has been deleted
        getBookListItem(0).check(ViewAssertions.doesNotExist())

        // Change tabs
        getTab(0).perform(click())

        // Verify book does not exist in other list either
        getBookListItem(0).check(ViewAssertions.doesNotExist())

    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }

    private fun getAddButton () : ViewInteraction {
        return onView( allOf(
            withId(R.id.add_book),
            childAtPosition(
                childAtPosition(
                    withClassName(`is`("android.widget.FrameLayout")), 0 ),
                3 ),
            isDisplayed() ) )
    }

    private fun getSearchField () : ViewInteraction {
        return onView( allOf(
            withId(R.id.searchEditText),
            childAtPosition(
                childAtPosition( withId(R.id.searchEditOverlay), 0 ),
                1 ),
            isDisplayed() ) )
    }

    private fun getBookResult (layoutIndex : Int, itemIndex : Int) : ViewInteraction {
        return onView( allOf( childAtPosition( allOf(
            withId(R.id.recyclerView),
            childAtPosition(
                withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                layoutIndex ) ),
            itemIndex ),
            isDisplayed() ) )
    }

    private fun getBookListItem (bookIndex : Int) : ViewInteraction {
        return onView( allOf( childAtPosition( allOf(
            withId(R.id.recyclerView),
            childAtPosition(
                IsInstanceOf.instanceOf(android.widget.FrameLayout::class.java),
                0 ) ),
            bookIndex ),
            isDisplayed() ) )
    }

    private fun getBookMenu (bookIndex : Int) : ViewInteraction {
        return onView( allOf(
            withId(R.id.menu),
            childAtPosition(
                childAtPosition(
                    withId(R.id.recyclerView),
                    bookIndex ),
                2 ),
            isDisplayed() ) )
    }

    private fun getBookOption (label : String) : ViewInteraction {
        return onView( allOf(
            withId(R.id.title), withText(label),
            childAtPosition(
                childAtPosition(
                    withId(R.id.content),
                    1 ),
                0 ),
            isDisplayed() ) )
    }

    private fun getTab (index : Int) : ViewInteraction {
        return onView( allOf(
            childAtPosition(
                childAtPosition(
                    withId(R.id.tab_layout),
                    0 ),
                index ),
            isDisplayed() ) )
    }
}
