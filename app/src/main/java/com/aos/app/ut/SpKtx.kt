package com.aos.app.ut

import android.content.SharedPreferences
import com.q.net.GSON

/**
 * Created by:  qiliantao on 2020.07.14 12:51
 * email     :  qiliantao@mockuai.com
 * Describe  :
 */
fun SharedPreferences.put(key: String, value: Any): SharedPreferences {
    val edit = edit()
    when (value) {
        is Int -> edit.putInt(key, value)
        is Long -> edit.putLong(key, value)
        is Float -> edit.putFloat(key, value)
        is Boolean -> edit.putBoolean(key, value)
        is String? -> edit.putString(key, value)
        else -> edit.putString(key, GSON.toJson(value))
    }
    edit.commit()
    return this
}


inline fun <reified T : Any> SharedPreferences.get(key: String, default: Long = 0): T {
    val kClass = T::class.java
    return when (kClass) {
        Int::class.java -> {
            getInt(key, default.toInt()) as T
        }
        Long::class.java -> {
            getLong(key, default) as T
        }
        Float::class.java -> {
            getFloat(key, default.toFloat()) as T
        }
        Boolean::class.java -> {
            getBoolean(key, false) as T
        }
        else -> {
            getString(key, "") as T
        }
    }
}

val KEY_SESSION_TOKEN = "session_token"
val KEY_ACCESS_TOKEN = "access_token"
val KEY_REFRESH_TOKEN = "refresh_token"

fun SharedPreferences.getAccessToken(): String {
    return getString(KEY_ACCESS_TOKEN, "") ?: ""
}

fun SharedPreferences.getRefreshToken(): String? {
    return getString(KEY_REFRESH_TOKEN, null)
}

fun SharedPreferences.getSessionToken(): String {
    return getString(KEY_SESSION_TOKEN, "-") ?: "-"
}

fun SharedPreferences.saveAccessToken(accessToken: String) {
    put(KEY_ACCESS_TOKEN, accessToken)
}

fun SharedPreferences.saveRefreshToken(refreshToken: String) {
    put(KEY_REFRESH_TOKEN, refreshToken)
}

fun SharedPreferences.saveSessionInfo(sessionToken: String) {
    put(KEY_SESSION_TOKEN, sessionToken)
}

