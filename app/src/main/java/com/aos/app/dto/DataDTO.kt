package com.aos.app.dto

/**
 * Created by:  qiliantao on 2020.04.25 16:39
 * email     :  qiliantao@mockuai.com
 * Describe  :
 */
data class Book(var name:String, val author: String) {

    override fun toString(): String {
        return name + "by $author"
    }
}