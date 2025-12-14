package com.example.nancysplace.View

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.nancysplace.View.ProductListActivity
import com.example.nancysplace.R
import com.google.android.material.card.MaterialCardView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ProductActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)

        // Referencias a las tarjetas
        val cardCremas = findViewById<MaterialCardView>(R.id.cardCremas)
        val cardAceites = findViewById<MaterialCardView>(R.id.cardAceites)
        val cardCosmeticos = findViewById<MaterialCardView>(R.id.cardCosmeticos)
        val cardArtCasa = findViewById<MaterialCardView>(R.id.cardArtCasa)
        val cardVariado = findViewById<MaterialCardView>(R.id.cardVariado)

        // Botón flotante para añadir producto
        val fabAddProduct = findViewById<FloatingActionButton>(R.id.fabAddProduct)

        // Listeners (de momento solo mostramos un Toast para probar)

        cardCremas.setOnClickListener {
            val intent = Intent(this, ProductListActivity::class.java)
            intent.putExtra("categoria", "Cremas")
            startActivity(intent)
        }


        cardAceites.setOnClickListener {
            val intent = Intent(this, ProductListActivity::class.java)
            intent.putExtra("categoria", "Aceites")
            startActivity(intent)
        }

        cardCosmeticos.setOnClickListener {
            val intent = Intent(this, ProductListActivity::class.java)
            intent.putExtra("categoria", "Cosmeticos")
            startActivity(intent)
        }

        cardArtCasa.setOnClickListener {
            val intent = Intent(this, ProductListActivity::class.java)
            intent.putExtra("categoria", "Art. Casa")
            startActivity(intent)
        }

        cardVariado.setOnClickListener {
            val intent = Intent(this, ProductListActivity::class.java)
            intent.putExtra("categoria", "Variado")
            startActivity(intent)
        }

        fabAddProduct.setOnClickListener {
            val i = Intent(this, ProductListActivity::class.java)
            startActivity(i)
        }
    }
}