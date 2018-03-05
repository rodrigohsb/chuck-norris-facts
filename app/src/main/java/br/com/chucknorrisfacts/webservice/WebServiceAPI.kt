package br.com.chucknorrisfacts.webservice

import br.com.chucknorrisfacts.webservice.payload.SearchPayload
import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @rodrigohsb
 */
interface WebServiceAPI {

    @GET("search")
    fun getFactBySearch(@Query("query") query: String): Flowable<SearchPayload>

}