package com.example.nancysplace.Adapter

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nancysplace.Entity.Product
import com.example.nancysplace.R

class ProductAdapter(
    private var productos: List<Product>,
    private val onItemClick: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgProducto: ImageView = view.findViewById(R.id.imgItemProducto)
        val txtNombre: TextView = view.findViewById(R.id.txtItemNombre)
        val txtInfo: TextView = view.findViewById(R.id.txtItemInfo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(v)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val p = productos[position]
        holder.txtNombre.text = p.nombre
        holder.txtInfo.text = "Stock: ${p.cantidad}"

        // Se busca el campo de texto Base64
        val base64String = p.imagen_base64

        if (!base64String.isNullOrEmpty()) {
            try {
                //Se decodifica: De Texto Base64 -> a Bytes
                val imageBytes = android.util.Base64.decode(base64String, android.util.Base64.DEFAULT)

                // Se crea el Bitmap
                val bmp = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

                // Se muestra
                holder.imgProducto.setImageBitmap(bmp)

            } catch (e: Exception) {
                // Si falla la conversión, muestra la imagen por defecto
                holder.imgProducto.setImageResource(R.drawable.ic_launcher_background)
            }
        } else {
            // Si no hay foto, muestra la imagen por defecto
            holder.imgProducto.setImageResource(R.drawable.ic_launcher_background)
        }
        // Click en toda la fila -> detalle
        holder.itemView.setOnClickListener {
            onItemClick(p)
        }
    }
    override fun getItemCount(): Int = productos.size
    fun actualizarLista(nuevaLista: List<Product>) {
        productos = nuevaLista
        notifyDataSetChanged()
    }

}