package br.com.chucknorrisfacts.home.service

import br.com.chucknorrisfacts.home.status.SchedulerHandler
import br.com.chucknorrisfacts.webservice.WebServiceAPI
import br.com.chucknorrisfacts.webservice.payload.SearchPayload
import io.reactivex.Observable

/**
 * @rodrigohsb
 */
class HomeService(private val webServiceAPI: WebServiceAPI) {

    fun searchFacts(query: String) : Observable<SearchPayload> =
            webServiceAPI.getFactBySearch(query)
                    .compose(SchedulerHandler<SearchPayload>())
}