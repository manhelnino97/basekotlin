package com.omi.pharmacy.manager.network

import com.omi.pharmacy.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by tinhvv on 10/24/18.
 */
class NetworkManager {
    companion object {
        val shared = NetworkManager()
    }

    private val httpClient by lazy {
        val httpClientBuilder = OkHttpClient.Builder()

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE

        val headerInterceptor = Interceptor{ chain ->
            val builder = chain.request().newBuilder()
            // Add header here
            val request = builder.build()
            chain.proceed(request)
        }

        httpClientBuilder.pingInterval(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .addNetworkInterceptor(headerInterceptor)


        httpClientBuilder.build()
    }

    private val retrofit by lazy {
        val builder = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("")
                .client(httpClient)
        builder.build()
    }

    lateinit var accountAPI: AccountAPI


    init {
        createAccountAPI()
    }



    private fun createAccountAPI(){
        accountAPI = retrofit.create(AccountAPI::class.java)
    }
}