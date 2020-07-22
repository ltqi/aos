package com.q.util

import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import com.q.net.GSON
import java.lang.reflect.Type

/**
 * Created by yuchengren on 2018/12/17.
 */
object GsonHelper {

    var gson: Gson = GSON
    var gsonParser: JsonParser = JsonParser()

     fun toJson(any: Any?): String? {
        try {
            return gson.toJson(any?: return null)
        } catch (e: Exception) {
//            printExceptionLog(e, "Any toJson")
        }
        return null
    }

     fun <T> fromJson(json: String?, type: Type?): T? {
        try {
            return gson.fromJson<T>(json, type)
        } catch (e: Exception) {
//            printExceptionLog(e, "String fromJson")
        }
        return null
    }


     fun findType(rawType: Type, vararg typeArguments: Type): Type {
        return TypeToken.getParameterized(rawType,*typeArguments).type
    }

}