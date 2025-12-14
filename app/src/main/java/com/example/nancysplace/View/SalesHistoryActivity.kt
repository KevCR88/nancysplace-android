package com.example.nancysplace.View

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nancysplace.Adapter.SaleAdapter
import com.example.nancysplace.Data.ApiClient
import com.example.nancysplace.R

class SalesHistoryActivity : AppCompatActivity() {


    private lateinit var txtTotalVentas: TextView
    private lateinit var txtMontoTotal: TextView
    private lateinit var txtProductoTop: TextView

    private lateinit var rvVentas: RecyclerView
    private lateinit var adapter: SaleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sales_history)

        txtTotalVentas = findViewById(R.id.txtTotalVentas)
        txtMontoTotal = findViewById(R.id.txtMontoTotal)
        txtProductoTop = findViewById(R.id.txtProductoTop)

        rvVentas = findViewById(R.id.rvVentas)
        rvVentas.layoutManager = LinearLayoutManager(this)
        adapter = SaleAdapter(emptyList())
        rvVentas.adapter = adapter
    }

    override fun onResume() {
        super.onResume()

        ApiClient.getSales { lista ->
            if (lista == null) {
                Toast.makeText(this, "Error al cargar ventas", Toast.LENGTH_SHORT).show()
            } else {
                adapter.actualizarLista(lista)

                // 🔹 Total de ventas
                txtTotalVentas.text = "Total de ventas: ${lista.size}"

                // 🔹 Monto total
                val montoTotal = lista.sumOf { it.quantity * it.unitPrice }
                txtMontoTotal.text = "Monto total: ₡$montoTotal"

                // 🔹 Producto más vendido
                val agrupado = lista.groupBy { it.productName }
                val top = agrupado.maxByOrNull { entry ->
                    entry.value.sumOf { it.quantity }
                }

                if (top != null) {
                    val totalUnidades = top.value.sumOf { it.quantity }
                    txtProductoTop.text =
                        "Producto más vendido: ${top.key} ($totalUnidades unidades)"
                } else {
                    txtProductoTop.text = "Producto más vendido: -"
                }
            }
        }

    }
}