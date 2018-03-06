package br.com.chucknorrisfacts

import br.com.chucknorrisfacts.home.service.HomeService
import br.com.chucknorrisfacts.webservice.WebServiceAPI
import br.com.chucknorrisfacts.webservice.payload.SearchList
import br.com.chucknorrisfacts.webservice.payload.SearchPayload
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable.just
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations.initMocks
import java.util.*

/**
 * @rodrigohsb
 */
class HomeServiceTest {
    
    @Mock
    private lateinit var webServiceAPI: WebServiceAPI

    private lateinit var homeService: HomeService
    private lateinit var categories: ArrayList<String>
    private lateinit var searchList: ArrayList<SearchList>

    @Before
    fun setup(){
        initMocks(this)
        homeService = HomeService(webServiceAPI)
    }

    private fun createPayload(): SearchPayload {

        categories = ArrayList<String>()
        categories.add("")
        categories.add("")
        categories.add("")
        categories.add("")
        val search = SearchList("", "", "", "", categories)

        searchList = ArrayList<SearchList>()
        searchList.add(search)
        searchList.add(search)
        searchList.add(search)
        searchList.add(search)
        searchList.add(search)

        return SearchPayload(5, searchList)
    }

    @Test
    @Ignore
    fun test(){

        whenever(homeService.searchFacts(""))
                .thenReturn(just(createPayload()))

        val testObserver = homeService
                .searchFacts("")
                .test()

        testObserver.awaitTerminalEvent()

        testObserver
        .assertNoErrors()
        .assertValue {
                payload ->
                payload.searchList!!.size == searchList.size
                payload.total == 5
                payload.searchList!![0].categories!!.size == categories.size
            }
    }
}