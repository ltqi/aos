package com.q.net

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.q.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by:  qiliantao on 2020.06.22 11:33
 * email     :  qiliantao@mockuai.com
 * Describe  :
 */
abstract class BaseRetrofitClient {

    companion object {
        const val HOST_S = "https://www.wanandroid.com"

        const val CONNECT_TIMEOUT = 20
        const val READ_TIMEOUT = CONNECT_TIMEOUT
    }

    protected val client: OkHttpClient
        get() {
            val builder = OkHttpClient.Builder()
            val logging = HttpLoggingInterceptor()
            if (BuildConfig.DEBUG) {
                logging.level = HttpLoggingInterceptor.Level.BODY
            } else {
                logging.level = HttpLoggingInterceptor.Level.BASIC
            }
            builder.addInterceptor(logging).connectTimeout(CONNECT_TIMEOUT.toLong(), TimeUnit.SECONDS)
//                .cache(Cache(cacheDir, cacheSize))
            builderOkHttpClient(builder)
            return builder.build()
        }

    protected abstract fun builderOkHttpClient(builder: OkHttpClient.Builder)

    inline fun <reified T> getApiService(baseUrl: String = HOST_S): T {
        return Retrofit.Builder()
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(GSON))
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .addCallAdapterFactory(CoroutineCallAdapterFactory.invoke())
            .baseUrl(baseUrl).apply {
                builderRetrofit(this)
            }
            .build().create(T::class.java)
    }

    protected abstract fun builderRetrofit(builder: Retrofit.Builder)


}

val GSON = GsonBuilder()
    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
    .serializeSpecialFloatingPointValues()
    .disableHtmlEscaping()
    .create()

object RequestMethod {
    const val GET = "GET"
    const val POST = "POST"
    const val PUT = "PUT"
}