package com.q.net

import com.q.net.okhttp.MFormBody
import okhttp3.*
import java.util.HashMap

/**
 * Created by:  qiliantao on 2020.07.22 16:12
 * email     :  qiliantao@mockuai.com
 * Describe  :
 */

class InjectCommonParamsInterceptor(var commonParams: HashMap<String, String>) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originRequest = chain.request()
        val requestBuilder = originRequest.newBuilder().injectParams(originRequest, commonParams)
        return chain.proceed(requestBuilder.build())
    }
}

fun Request.Builder.injectParams(originRequest: Request, commonParams: HashMap<String, String>) : Request.Builder {

    val urlBuilder = originRequest.url.newBuilder()
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