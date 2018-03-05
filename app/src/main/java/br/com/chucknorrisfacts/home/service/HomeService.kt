package br.com.chucknorrisfacts.home.service

import br.com.chucknorrisfacts.webservice.WebServiceAPI
import br.com.chucknorrisfacts.webservice.payload.SearchPayload
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * @rodrigohsb
 */
class HomeService(private val webServiceAPI: WebServiceAPI) {

    fun searchFacts(query: String) : Flowable<SearchPayload> {
        return webServiceAPI.getFactBySearch(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}