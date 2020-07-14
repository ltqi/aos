package com.aos.app.dto

/**
 * Created by:  qiliantao on 2020.06.22 12:27
 * email     :  qiliantao@mockuai.com
 * Describe  :
 */
data class AResponse<out T>(val errorCode: Int, val errorMsg: String, val data: T?)

