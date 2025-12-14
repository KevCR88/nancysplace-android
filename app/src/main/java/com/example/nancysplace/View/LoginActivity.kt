package com.example.nancysplace.View

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.nancysplace.Data.ApiClient
import com.example.nancysplace.View.HomeActivity
import com.example.nancysplace.R
import com.example.nancysplace.Util.SessionManager

class LoginActivity : AppCompatActivity() {

    private lateinit var btnLogin: Button
    private lateinit var edtUser: EditText
    private lateinit var edtPass: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        if (SessionManager.isLogged(this)) {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
            return
        }

        btnLogin = findViewById(R.id.btn_login)
        edtUser = findViewById(R.id.edtUser)
        edtPass = findViewById(R.id.edtPass)

        btnLogin.setOnClickListener {
            val user = edtUser.text.toString().trim()
            val pass = edtPass.text.toString().trim()

            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Ingrese usuario y contraseña", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            ApiClient.login(user, pass) { ok ->
                if (ok) {
                    SessionManager.setLogged(this, true)
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }
}