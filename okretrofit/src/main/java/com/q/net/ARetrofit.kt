package com.q.net

import com.q.BuildConfig
import com.q.util.body2String
import com.q.util.formatJson
import com.q.util.i
import okhttp3.OkHttpClient
import okio.Buffer
import retrofit2.Retrofit

/**
 * Created by:  qiliantao on 2020.06.22 11:52
 * email     :  qiliantao@mockuai.com
 * Describe  :  Retrofit okHttpClient封装
 */
object ARetrofit : BaseRetrofitClient() {


    private val buffer = Buffer()
    override fun builderOkHttpClient(builder: OkHttpClient.Builder) {
        if (BuildConfig.DEBUG) {
            builder.addInterceptor {
                val request = it.request()
                val t1 = System.nanoTime()
                buffer.clear()
                request.body?.writeTo(buffer)
                i(
                    " =============================Request=======================" +
                            String.format(
                                " \n %s to %s%n%s Body = %s",
                                request.url,
                                it.connection(),
                                request.headers,
                                formatJson(buffer.readUtf8())
                            )
                )
                val response = it.proceed(request)
                val t2 = System.nanoTime()
                i(
                    " =============================Response======================= " +
                            String.format(
                                " \n from %s spend %.1fms%n%s Body = %s",
                                response.request.url,
                                (t2 - t1) / 1e6,
                                response.headers,
                                response.body?.body2String()
                            )
                )
                response
            }
        }
        builder.addInterceptor {
            val originRequest = it.request()
//            val requestBuilder = originRequest.newBuilder()
//            val urlBuilder = originRequest.url.newBuilder()
//            when (originRequest.method) {
//                RequestMethod.GET -> {
//
//                }
//                RequestMethod.POST -> {
//
//                }
//            }
            it.proceed(originRequest)
        }
    }

    override fun builderRetrofit(builder: Retrofit.Builder) {}
}
