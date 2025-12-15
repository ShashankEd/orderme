package com.product.orderfromhere.model

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    public  val id: String?,
    public  val title: String?,
    public  val brand: String?,
    public  val description: String?
)
