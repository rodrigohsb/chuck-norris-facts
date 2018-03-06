package br.com.chucknorrisfacts

import android.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.chucknorrisfacts.home.handler.FactHandler
import br.com.chucknorrisfacts.home.service.HomeService
import br.com.chucknorrisfacts.home.status.HomeStatus
import br.com.chucknorrisfacts.home.viewmodel.HomeViewModel
import br.com.chucknorrisfacts.webservice.payload.SearchList
import br.com.chucknorrisfacts.webservice.payload.SearchPayload
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
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

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setup(){
        initMocks(this)
        viewModel = HomeViewModel(homeService,factHandler)
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
    fun test(){

        whenever(homeService.searchFacts("")).thenReturn(Observable.just(createPayload()))

        viewModel.searchContent("")

        verify(homeService, times(1)).searchFacts("")
        verify(factHandler, times(1)).convert(createPayload(), "")
    }
}