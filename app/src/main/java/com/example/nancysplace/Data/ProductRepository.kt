package com.example.nancysplace.Data

import com.example.nancysplace.Entity.Product

object ProductRepository {


    val productos = mutableListOf<Product>()

    fun agregarProducto(producto: Product) {
        productos.add(producto)
    }

    fun obtenerProductosPorCategoria(categoria: String): List<Product> {
        return productos.filter { it.categoria == categoria }
    }
}