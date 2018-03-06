package br.com.chucknorrisfacts.home.handler

import br.com.chucknorrisfacts.home.entry.HomeEntryModel
import br.com.chucknorrisfacts.home.entry.SearchFactsEntryModel
import br.com.chucknorrisfacts.webservice.payload.SearchPayload

/**
 * @rodrigohsb
 */
class FactHandler {

    fun convert(searchPayload: SearchPayload, query: String): SearchFactsEntryModel {

        val size = searchPayload.total

        val list = arrayListOf<HomeEntryModel>()

        searchPayload.searchList?.let{
            it.mapTo(list) { HomeEntryModel(it.text ?: "", it.categories?.get(0)?:"") }
        }
        return SearchFactsEntryModel(homeEntryModel = list, size = size?:0, query = query)
    }
}