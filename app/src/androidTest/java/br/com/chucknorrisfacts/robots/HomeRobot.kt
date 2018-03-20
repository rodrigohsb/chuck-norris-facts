package br.com.chucknorrisfacts.robots

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.assertion.ViewAssertions.*
import android.support.test.espresso.matcher.ViewMatchers.*
import android.view.KeyEvent
import br.com.chucknorrisfacts.R

/**
 * @rodrigohsb
 */

fun robot(func: HomeRobot.() -> Unit) = HomeRobot().apply{func()}

class HomeRobot {

    fun withWaitingForInput() =
            onView(withId(R.id.waitingForInputState)).check(matches(isDisplayed()))

    fun doSearch(query: String) {
        onView(withId(R.id.mainFAB)).perform(click())
        onView(withId(R.id.searchView)).check(matches(isDisplayed()))
        onView(withId(R.id.search_button)).perform(click())
        onView(withId(R.id.search_src_text)).perform(typeText(query),
                pressKey(KeyEvent.KEYCODE_ENTER))
    }

    fun withLoading() = onView(withId(R.id.loadingState)).check(matches(isDisplayed()))

    infix fun withContent(func: RobotContentResult.() -> Unit) =
            RobotContentResult().apply{ func() }

    infix fun withBadRequest(func: RobotBadRequestResult.() -> Unit) =
            RobotBadRequestResult().apply{ func() }

    infix fun withEmptyState(func: RobotEmptyResult.() -> Unit) =
            RobotEmptyResult().apply{ func() }

    infix fun withGenericError(func: RobotGenericErrorResult.() -> Unit) =
            RobotGenericErrorResult().apply{ func() }

    infix fun withClientError(func: RobotClientErrorResult.() -> Unit) =
            RobotClientErrorResult().apply{ func() }

    infix fun withServerError(func: RobotServerErrorResult.() -> Unit) =
            RobotServerErrorResult().apply{ func() }

    infix fun withTimeout(func: RobotTimeoutResult.() -> Unit) =
            RobotTimeoutResult().apply{ func() }

    infix fun withoutConnection(func: RobotNoNetworkResult.() -> Unit) =
            RobotNoNetworkResult().apply{ func() }
}