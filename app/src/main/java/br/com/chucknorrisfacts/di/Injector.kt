package br.com.chucknorrisfacts.di

import android.content.Context
import android.net.ConnectivityManager
import br.com.chucknorrisfacts.BuildConfig
import br.com.chucknorrisfacts.home.service.HomeService
import br.com.chucknorrisfacts.webservice.NetworkService
import br.com.chucknorrisfacts.webservice.RequestInterceptor
import br.com.chucknorrisfacts.webservice.WebServiceAPI
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.provider
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * @rodrigohsb
 */
class Injector(context: Context) {

    val dependencies = Kodein.Module(allowSilentOverride = true) {

        bind<HomeService>() with provider {
            HomeService(webServiceAPI = instance())
        }

        bind<Interceptor>(tag = LOG_INTERCEPTOR) with provider {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            logging
        }

        bind<Interceptor>(tag = REQUEST_INTERCEPTOR) with provider {
            RequestInterceptor(networkService = instance())
        }

        bind<OkHttpClient>() with provider {
            val builder = OkHttpClient.Builder()
            builder.writeTimeout(30, TimeUnit.SECONDS)
            builder.readTimeout(30, TimeUnit.SECONDS)
            builder.addInterceptor(instance(REQUEST_INTERCEPTOR))
            if (BuildConfig.DEBUG) builder.addNetworkInterceptor(instance(LOG_INTERCEPTOR))
            builder.build()
        }

        bind<CallAdapter.Factory>() with provider {
            RxJava2CallAdapterFactory.create()
        }

        bind<Converter.Factory>() with provider {
            GsonConverterFactory.create()
        }

        bind<Retrofit>() with provider {
            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(instance())
                    .addConverterFactory(instance())
                    .client(instance())
                    .baseUrl("https://api.chucknorris.io/jokes/")

            retrofit.build()
        }

        bind<WebServiceAPI>() with provider {
            instance<Retrofit>().create(WebServiceAPI::class.java)
        }

        bind<ConnectivityManager>() with provider {
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        }

        bind<NetworkService>() with provider {
            NetworkService(connectivityManager = instance())
        }
    }

    companion object {
        private val LOG_INTERCEPTOR = "LogInterceptor"
        private val REQUEST_INTERCEPTOR = "RequestInterceptor"
    }
}