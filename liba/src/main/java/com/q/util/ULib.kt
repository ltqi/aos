package com.q.util

import android.app.Activity
import android.content.Context
import android.content.Context.TELEPHONY_SERVICE
import android.telephony.TelephonyManager
import android.util.DisplayMetrics
import android.util.Log
import okhttp3.ResponseBody
import java.lang.reflect.Method


var appVersion = "1.0"
var IMEI = ""

fun dp2pxF(context: Context, dipValue: Float): Float {
    val scale = context.resources.displayMetrics.density
    return (dipValue * scale + 0.5f)
}


fun getScreenWidth(context: Context): Int {
    val displayMetrics = context.resources.displayMetrics
    return displayMetrics.widthPixels
}

/** 获得状态栏的高度  */
fun getStatusHeight(context: Context): Int {
    var statusHeight = -1
    try {
        val clazz = Class.forName("com.android.internal.R\$dimen")
        val `object` = clazz.newInstance()
        val height = Integer.parseInt(clazz.getField("status_bar_height").get(`object`).toString())
        statusHeight = context.resources.getDimensionPixelSize(height)
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return statusHeight
}

fun getScreenPix(activity: Activity): DisplayMetrics {
    val displaysMetrics = DisplayMetrics()
    activity.windowManager.defaultDisplay.getMetrics(displaysMetrics)
    return displaysMetrics
}

fun <T> isEmpty(list: List<T>?): Boolean {
    return list == null || list.isEmpty()
}

// 将px值转换为dip或dp值
fun px2dp(context: Context, pxValue: Float): Int {
    val scale = context.resources.displayMetrics.density
    return (pxValue / scale + 0.5f).toInt()
}

// 将dip或dp值转换为px值
fun dp2px(context: Context, dipValue: Float): Int {
    val scale = context.resources.displayMetrics.density
    return (dipValue * scale + 0.5f).toInt()
}

// 将px值转换为sp值
fun px2sp(context: Context, pxValue: Float): Int {
    val fontScale = context.resources.displayMetrics.scaledDensity
    return (pxValue / fontScale + 0.5f).toInt()
}

// 将sp值转换为px值
fun sp2px(context: Context, spValue: Float): Int {
    val fontScale = context.resources.displayMetrics.scaledDensity
    return (spValue * fontScale + 0.5f).toInt()
}

// 屏幕宽度（像素）
fun getWindowWidth(context: Activity): Int {
    val metric = DisplayMetrics()
    context.windowManager.defaultDisplay.getMetrics(metric)
    return metric.widthPixels
}

// 屏幕高度（像素）
fun getWindowHeight(context: Activity): Int {
    val metric = DisplayMetrics()
    context.windowManager.defaultDisplay.getMetrics(metric)
    return metric.heightPixels
}

// 根据Unicode编码判断中文汉字和符号
private fun isChinese(c: Char): Boolean {
    val ub = Character.UnicodeBlock.of(c)
    return (ub === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub === Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
            || ub === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
            || ub === Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub === Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
            || ub === Character.UnicodeBlock.GENERAL_PUNCTUATION)
}

// 判断中文汉字和符号
fun isChinese(strName: String): Boolean {
    val ch = strName.toCharArray()
    for (i in ch.indices) {
        val c = ch[i]
        if (isChinese(c)) {
            return true
        }
    }
    return false
}


fun formatJson(jsonStr: String?): String {
    if (null == jsonStr || "" == jsonStr)
        return ""
    val sb = StringBuilder()
    var last: Char
    var current = '\u0000'
    var indent = 0
    var isInQuotationMarks = false
    for (i in 0 until jsonStr.length) {
        last = current
        current = jsonStr[i]
        when (current) {
            '"' -> {
                if (last != '\\') {
                    isInQuotationMarks = !isInQuotationMarks
                }
                sb.append(current)
            }
            '{', '[' -> {
                sb.append(current)
                if (!isInQuotationMarks) {
                    sb.append('\n')
                    indent++
                    addIndentBlank(sb, indent)
                }
            }
            '}', ']' -> {
                if (!isInQuotationMarks) {
                    sb.append('\n')
                    indent--
                    addIndentBlank(sb, indent)
                }
                sb.append(current)
            }
            ',' -> {
                sb.append(current)
                if (last != '\\' && !isInQuotationMarks) {
                    sb.append('\n')
                    addIndentBlank(sb, indent)
                }
            }
            else -> sb.append(current)
        }
    }

    return sb.toString()
}

/**
 * 添加space
 */
private fun addIndentBlank(sb: StringBuilder, indent: Int) {
    for (i in 0 until indent) {
        sb.append('\t')
    }
}
//解决response.body().string();只能打印一次
fun ResponseBody.body2String() : String {
    val source = source()
    source.request(java.lang.Long.MAX_VALUE)
    return formatJson(source.buffer.clone().readUtf8())
}


//Log-----------------------------------------------------------------------

fun i(msg: String?) {
    i("OKTGS", msg)
}

fun i(tag: String?, msgs: String?) {
    var msg = msgs ?: ""
    if (tag == null || tag.isEmpty() || msg.isEmpty())
        return

    val segmentSize = 3 * 1024

    val length = msg.length.toLong()
    if (length <= segmentSize) {// 长度小于等于限制直接打印
        Log.i(tag, msg)
    } else {
        while (msg.length > segmentSize) {// 循环分段打印日志
            val logContent = msg.substring(0, segmentSize)
            Log.i(tag, logContent)
            msg = msg.substring(logContent.length)
        }
        Log.i(tag, msg)// 打印剩余日志
    }
}

fun getImei(context: Context, first: Int = 0) : String {
    val manager =  context.getSystemService(TELEPHONY_SERVICE) as TelephonyManager
    var clazz = manager.javaClass
    var imei = ""
    try {
        val getImei = clazz.getDeclaredMethod("getImei",Int::class.java) as Method //(int slotId)
        getImei.setAccessible(true)
        imei =  getImei.invoke(manager,if (first > 1) 1 else 0) as String
    } catch (e: Exception) {
    }
    return imei
}
