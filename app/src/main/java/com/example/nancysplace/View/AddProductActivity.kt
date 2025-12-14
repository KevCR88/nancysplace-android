package com.example.nancysplace.View

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.nancysplace.Data.ApiClient
import com.example.nancysplace.Entity.Product
import com.example.nancysplace.R
import java.io.ByteArrayOutputStream

class AddProductActivity : AppCompatActivity() {

    private lateinit var imgProducto: ImageView
    private lateinit var btnGaleria: Button
    private lateinit var btnCamara: Button
    private lateinit var edtNombre: EditText
    private lateinit var edtDescripcion: EditText
    private lateinit var edtPrecio: EditText
    private lateinit var edtCantidad: EditText
    private lateinit var btnGuardar: Button

    private var imgBitmap: Bitmap? = null //En esta variable se guarda la foto tomada o de galeria
    private var stringImagenFoto: String = "" //En esta variable se almacena la foto convertida

    // Launcher para GALERÍA
    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data: Intent? = result.data
                val uriImagen: Uri? = data?.data
                if (uriImagen != null) {
                    try {
                        val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uriImagen)

                        // 1. Guardar y Mostrar
                        imgBitmap = bitmap
                        imgProducto.setImageBitmap(bitmap)

                        // 2. Convertir a Base64 para la API
                        stringImagenFoto = bitmapToBase64(bitmap)

                    } catch (e: Exception) {
                        Toast.makeText(this, "Error al cargar imagen", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    // Launcher para CÁMARA
    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap: Bitmap? ->
            if (bitmap != null) {
                // 1. Mostrarla en pantalla
                imgProducto.setImageBitmap(bitmap)

                // 2. Guardarla en la variable global por si acaso
                imgBitmap = bitmap

                // 3. Aca se convierte a texto para la API
                stringImagenFoto = bitmapToBase64(bitmap)

            } else {
                Toast.makeText(this, "No se tomó la foto", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        imgProducto = findViewById(R.id.imgProducto)
        btnGaleria = findViewById(R.id.btnGaleria)
        btnCamara = findViewById(R.id.btnCamara)
        edtNombre = findViewById(R.id.edtNombre)
        edtDescripcion = findViewById(R.id.edtDescripcion)
        edtPrecio = findViewById(R.id.edtPrecio)
        edtCantidad = findViewById(R.id.edtCantidad)
        btnGuardar = findViewById(R.id.btnGuardarProducto)

        // Boton para abrir la galeria
        btnGaleria.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            galleryLauncher.launch(intent)
        }

        // Boton para abrir la camara
        btnCamara.setOnClickListener {
            cameraLauncher.launch(null)
        }
        //Bootn para guardar los productos
        btnGuardar.setOnClickListener {

            val nombre = edtNombre.text.toString()
            val descripcion = edtDescripcion.text.toString()
            val precio = edtPrecio.text.toString().toDoubleOrNull() ?: 0.0
            val cantidad = edtCantidad.text.toString().toIntOrNull() ?: 0

            // Validaciones básicas
            if (nombre.isEmpty()) {
                Toast.makeText(this, "Debe ingresar un nombre", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (precio <= 0.0) {
                Toast.makeText(this, "Precio inválido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (cantidad < 0) {
                Toast.makeText(this, "Cantidad inválida", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Crear objeto Product
            val nuevo = Product(
                id = null,
                nombre = nombre,
                descripcion = descripcion,
                precio = precio,
                cantidad = cantidad,
                categoria = intent.getStringExtra("categoria") ?: "Sin categoría",

                // Aca se da el cambio, se envia el texto Base64, no los bytes
                imagen_base64 = stringImagenFoto
            )

            // Se llama a la API
            ApiClient.postProduct(nuevo) { ok ->
                if (ok) {
                    Toast.makeText(this, "Producto guardado con foto", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Error al enviar a servidor.", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
    // Esta funcion es la que convierte la imagen de bitmap a Base 64
    private fun bitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        // Comprimimos la imagen al 50% para que no sea muy pesada y no sature el servidor
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }
}