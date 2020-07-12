package com.aos.app.ui.coroutines

import com.aos.app.dto.Book

/**
 */
object Api {

    fun request(url: String): Book {

        return Book("kotlin program technology at $url ", "qlt")
    }

}