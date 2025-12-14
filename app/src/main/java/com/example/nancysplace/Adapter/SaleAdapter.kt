package com.example.nancysplace.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nancysplace.Entity.Sale
import com.example.nancysplace.R

class SaleAdapter(
    private var ventas: List<Sale>
) : RecyclerView.Adapter<SaleAdapter.ViewHolder>() {

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val txtNombre: TextView = v.findViewById(R.id.txtNombreVenta)
        val txtCantidad: TextView = v.findViewById(R.id.txtCantidadVenta)
        val txtPrecio: TextView = v.findViewById(R.id.txtPrecioVenta)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_sale, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val s = ventas[position]
        holder.txtNombre.text = s.productName
        holder.txtCantidad.text = "Cantidad: ${s.quantity}"
        holder.txtPrecio.text = "₡ ${s.unitPrice * s.quantity}"
    }

    override fun getItemCount() = ventas.size

    fun actualizar(lista: List<Sale>) {
        ventas = lista
        notifyDataSetChanged()
    }

    fun actualizarLista(lista: List<Sale>) {
        actualizar(lista)
    }
}