package com.aos.app.net.mk

/**
 */
object ServerCode {
    const val SUCCESS = "10000"
    const val SESSION_TOKEN_INVALID = "50002" //session_token无效或已过期 , 重新获取session_token
    const val ACCESS_TOKEN_INVALID = "50004" //access_token无效或已过期，重新换取最新的access_token
    const val ACCESS_TOKEN_INVALID_NEW =
        "50003" //access_token无效或已过期，重新换取最新的access_token
    const val REFRESH_TOKEN_INVALID = "50006" //refresh_token无效或已过期 , 需要重新登陆
    const val USER_ROLE_LIMIT = "50009" //用户角色权限出错,可能是本地数据与服务端不同步导致,需重新登录
    const val REQUEST_TIME_OUT = "40003" //请求超时

    /**
     * 商品最多可以购买几件
     */
    const val BUY_LIMIT = "30014"

    /**  指定区域不发货
     */
    const val DELIVERY_OUT_OF_SERVICE = "30201"

    /**
     * 添加网红达到上限
     */
    const val ADD_ORGSELLER_MAX_LIMIT = "30001"
}