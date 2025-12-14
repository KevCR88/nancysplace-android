package com.example.nancysplace.View

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.nancysplace.Data.ApiClient
import com.example.nancysplace.Entity.Product
import com.example.nancysplace.Entity.Sale
import com.example.nancysplace.R
import com.example.nancysplace.View.SalesHistoryActivity

class SalesActivity : AppCompatActivity() {

    private lateinit var spnCategoria: Spinner
    private lateinit var spnProducto: Spinner
    private lateinit var txtInfoStock: TextView
    private lateinit var edtCantidadVenta: EditText
    private lateinit var btnRegistrarVenta: Button

    private lateinit var btnVerHistorial: Button


    // Lista actual de productos según categoría
    private var productosCategoria: List<Product> = emptyList()
    private var productoSeleccionado: Product? = null

    private val categorias = listOf(
        "Cremas",
        "Aceites",
        "Cosméticos",
        "Art. Casa",
        "Variado"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sales)   // 👈 Asegúrate de que el XML se llama así

        spnCategoria = findViewById(R.id.spnCategoriaVenta)
        spnProducto = findViewById(R.id.spnProductoVenta)
        txtInfoStock = findViewById(R.id.txtInfoStock)
        edtCantidadVenta = findViewById(R.id.edtCantidadVenta)
        btnRegistrarVenta = findViewById(R.id.btnRegistrarVenta)
        btnVerHistorial = findViewById(R.id.btnVerHistorial)

        btnVerHistorial.setOnClickListener {
            startActivity(Intent(this, SalesHistoryActivity::class.java))
        }


        // Llenar spinner de categorías
        val adapterCategorias = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            categorias
        )
        adapterCategorias.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnCategoria.adapter = adapterCategorias

        // Cuando se selecciona una categoría, cargamos productos de esa categoría
        spnCategoria.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val cat = categorias[position]
                cargarProductosDeCategoria(cat)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Nada
            }
        }

        // Cuando se selecciona un producto, actualizamos info de stock
        spnProducto.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (productosCategoria.isNotEmpty() && position in productosCategoria.indices) {
                    productoSeleccionado = productosCategoria[position]
                    val stock = productoSeleccionado?.cantidad ?: 0
                    txtInfoStock.text = "Stock disponible: $stock"
                } else {
                    productoSeleccionado = null
                    txtInfoStock.text = "Stock disponible: -"
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Nada
            }
        }

        btnRegistrarVenta.setOnClickListener {
            registrarVenta()
        }
    }

    private fun cargarProductosDeCategoria(categoria: String) {

        ApiClient.getProducts { lista ->

            if (lista == null) {
                Toast.makeText(this, "Error al cargar productos", Toast.LENGTH_SHORT).show()

                // Mostrar un solo item vacío
                val adapterVacio = ArrayAdapter(
                    this,
                    android.R.layout.simple_spinner_item,
                    listOf("Sin productos")
                )
                spnProducto.adapter = adapterVacio
                productoSeleccionado = null
                txtInfoStock.text = "Stock disponible: -"
                return@getProducts
            }

            // Filtrar productos reales
            productosCategoria = lista.filter { it.categoria == categoria }

            if (productosCategoria.isEmpty()) {
                val adapterVacio = ArrayAdapter(
                    this,
                    android.R.layout.simple_spinner_item,
                    listOf("Sin productos")
                )
                spnProducto.adapter = adapterVacio
                productoSeleccionado = null
                txtInfoStock.text = "Stock disponible: -"
            } else {
                val nombres = productosCategoria.map { it.nombre }
                val adapterProductos = ArrayAdapter(
                    this,
                    android.R.layout.simple_spinner_item,
                    nombres
                )
                spnProducto.adapter = adapterProductos

                productoSeleccionado = productosCategoria[0]
                txtInfoStock.text = "Stock disponible: ${productoSeleccionado?.cantidad}"
            }
        }
    }


    private fun registrarVenta() {
        val producto = productoSeleccionado
        if (producto == null) {
            Toast.makeText(this, "Debe seleccionar un producto válido", Toast.LENGTH_SHORT).show()
            return
        }

        val cantidadStr = edtCantidadVenta.text.toString().trim()
        val cantidadVenta = cantidadStr.toIntOrNull()

        if (cantidadVenta == null || cantidadVenta <= 0) {
            Toast.makeText(this, "Cantidad inválida", Toast.LENGTH_SHORT).show()
            return
        }

        if (cantidadVenta > producto.cantidad) {
            Toast.makeText(this, "No hay stock suficiente", Toast.LENGTH_SHORT).show()
            return
        }

        // Descontar stock
        producto.cantidad -= cantidadVenta

        // 🔹 NUEVO: Registrar venta en el repositorio
        val sale = Sale(
            productId = producto.id ?: 0,
            productName = producto.nombre,
            quantity = cantidadVenta,
            unitPrice = producto.precio
        )
       // SalesRepository.registrarVenta(sale)
        // Enviar venta a la API Flask
        ApiClient.enviarVenta(sale) { ok ->
            runOnUiThread {
                if (!ok) {
                    Toast.makeText(
                        this,
                        "No se pudo enviar la venta al servidor",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        // Actualizar texto de stock en pantalla
        txtInfoStock.text = "Stock disponible: ${producto.cantidad}"
        edtCantidadVenta.text.clear()

        Toast.makeText(this, "Venta registrada", Toast.LENGTH_SHORT).show()
    }


}