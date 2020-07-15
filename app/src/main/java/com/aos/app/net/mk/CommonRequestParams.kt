package com.aos.app.net.mk

import android.content.Context
import android.content.pm.PackageManager
import android.text.TextUtils
import com.aos.app.App
import com.aos.app.ut.DeviceUtils
import com.aos.app.ut.getAccessToken
import com.aos.app.ut.getSessionToken
import java.lang.String.CASE_INSENSITIVE_ORDER
import java.security.MessageDigest
import java.util.*

object CommonRequestParams {
    /**
     * 获取公有的参数
     *
     * @return
     */
    val commonParams: HashMap<String, String>
        get() {
            val params = HashMap<String, String>()
            try {
                params["app_key"] = appKey
                if (!TextUtils.isEmpty(App.sp.getAccessToken())) {
                    params["access_token"] = App.sp.getAccessToken()
                }
                params["session_token"] = App.sp.getSessionToken()
                params["format"] = "json"
                params["version"] = getApplicationVersion(App.application)
                params["phone_model"] = DeviceUtils.getModel()
                params["server_version"] = DeviceUtils.getSystemVersion()
            } catch (e: SecurityException) {
            }
            return params
        }

    fun getApiSign(params: Map<String, Any>): String? {
        return md5Encrypt(params, kSort(params))
    }

    fun getApplicationVersion(context: Context): String {
        return try {
            context.applicationContext.packageManager
                .getPackageInfo(context.packageName, 0).versionName
        } catch (e: PackageManager.NameNotFoundException) {
            ""
        }
    }

    private fun kSort(params: Map<String, Any>): List<String> {
        val keys = params.keys.sortedWith(CASE_INSENSITIVE_ORDER)
        return keys
    }

    private fun md5Encrypt(mapParams: Map<String, Any?>, keys: List<String>): String? {
        val signMap: MutableMap<String, String> =
            TreeMap()
        for ((key, value) in mapParams) {
            signMap[key] = value?.toString() ?: ""
        }
        val paramSb = StringBuilder()
        for ((key, value) in signMap) {
            paramSb.append(key)
            paramSb.append("=")
            paramSb.append(value)
            paramSb.append("&")
        }
        paramSb.deleteCharAt(paramSb.length - 1)
        var params = ""
        params = appPwd + paramSb + appPwd
        return MD5(params)
    }

    fun MD5(s: String): String? {
        val hexDigits = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F')
        return try {
            val btInput = s.toByteArray()
            // 获得MD5摘要算法的 MessageDigest 对象
            val mdInst = MessageDigest.getInstance("MD5")
            // 使用指定的字节更新摘要
            mdInst.update(btInput)
            // 获得密文
            val md = mdInst.digest()
            // 把密文转换成十六进制的字符串形式
            val j = md.size
            val str = CharArray(j * 2)
            var k = 0
            for (i in 0 until j) {
                val byte0 = md[i]
                str[k++] = hexDigits[byte0.toInt() ushr 4 and 0xf]
                str[k++] = hexDigits[byte0.toInt() and 0xf]
            }
            String(str).toLowerCase()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    val appKey = "d3274b1790ff13a6e000c0685856fe46"
    val appPwd = "c793a7942aed0e80ffc7be042731e1d2"

}