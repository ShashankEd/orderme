package com.product.orderfromhere.model

data class Credentials(
    var login: String = "",
    var pwd: String = "",
    var remember: Boolean = false
) {
    fun isNotEmpty(): Boolean {
        return login.isNotEmpty() && pwd.isNotEmpty()
    }
}