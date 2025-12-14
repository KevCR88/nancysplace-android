package com.example.nancysplace.Data

import com.example.nancysplace.Entity.Product

object ProductRepository {

    // Lista de productos temporal (simulando base de datos)
    val productos = mutableListOf<Product>()

    fun agregarProducto(producto: Product) {
        productos.add(producto)
    }

    fun obtenerProductosPorCategoria(categoria: String): List<Product> {
        return productos.filter { it.categoria == categoria }
    }
}