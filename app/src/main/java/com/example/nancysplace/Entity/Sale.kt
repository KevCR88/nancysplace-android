package com.example.nancysplace.Entity

data class Sale(
    val id: String = System.currentTimeMillis().toString(),
    val productId: Int,
    val productName: String,
    val quantity: Int,
    val unitPrice: Double,
    val date: Long = System.currentTimeMillis()
)