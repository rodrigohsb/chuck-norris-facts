package br.com.chucknorrisfacts

import br.com.chucknorrisfacts.home.handler.FactHandler
import br.com.chucknorrisfacts.webservice.payload.SearchList
import br.com.chucknorrisfacts.webservice.payload.SearchPayload
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Test
import org.mockito.Mockito

/**
 * @rodrigohsb
 */
class FactHandlerTest {

    private val factHandler = FactHandler()

    private lateinit var searchPayload: SearchPayload

    @Test
    fun `test when size is 5`(){

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

        searchPayload = SearchPayload(5, searchList)

        val searchFactsEntryModel = factHandler.convert(searchPayload, "TEXT")
        assertThat(searchFactsEntryModel.homeEntryModel.size, equalTo(5))
        assertThat(searchFactsEntryModel.size, equalTo(5))
        assertThat(searchFactsEntryModel.query, equalTo("TEXT"))
    }

    @Test
    fun `test when size is 10 and no content`(){

        searchPayload = SearchPayload(10, null)

        val searchFactsEntryModel = factHandler.convert(searchPayload, "TEXT")
        assertThat(searchFactsEntryModel.homeEntryModel.size, equalTo(0))
        assertThat(searchFactsEntryModel.size, equalTo(10))
        assertThat(searchFactsEntryModel.query, equalTo("TEXT"))
    }

    @Test
    fun `test when everything is null besides text que`(){

        searchPayload = SearchPayload(null, null)

        val searchFactsEntryModel = factHandler.convert(searchPayload, "TEXT")
        assertThat(searchFactsEntryModel.size, equalTo(0))
        assertThat(searchFactsEntryModel.homeEntryModel.size, equalTo(0))
        assertThat(searchFactsEntryModel.query, equalTo("TEXT"))
    }

    @Test
    fun `test when everything is null`(){

        searchPayload = SearchPayload(null, null)

        val searchFactsEntryModel = factHandler.convert(searchPayload, "")
        assertThat(searchFactsEntryModel.size, equalTo(0))
        assertThat(searchFactsEntryModel.homeEntryModel.size, equalTo(0))
        assertThat(searchFactsEntryModel.query, equalTo(""))
    }

}