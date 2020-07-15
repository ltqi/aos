package com.aos.app.ut

import android.Manifest
import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Point
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.provider.Settings
import android.telephony.TelephonyManager
import android.text.format.Formatter
import android.view.WindowManager
import androidx.core.content.ContextCompat
import java.io.UnsupportedEncodingException
import java.util.*

/**
 * Describe  :
 */

fun Context.getTelephonyManager(): TelephonyManager? {
    return applicationContext.getSystemService(Context.TELEPHONY_SERVICE) as? TelephonyManager
}

fun Context.getWifiManager(): WifiManager? {
    return applicationContext.getSystemService(Context.WIFI_SERVICE) as? WifiManager
}

fun Context.getWindowManager(): WindowManager? {
    return applicationContext.getSystemService(Context.WINDOW_SERVICE) as? WindowManager
}

fun Context.getActivityManager(): ActivityManager? {
    return getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager
}

fun Context.checkPermission(permission: String): Boolean {
    return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
}

@SuppressLint("MissingPermission", "HardwareIds")
fun Context.getDeviceIMEI(): String {

    if (!checkPermission(Manifest.permission.READ_PHONE_STATE)) {
        return Strings.EMPTY
    }
    val IMEI = getTelephonyManager()?.deviceId ?: Strings.EMPTY
    return if (IMEI == "000000000000000") Strings.EMPTY else IMEI
}

@SuppressLint("MissingPermission", "HardwareIds")
fun Context.getDeviceIMSI(): String {
    if (!checkPermission(Manifest.permission.READ_PHONE_STATE)) {
        return Strings.EMPTY
    }
    return getTelephonyManager()?.subscriberId ?: Strings.EMPTY
}


fun Context.getDeviceDisplaySize(): Point {
    return Point().also { getWindowManager()?.defaultDisplay?.getSize(it) }
}

fun Context.getDeviceDisplayWidth(): Int {
    return getDeviceDisplaySize().x
}

fun Context.getDeviceDisplayHeight(): Int {
    return getDeviceDisplaySize().y
}

fun Context.getDeviceResolution(concatChar: Char = Chars.ASTERISK): String {
    val point = getDeviceDisplaySize()
    return "${point.y}$concatChar${point.x}"
}

/**
 * 获取手机号
 */
@SuppressLint("HardwareIds", "MissingPermission")
fun Context.getDevicePhoneNumber(): String {
    if (!checkPermission(Manifest.permission.READ_PHONE_STATE)) {
        return Strings.EMPTY
    }
    return getTelephonyManager()?.line1Number ?: Strings.EMPTY
}

/**
 * 获取手机网络运营商信息
 */
fun Context.getDeviceProviderName(): String {
    val IMSI = getDeviceIMSI()
    return when {
        IMSI.startsWith("46000") || IMSI.startsWith("46002") -> "中国移动"
        IMSI.startsWith("46001") -> "中国联通"
        IMSI.startsWith("46003") -> "中国电信"
        else -> Strings.EMPTY
    }
}

@SuppressLint("HardwareIds")
fun Context.getDeviceAndroidId(): String {
    val androidId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
    return if ("9774d56d682e549c" == androidId) Strings.EMPTY else androidId
}

@SuppressLint("MissingPermission", "HardwareIds")
fun Context.getDeviceWifiMacAddress(): String {
    if (!checkPermission(Manifest.permission.ACCESS_WIFI_STATE)) {
        return Strings.EMPTY
    }
    return getWifiManager()?.connectionInfo?.macAddress ?: Strings.EMPTY
}


/**
 * 获得机身内存总大小
 */
fun Context.getDeviceMemTotalSize(): Long {
    val info = ActivityManager.MemoryInfo()
    getActivityManager()?.getMemoryInfo(info)
    return info.totalMem shr 10
}

fun Context.getDeviceMemTotalSizeFormatString(): String {
    return Formatter.formatFileSize(this, getDeviceMemTotalSize())
}

/**
 * 获取设备可用内存大小
 */
fun Context.getDeviceMemAvailSize(): Long {
    val info = ActivityManager.MemoryInfo()
    getActivityManager()?.getMemoryInfo(info)
    return info.availMem shr 10
}

