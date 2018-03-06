package br.com.chucknorrisfacts

import br.com.chucknorrisfacts.webservice.NetworkService
import br.com.chucknorrisfacts.webservice.RequestInterceptor
import br.com.chucknorrisfacts.webservice.exceptions.*
import com.nhaarman.mockito_kotlin.whenever
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.mockwebserver.MockWebServer
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations.initMocks
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.SocketPolicy
import java.util.concurrent.TimeUnit

/**
 * @rodrigohsb
 */
class RequestBehavior {
    
    private val mockWebServer by lazy { MockWebServer() }

    private lateinit var client:OkHttpClient

    @Mock
    private lateinit var networkService: NetworkService

    private lateinit var requestInterceptor: RequestInterceptor

    @Before
    fun setup(){
        initMocks(this)
        requestInterceptor = RequestInterceptor(networkService)

        val builder = OkHttpClient.Builder()
        builder.readTimeout(1, TimeUnit.SECONDS)
        builder.addInterceptor(requestInterceptor)
        client = builder.build()
    }

    @Test
    fun `internal_server_error_test`(){

        whenever(networkService.isConnected).thenReturn(true)

            mockWebServer.start()
            mockWebServer.enqueue(MockResponse().setResponseCode(500))

            val httpUrl = mockWebServer.url("/")



            val request = Request.Builder()
                    .url(httpUrl)
                    .build()
        try{
                client.newCall(request).execute()
            }catch (e:Error5XXException) {
                assert(true)
            }catch (e:Exception) {
                assert(false)
            }
    }

    @Test
    fun `bad_request_test`(){

        whenever(networkService.isConnected).thenReturn(true)

        mockWebServer.start()
        mockWebServer.enqueue(MockResponse().setResponseCode(400))

        val httpUrl = mockWebServer.url("/")

        val request = Request.Builder()
                .url(httpUrl)
                .build()
        try{
                client.newCall(request).execute()
            }catch (e: Error4XXException) {
                assert(true)
            }catch (e:Exception) {
                assert(false)
            }
    }

    @Test
    fun `no_network_test`(){

        whenever(networkService.isConnected).thenReturn(false)

        mockWebServer.start()
        mockWebServer.enqueue(MockResponse().setResponseCode(200))

        val httpUrl = mockWebServer.url("/")

        val request = Request.Builder()
                .url(httpUrl)
                .build()
        try{
                client.newCall(request).execute()
            }catch (e:NoNetworkException) {
                assert(true)
            }catch (e:Exception) {
                assert(false)
            }
    }

    @Test
    fun `no_data_available_test`(){

        whenever(networkService.isConnected).thenReturn(true)

        mockWebServer.start()
        mockWebServer.enqueue(MockResponse().setResponseCode(404))

        val httpUrl = mockWebServer.url("/")

        val request = Request.Builder()
                .url(httpUrl)
                .build()
        try{
                client.newCall(request).execute()
            }catch (e: NoDataException) {
                assert(true)
            }catch (e:Exception) {
                assert(false)
            }
    }

    @Test
    fun `success_test`(){

        whenever(networkService.isConnected).thenReturn(true)

        mockWebServer.start()
        mockWebServer.enqueue(MockResponse().setResponseCode(200))

        val httpUrl = mockWebServer.url("/")

        val request = Request.Builder()
                .url(httpUrl)
                .build()
        try{
                client.newCall(request).execute()
            }catch (e:Exception) {
                assert(false)
            }
    }

    @Test
    fun `timout_test`(){

        whenever(networkService.isConnected).thenReturn(true)

        mockWebServer.start()
        mockWebServer.enqueue(MockResponse().setSocketPolicy(SocketPolicy.NO_RESPONSE))

        val httpUrl = mockWebServer.url("/")

        val request = Request.Builder()
                .url(httpUrl)
                .build()
        try{
                client.newCall(request).execute()
            }catch (e: TimeoutException) {
                assert(true)
            }catch (e:Exception) {
                assert(false)
            }
    }

}