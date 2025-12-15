package com.product.orderfromhere.model

import kotlinx.serialization.Serializable

// Note: This data class will be used for the navigation param
@Serializable
data class User(
    val username: String?,
    val password: String?,
    val isAdmin: Boolean? = false
)
