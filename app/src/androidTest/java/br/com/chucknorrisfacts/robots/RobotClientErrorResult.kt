package br.com.chucknorrisfacts.robots

import android.support.test.espresso.Espresso.onView

import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.espresso.matcher.ViewMatchers.*
import br.com.chucknorrisfacts.R

/**
 * @rodrigohsb
 */
class RobotClientErrorResult {

    fun errorHasBeenShown(): Boolean{
        onView(withText(R.string.client_error_message)).check(matches(isDisplayed()))

        onView(withId(R.id.errorButton)).check(matches((isDisplayed())))
        onView(withText(R.string.try_again)).check(matches((isDisplayed())))

        return true
    }
}