fun Context.getDeviceMemAvailSizeFormatString(): String {
    return Formatter.formatFileSize(this, getDeviceMemAvailSize())
}


/**
 * 获得SD卡总大小
 */
fun Context.getDeviceSDCardTotalSize(): Long {
    if (!checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
        return 0
    }
    val path = Environment.getExternalStorageDirectory()
    val stat = StatFs(path.path)
    val blockSize = stat.blockSize.toLong()
    val totalBlocks = stat.blockCount.toLong()
    return blockSize * totalBlocks
}

fun Context.getDeviceSDCardTotalSizeFormatString(): String {
    return Formatter.formatFileSize(this, getDeviceSDCardTotalSize())
}

/**
 * 获得sd卡剩余容量，即可用大小
 */
fun Context.getDeviceSDCardAvailSize(): Long {
    if (!checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
        return 0
    }
    val path = Environment.getExternalStorageDirectory()
    val stat = StatFs(path.path)
    val blockSize = stat.blockSize.toLong()
    val availableBlocks = stat.availableBlocks.toLong()
    return blockSize * availableBlocks
}

fun Context.getDeviceSDCardAvailSizeFormatString(): String {
    return Formatter.formatFileSize(this, getDeviceSDCardAvailSize())
}


fun Context.getDeviceUUID(): String {
    var uuid: String? = null
    val androidId = getDeviceAndroidId()
    val imei = this.getDeviceIMEI()
    val imsi = this.getDeviceIMSI()

    var firstId: String? = null
    var secondId: String? = null
    if (!androidId.isNullOrBlank()) {
        firstId = androidId
    } else {
        if (!imsi.isNullOrBlank()) {
            firstId = imsi
        }
    }
    if (!imei.isNullOrBlank()) {
        secondId = imei
    } else {
        if (androidId.isNullOrBlank()) {
            val macAddr = getDeviceWifiMacAddress()
            if (!macAddr.isNullOrBlank()) {
                secondId = macAddr
            }
        }
    }

    if (!firstId.isNullOrBlank() || !secondId.isNullOrBlank()) {
        try {
            val fullId = firstId + secondId
            uuid = UUID.nameUUIDFromBytes(fullId.toByteArray(charset("utf8"))).toString()
        } catch (e: UnsupportedEncodingException) {
        }
    }

    if (uuid.isNullOrBlank()) {
        uuid = UUID.randomUUID().toString()
    }

    return uuid
}

object DeviceUtils {

    /**
     * 获取手机品牌及型号
     */
    @JvmStatic
    fun getModel(): String {
        return Build.MODEL
    }

    /**
     * 获取设备制造商
     */
    @JvmStatic
    fun getManufacturer(): String {
        return Build.MANUFACTURER
    }

    @JvmStatic
    fun getSystemSDKInt(): Int {
        return Build.VERSION.SDK_INT
    }

    @JvmStatic
    fun getSystemVersion(): String {
        return Build.VERSION.RELEASE
    }
}

class Strings {
    companion object {
        const val EMPTY = ""
        const val ELLIPSIS = "..." //省略号
    }
}

object Chars {
    const val BLANK = ' ' //空格
    const val COLON = ':'//冒号
    const val SEMICOLON = ';' //分号
    const val COMMA = ',' //英文逗号
    const val POINT = '.'//句号或小数点
    const val QUESTION_MARK = '?' //问号
    const val AND = '&' //问号
    const val OR = '|' //问号
    const val SLASH_FORWARD = '/' //正斜杠 左斜杠
    const val SLASH_BACK = '\\' //反斜杠 右斜杠
    const val HYPHEN = '-' //连字符
    const val UNDERLINE = '_'//下划线
    const val AT = '@'
    const val ASTERISK = '*'

    const val BRACKET_ROUND_LEFT = '('
    const val BRACKET_ROUND_RIGHT = ')'
    const val BRACKET_SQUARE_LEFT = '['
    const val BRACKET_SQUARE_RIGHT = ']'
    const val BRACKET_LEFT = '{'
    const val BRACKET_RIGHT = '}'
    const val QUOTE = '"'
    const val SINGLE_QUOTE = '\''
    const val EXCLAMATION_MARK = '!'
    const val RMB = '¥'
    const val DOLLAR = '$'
    const val PERCENT = '%'
}