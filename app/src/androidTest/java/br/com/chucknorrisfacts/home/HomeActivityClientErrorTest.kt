package br.com.chucknorrisfacts.home

import br.com.chucknorrisfacts.AcceptanceTest
import br.com.chucknorrisfacts.RequestInterceptorMock
import br.com.chucknorrisfacts.home.ui.HomeActivity
import br.com.chucknorrisfacts.robots.robot
import br.com.chucknorrisfacts.webservice.exceptions.Error4XXException
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.provider
import okhttp3.Interceptor
import org.junit.Test

/**
 * @rodrigohsb
 */
class HomeActivityClientErrorTest : AcceptanceTest<HomeActivity>(HomeActivity::class.java) {

    @Test
    fun testWithClientErrorState() {

        startActivity()

        robot {
            withWaitingForInput()
            doSearch("movie")
        } withClientError {
            errorHasBeenShown()
        }
    }

    override val testDependencies = Kodein.Module(allowSilentOverride = true) {
        bind<Interceptor>(tag = "RequestInterceptor",overrides = true) with provider {
            RequestInterceptorMock(Error4XXException())
        }
    }
}