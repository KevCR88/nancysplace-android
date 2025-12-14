package com.example.nancysplace.View

import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.nancysplace.R

class ProductDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        val img = findViewById<ImageView>(R.id.imgDetalleProducto)
        val txtNombre = findViewById<TextView>(R.id.txtDetalleNombre)
        val txtCategoria = findViewById<TextView>(R.id.txtDetalleCategoria)
        val txtDescripcion = findViewById<TextView>(R.id.txtDetalleDescripcion)
        val txtPrecio = findViewById<TextView>(R.id.txtDetallePrecio)
        val txtStock = findViewById<TextView>(R.id.txtDetalleStock)

        val nombre = intent.getStringExtra("nombre") ?: ""
        val categoria = intent.getStringExtra("categoria") ?: ""
        val descripcion = intent.getStringExtra("descripcion") ?: ""
        val precio = intent.getDoubleExtra("precio", 0.0)
        val stock = intent.getIntExtra("stock", 0)
        val imagenString = intent.getStringExtra("imagen_base64")

        txtNombre.text = nombre
        txtCategoria.text = "Categoría: $categoria"
        txtDescripcion.text = "Descripción: $descripcion"
        txtPrecio.text = "Precio: ₡$precio"
        txtStock.text = "Stock: $stock"

        //Aca se decodifica el texto a imagen
        if (!imagenString.isNullOrEmpty()) {
            try {
                //Aca se convierte el texto Base64 de vuelta a Bytes
                val imageBytes =
                    android.util.Base64.decode(imagenString, android.util.Base64.DEFAULT)

                // Aqui se convierte la imagen (Bitmap) desde los bytes
                val bmp = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

                img.setImageBitmap(bmp)
            } catch (e: Exception) {
                // Si algo falla, se pone la imagen por defecto
                img.setImageResource(R.drawable.ic_launcher_background)
            }
        } else {
            // Si viene vacío, imagen por defecto
            img.setImageResource(R.drawable.ic_launcher_background)
        }
    }
}