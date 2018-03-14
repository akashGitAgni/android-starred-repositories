package com.android.repos.network

import android.annotation.SuppressLint
import android.app.Application
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit

object AppNetwork {

    /**
     * Maximum time in milliseconds to wait while connecting.
     */
    private const val TIMEOUT_CONNECT = 20000

    /**
     * Maximum time in milliseconds to wait for an InputStream read to complete.
     */
    private const val TIMEOUT_READ = 30000

    var client: OkHttpClient

    @SuppressLint("StaticFieldLeak")
    lateinit var picasso: Picasso

    init {
        client = initClient()
    }

    fun initPicasso(application: Application) {
        picasso = Picasso.Builder(application)
                .downloader(OkHttp3Downloader(client))
                .listener { _, _, exception ->
                    exception.printStackTrace()
                }.build()

        picasso.setIndicatorsEnabled(true)
        picasso.isLoggingEnabled = true
    }

    private fun initClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message ->
            Timber.tag("RenaOkHttp").d(message)
        })

        val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .followRedirects(true)
                .followSslRedirects(true)
                .retryOnConnectionFailure(true)
                .cache(null)
                .connectTimeout(TIMEOUT_CONNECT.toLong(), TimeUnit.MILLISECONDS)
                .writeTimeout(TIMEOUT_CONNECT.toLong(), TimeUnit.MILLISECONDS)
                .readTimeout(TIMEOUT_READ.toLong(), TimeUnit.MILLISECONDS)
                .hostnameVerifier { hostname, session -> true; }

        return client.build()
    }

    private const val BASE_URL = "https://api.github.com/search/"

    val retrofit: Retrofit by lazy {
        initRetrofit();
    }

    private fun initRetrofit(): Retrofit {
        val builder = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(NullOnEmptyConverterFactory())
                .addConverterFactory(GsonConverterFactory.create())

        return builder.build()
    }
}