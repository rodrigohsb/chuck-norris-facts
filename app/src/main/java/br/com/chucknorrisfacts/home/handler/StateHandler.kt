package br.com.chucknorrisfacts.home.handler

import br.com.chucknorrisfacts.home.entry.SearchFactsEntryModel
import br.com.chucknorrisfacts.home.status.State
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer

/**
 * @rodrigohsb
 */
class StateHandler: ObservableTransformer<Any, State> {
    override fun apply(upstream: Observable<Any>): ObservableSource<State> {
        return upstream
                .map {
                    State.Sucess(it as SearchFactsEntryModel) as State
                }
                .onErrorResumeNext { error: Throwable ->
                    Observable.just(State.Error(error as Exception))
                }
    }

}