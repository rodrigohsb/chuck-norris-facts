package br.com.chucknorrisfacts.webservice

import br.com.chucknorrisfacts.webservice.exceptions.*
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.net.SocketException
import java.util.concurrent.TimeoutException

/**
 * @rodrigohsb
 */
class RequestInterceptor constructor(private val networkService: NetworkService): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        if (isNotConnected()) throw NoNetworkException()

        return try {
            val request = chain.request()
            val response = chain.proceed(request)

            val responseCode = response.code()
            val requestEndpoint = request.url().toString()
            val errorMsg = { "API $responseCode @ $requestEndpoint" }

            when (response.code()) {
                400 -> throw BadRequestException(errorMsg())
                404 -> throw NoDataException()
                401,402,403,in 400..499 -> throw Error4XXException(errorMsg())
                in 500..599 -> throw Error5XXException(errorMsg())
                else -> response
            }
        } catch (e: Exception) {

            when(e) {
                is IOException, is SocketException -> throw TimeoutException()
                else -> throw e
            }
        }
    }

    private fun isNotConnected() = !networkService.isConnected
}