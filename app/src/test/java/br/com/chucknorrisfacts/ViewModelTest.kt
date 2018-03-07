package br.com.chucknorrisfacts

import android.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.chucknorrisfacts.home.entry.SearchFactsEntryModel
import br.com.chucknorrisfacts.home.handler.FactHandler
import br.com.chucknorrisfacts.home.service.HomeService
import br.com.chucknorrisfacts.home.status.State
import br.com.chucknorrisfacts.home.viewmodel.HomeViewModel
import br.com.chucknorrisfacts.webservice.WebServiceAPI
import br.com.chucknorrisfacts.webservice.exceptions.*
import br.com.chucknorrisfacts.webservice.payload.SearchList
import br.com.chucknorrisfacts.webservice.payload.SearchPayload
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations.initMocks

/**
 * @rodrigohsb
 */
class ViewModelTest {

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var homeService: HomeService

    @Mock
    private lateinit var factHandler: FactHandler

    @Mock
    private lateinit var webServiceAPI: WebServiceAPI

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setup() {
        initMocks(this)
        viewModel = HomeViewModel(homeService, factHandler)
    }

    private fun createPayload(): SearchPayload {

        val categories = ArrayList<String>()
        categories.add("")
        categories.add("")
        categories.add("")
        categories.add("")
        val search = SearchList("", "", "", "", categories)

        val searchList = ArrayList<SearchList>()
        searchList.add(search)
        searchList.add(search)
        searchList.add(search)
        searchList.add(search)
        searchList.add(search)

        return SearchPayload(5, searchList)

    }

    @Test
    fun `test if status is waiting for input`() {

        val testObserver = viewModel.fetchState().test()

        testObserver.awaitTerminalEvent()

        testObserver
        .assertNoErrors()
        .assertValue { status -> status is State.WaitingForInput }
        .isTerminated
    }

    @Test
    fun `test if status is Loading and then has content`() {

        val searchFactsEntryModel = SearchFactsEntryModel(1, "", emptyList())

        whenever(homeService.searchFacts("")).thenReturn(Observable.just(createPayload()))
        whenever(factHandler.convert(createPayload(), "")).thenReturn(searchFactsEntryModel)

        val testObserver = viewModel.searchContent("").test()

        testObserver.awaitTerminalEvent()

        val values = testObserver
                .assertComplete()
                .assertNoErrors()
                .values()

        assertTrue((values.size == 2)
                .and(values[0] is State.Loading)
                .and(values[1] is State.Sucess))
    }

    @Test
    fun `test if status starts with Loading and ends with BadRequestException`() {

        mockMethodsWith(BadRequestException())

        val testObserver = viewModel.searchContent("").test()

        testObserver.awaitTerminalEvent()

        val values = testObserver
                .assertComplete()
                .assertNoErrors()
                .values()

        assertTrue((values.size == 2)
                .and(values[0] is State.Loading)
                .and((values[1] as State.Error).exception is BadRequestException))
    }

    private fun mockMethodsWith(value: Exception) {
        whenever(homeService.searchFacts("")).thenReturn(Observable.error(value))
        whenever(webServiceAPI.getFactBySearch("")).thenReturn(Observable.error(value))
    }

    @Test
    fun `test if status starts with Loading and ends with Error4XXException`() {

        mockMethodsWith(Error4XXException())

        val testObserver = viewModel.searchContent("").test()

        testObserver.awaitTerminalEvent()

        val values = testObserver
                .assertComplete()
                .assertNoErrors()
                .values()

        assertTrue((values.size == 2)
                .and(values[0] is State.Loading)
                .and((values[1] as State.Error).exception is Error4XXException))

    }

    @Test
    fun `test if status starts with Loading and ends with Error5XXException`() {

        mockMethodsWith(Error5XXException())

        val testObserver = viewModel.searchContent("").test()

        testObserver.awaitTerminalEvent()

        val values = testObserver
                .assertComplete()
                .assertNoErrors()
                .values()

        assertTrue((values.size == 2)
                .and(values[0] is State.Loading)
                .and((values[1] as State.Error).exception is Error5XXException))
    }

    @Test
    fun `test if status starts with Loading and ends with NoDataException`() {

        mockMethodsWith(NoDataException())

        val testObserver = viewModel.searchContent("").test()

        testObserver.awaitTerminalEvent()

        val values = testObserver
                .assertComplete()
                .assertNoErrors()
                .values()

        assertTrue((values.size == 2)
                .and(values[0] is State.Loading)
                .and((values[1] as State.Error).exception is NoDataException))
    }

    @Test
    fun `test if status starts with Loading and ends with NoNetworkException`() {

        mockMethodsWith(NoNetworkException())

        val testObserver = viewModel.searchContent("").test()

        testObserver.awaitTerminalEvent()

        val values = testObserver
                .assertComplete()
                .assertNoErrors()
                .values()

        assertTrue((values.size == 2)
                .and(values[0] is State.Loading)
                .and((values[1] as State.Error).exception is NoNetworkException))
    }

    @Test
    fun `test if status starts with Loading and ends with TimeoutException`() {

        mockMethodsWith(TimeoutException())

        val testObserver = viewModel.searchContent("").test()

        testObserver.awaitTerminalEvent()

        val values = testObserver
                .assertComplete()
                .assertNoErrors()
                .values()

        assertTrue((values.size == 2)
                .and(values[0] is State.Loading)
                .and((values[1] as State.Error).exception is TimeoutException))
    }
}