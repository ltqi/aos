package com.aos.app.net.mk

import com.aos.app.App
import com.aos.app.ut.saveAccessToken
import com.aos.app.ut.saveRefreshToken
import com.aos.app.ut.saveSessionInfo
import com.q.lib.BuildConfig
import com.q.net.BaseRetrofitClient
import com.q.net.RequestMethod
import com.q.net.okhttp.MFormBody
import com.q.util.body2String
import com.q.util.formatJson
import com.q.util.i
import okhttp3.*
import okio.Buffer
import retrofit2.Retrofit
import java.util.HashMap


/**
 * Created by:  qiliantao on 2020.06.22 11:52
 * email     :  qiliantao@mockuai.com
 * Describe  :  Retrofit okHttpClient封装
 */
object MRetrofit : BaseRetrofitClient() {

    val refreshTokenLock = Any()

    private val buffer = Buffer()
    override fun builderOkHttpClient(builder: OkHttpClient.Builder) {
        builder.addInterceptor(InjectCommonParamsInterceptor())
        if (BuildConfig.DEBUG) {
            builder.addInterceptor {
                val request = it.request()
                val t1 = System.nanoTime()
                buffer.clear()
                request.body?.writeTo(buffer)
                i(" =============================Request=======================" +
                            String.format(" \n %s to %s%n%s Body = %s", request.url, it.connection(), request.headers, formatJson(buffer.readUtf8())))
                val response = it.proceed(request)
                val t2 = System.nanoTime()
                i(" =============================Response======================= " +
                            String.format(" \n from %s spend %.1fms%n%s Body = %s", response.request.url, (t2 - t1) / 1e6, response.headers, response.body?.body2String()))
                response
            }
        }
        builder.addInterceptor {
            val originRequest = it.request()

            val response = it.proceed(originRequest)
            if (response.isSuccessful) {
                val result = GsonHelper.fromJson<MKResult<String?>>(response.body?.body2String(), GsonHelper.findType(
                    MKResult::class.java, String::class.java))
                when(result?.code){
                    ServerCode.SESSION_TOKEN_INVALID -> {
                        synchronized(refreshTokenLock) {
                            val sessionTokenResult = MRetrofit.getApiService<ApiMKService>().refreshSessionToken()
                            val sessionToken = sessionTokenResult.data?.sessionToken
                            if(sessionTokenResult.isSuccess && sessionToken != null){
                                App.sp.saveSessionInfo(sessionToken)
                                val requestBuilder = originRequest.newBuilder().injectParams(originRequest)
                                return@addInterceptor it.proceed(requestBuilder.build())
                            } else {
                                return@addInterceptor Response.Builder().newBuilder(response).message("获取sessionToken失败").build()
                            }
                        }
                    }
                    ServerCode.ACCESS_TOKEN_INVALID -> {
                        synchronized(refreshTokenLock) {
                            val accessTokenResult = MRetrofit.getApiService<ApiMKService>().refreshAccessToken()
                            val accessToken = accessTokenResult.accessToken
                            val refreshToken = accessTokenResult.refreshToken
                            if(accessTokenResult.isSuccess && accessToken != null && refreshToken != null){
                                App.sp.saveAccessToken(accessToken)
                                App.sp.saveRefreshToken(refreshToken)
                                val requestBuilder = originRequest.newBuilder().injectParams(originRequest)
                                return@addInterceptor it.proceed(requestBuilder.build())
                            } else {
                                return@addInterceptor  Response.Builder().newBuilder(response).message("获取accessToken失败").build()
                            }
                        }
                    }
                    ServerCode.REFRESH_TOKEN_INVALID-> {
                        return@addInterceptor Response.Builder().newBuilder(response).message("token已失效，请重新登录").build()
                    }
                    ServerCode.REQUEST_TIME_OUT ->{
                        return@addInterceptor Response.Builder().newBuilder(response).message("请求超时").build()
                    }
                }
            }

            response
        }
    }

    override fun builderRetrofit(builder: Retrofit.Builder) {}


}

class InjectCommonParamsInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originRequest = chain.request()
        val requestBuilder = originRequest.newBuilder().injectParams(originRequest)
        return chain.proceed(requestBuilder.build())
    }
}

fun Request.Builder.injectParams(originRequest: Request) : Request.Builder {

    val urlBuilder = originRequest.url.newBuilder()
    val commonParams = CommonRequestParams.commonParams
    when (originRequest.method) {
        RequestMethod.GET -> {
            val iterator = commonParams.iterator()
            while (iterator.hasNext()) {
                val next = iterator.next()
                urlBuilder.addQueryParameter(next.key, next.value)
            }
            url(urlBuilder.build())
        }
        RequestMethod.POST -> {
            when (val requestBody = originRequest.body) {
                is MFormBody -> {
                    addCommonParams(requestBody, commonParams)
                }
                is FormBody -> {
                    addCommonParams(requestBody, commonParams)
                }
                is MultipartBody -> {
                    val partBodyList = requestBody.parts
                    val bodyBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)
                    partBodyList.forEach {
                        bodyBuilder.addPart(it)
                    }
                    commonParams.forEach {
                        bodyBuilder.addFormDataPart(it.key, it.value)
                    }
                    post(bodyBuilder.build())
                }
            }

        }
    }
    return this
}

private fun Request.Builder.addCommonParams(
    requestBody: MFormBody,
    commonParams: HashMap<String, String>
) {
    val bodyBuilder = MFormBody.Builder()
    for (i in 0 until requestBody.size) {
        bodyBuilder.add(
            requestBody.name(i),
            requestBody.value(i)
        )
    }
    val iterator = commonParams.iterator()
    while (iterator.hasNext()) {
        val next = iterator.next()
        bodyBuilder.add(next.key, next.value)
    }
    post(bodyBuilder.build())
}

private fun Request.Builder.addCommonParams(
    requestBody: FormBody,
    commonParams: HashMap<String, String>
) {
    val bodyBuilder = MFormBody.Builder()
    for (i in 0 until requestBody.size) {
        bodyBuilder.add(
            requestBody.name(i),
            requestBody.value(i)
        )
    }
    val iterator = commonParams.iterator()
    while (iterator.hasNext()) {
        val next = iterator.next()
        bodyBuilder.add(next.key, next.value)
    }
    post(bodyBuilder.build())
}

fun Response.Builder.newBuilder(response: Response) : Response.Builder {
    request(response.request)
    protocol(response.protocol)
    code(response.code)
    message(response.message)
    handshake(response.handshake)
    headers(response.headers)
    body(response.body)
    networkResponse(response.networkResponse)
    cacheResponse(response.cacheResponse)
    priorResponse(response.priorResponse)
    sentRequestAtMillis(response.sentRequestAtMillis)
    receivedResponseAtMillis(response.receivedResponseAtMillis)
    return this
}