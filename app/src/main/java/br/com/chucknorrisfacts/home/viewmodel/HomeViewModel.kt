package br.com.chucknorrisfacts.home.viewmodel

import android.arch.lifecycle.ViewModel
import br.com.chucknorrisfacts.home.entry.SearchFactsEntryModel
import br.com.chucknorrisfacts.home.handler.FactHandler
import br.com.chucknorrisfacts.home.service.HomeService
import io.reactivex.Observable

/**
 * @rodrigohsb
 */
class HomeViewModel(private val homeService: HomeService,
                    private val factHandler: FactHandler): ViewModel() {

    fun searchContent(query: String): Observable<SearchFactsEntryModel> {
            return homeService
                    .searchFacts(query)
                        .map { factHandler.convert(it,query) }
    }

}