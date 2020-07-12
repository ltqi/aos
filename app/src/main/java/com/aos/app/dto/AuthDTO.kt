package com.aos.app.dto

/**
 * Created by:  qiliantao on 2020.06.22 12:27
 * email     :  qiliantao@mockuai.com
 * Describe  :
 */
data class WanResponse<out T>(val errorCode: Int, val errorMsg: String, val data: T)

data class UserInfo(val admin: Boolean, val chapterTops: List<String>, val collectIds: List<Int>,
                val email: String, val icon: String, val id: Int, val nickname: String,
                val password: String, val publicName: String, val token: String,
                val type: Int, val username: String){

    override fun equals(other: Any?): Boolean {
        return if (other is UserInfo){
            this.id == other.id
        }else false
    }
}
