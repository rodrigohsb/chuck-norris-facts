package br.com.chucknorrisfacts.home

import br.com.chucknorrisfacts.AcceptanceTest
import br.com.chucknorrisfacts.home.ui.HomeActivity
import br.com.chucknorrisfacts.robots.robot
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.singleton
import okhttp3.mockwebserver.MockResponse
import org.junit.Test

/**
 * @rodrigohsb
 */
class HomeActivityContentTest : AcceptanceTest<HomeActivity>(HomeActivity::class.java) {

    @Test
    fun testWithContentState() {

        server.enqueue(MockResponse()
                .setResponseCode(200)
                .setBody(getJson("success.json")))

        startActivity()

        robot{
            withWaitingForInput()
            doSearch("movie")
        } withContent {
            isSuccess()
        }
    }
    override val testDependencies = Kodein.Module(allowSilentOverride = true) {}
}