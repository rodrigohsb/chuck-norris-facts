package br.com.chucknorrisfacts.home

import br.com.chucknorrisfacts.AcceptanceTest
import br.com.chucknorrisfacts.RequestInterceptorMock
import br.com.chucknorrisfacts.home.ui.HomeActivity
import br.com.chucknorrisfacts.robots.robot
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.provider
import okhttp3.Interceptor
import org.junit.Test

/**
 * @rodrigohsb
 */
class HomeActivityGenericErrorTest : AcceptanceTest<HomeActivity>(HomeActivity::class.java) {

    @Test
    fun testWithGenericErrorState() {

        startActivity()

        robot {
            withWaitingForInput()
            doSearch("movie")
        } withGenericError {
            errorHasBeenShown()
        }
    }

    override val testDependencies = Kodein.Module(allowSilentOverride = true) {
        bind<Interceptor>(tag = "RequestInterceptor",overrides = true) with provider {
            RequestInterceptorMock(Exception())
        }
    }
}