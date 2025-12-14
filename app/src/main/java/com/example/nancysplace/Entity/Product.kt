package com.example.nancysplace.Entity

data class Product(
    val id: Int? = null,
    var nombre: String,
    var descripcion: String,
    var precio: Double,
    var cantidad: Int,
    var categoria: String,
    val imagen_base64: String? = null
)