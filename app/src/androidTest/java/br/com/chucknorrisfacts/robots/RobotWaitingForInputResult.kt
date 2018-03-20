package br.com.chucknorrisfacts.robots

import android.support.test.espresso.Espresso.onView

import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import br.com.chucknorrisfacts.R

/**
 * @rodrigohsb
 */
class RobotWaitingForInputResult {

    fun isSuccess(): Boolean{
        onView(withId(R.id.waitingForInputState)).check(matches(isDisplayed()))
        return true
    }
}