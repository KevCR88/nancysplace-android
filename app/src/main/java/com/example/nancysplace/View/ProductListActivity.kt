package com.example.nancysplace.View

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nancysplace.Adapter.ProductAdapter
import com.example.nancysplace.Data.ApiClient
import com.example.nancysplace.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ProductListActivity : AppCompatActivity() {

    private lateinit var txtTituloCategoria: TextView
    private lateinit var rvProductos: RecyclerView
    private lateinit var fabAddProductLista: FloatingActionButton
    private lateinit var adapter: ProductAdapter

    private var categoria: String = "Sin categoría"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)

        txtTituloCategoria = findViewById(R.id.txtTituloCategoria)
        rvProductos = findViewById(R.id.rvProductos)
        fabAddProductLista = findViewById(R.id.fabAddProductLista)

        categoria = intent.getStringExtra("categoria") ?: "Sin categoría"
        txtTituloCategoria.text = categoria

        // Configurar RecyclerView
        rvProductos.layoutManager = LinearLayoutManager(this)
        adapter = ProductAdapter(emptyList()) { product ->
            val i = Intent(this, ProductDetailActivity::class.java)
            i.putExtra("nombre", product.nombre)
            i.putExtra("categoria", product.categoria)
            i.putExtra("descripcion", product.descripcion)
            i.putExtra("precio", product.precio)
            i.putExtra("stock", product.cantidad)
            i.putExtra("imagen_base64", product.imagen_base64)
            startActivity(i)
        }
        rvProductos.adapter = adapter


        // FAB para añadir producto en esta categoría
        fabAddProductLista.setOnClickListener {
            val i = Intent(this, AddProductActivity::class.java)
            i.putExtra("categoria", categoria)
            startActivity(i)
        }
    }

    override fun onResume() {
        super.onResume()

        ApiClient.getProducts { lista ->
            if (lista == null) {
                Toast.makeText(this, "Error al cargar productos", Toast.LENGTH_SHORT).show()
            } else {
                // Filtramos por categoría (la API devuelve todos)
                val productosCat = lista.filter { it.categoria == categoria }
                adapter.actualizarLista(productosCat)
            }
        }
    }
}