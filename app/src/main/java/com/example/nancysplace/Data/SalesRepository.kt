package com.example.nancysplace.Data

import com.example.nancysplace.Entity.Sale

object SalesRepository {

    private val ventas = mutableListOf<Sale>()

    fun registrarVenta(sale: Sale) {
        ventas.add(sale)
    }

    fun obtenerVentas(): List<Sale> = ventas.toList()
}