package br.com.chucknorrisfacts.home.viewmodel

import android.arch.lifecycle.ViewModel
import br.com.chucknorrisfacts.home.handler.FactHandler
import br.com.chucknorrisfacts.home.handler.StateHandler
import br.com.chucknorrisfacts.home.service.HomeService
import br.com.chucknorrisfacts.home.status.State
import io.reactivex.Observable

/**
 * @rodrigohsb
 */
class HomeViewModel(private val homeService: HomeService,
                    private val factHandler: FactHandler) : ViewModel() {

    fun fetchState(): Observable<State> = Observable.just(State.WaitingForInput())

    fun searchContent(query: String): Observable<State> {

        return homeService
                .searchFacts(query)
                .map { factHandler.convert(it, query) }
                .compose(StateHandler())
                .startWith(State.Loading())
    }
}