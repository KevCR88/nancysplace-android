package com.example.nancysplace.View

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.nancysplace.View.ProductActivity
import com.example.nancysplace.R
import com.example.nancysplace.View.SalesActivity
import com.example.nancysplace.Util.SessionManager

class HomeActivity : AppCompatActivity() {

    private lateinit var btnVentas: Button
    private lateinit var btnProductos: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        btnVentas = findViewById(R.id.btnVentas)
        btnProductos = findViewById(R.id.btnProductos)

        // De momento no navegan a ningún lado, solo existen
        btnVentas.setOnClickListener {
            val intent = Intent(this, SalesActivity::class.java)
            startActivity(intent)
        }

        btnProductos.setOnClickListener {
            val intent = Intent(this, ProductActivity::class.java)
            startActivity(intent)
        }

        val btnLogout = findViewById<Button>(R.id.btnLogout)

        btnLogout.setOnClickListener {
            SessionManager.logout(this)
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

    }
}