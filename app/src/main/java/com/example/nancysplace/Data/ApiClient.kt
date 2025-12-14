package com.example.nancysplace.Data

import com.example.nancysplace.Entity.Product
import com.example.nancysplace.Entity.Sale
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

object ApiClient {

    // ⚠️ Asegúrate de que esta IP es la de tu PC en el hotspot del cel
    private const val BASE_URL = "https://kevincr88.pythonanywhere.com/"

    fun login(username: String, password: String, onResult: (Boolean) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val url = URL("$BASE_URL/login")
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "POST"
                conn.connectTimeout = 5000
                conn.readTimeout = 5000
                conn.doOutput = true
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8")

                val json = JSONObject().apply {
                    put("username", username)
                    put("password", password)
                }

                conn.outputStream.use { os ->
                    os.write(json.toString().toByteArray(Charsets.UTF_8))
                }

                val ok = conn.responseCode in 200..299

                withContext(Dispatchers.Main) { onResult(ok) }
                conn.disconnect()
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) { onResult(false) }
            }
        }
    }


    fun getProducts(onResult: (List<Product>?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val url = URL("$BASE_URL/productos")
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "GET"
                conn.connectTimeout = 5000
                conn.readTimeout = 5000

                val code = conn.responseCode
                if (code in 200..299) {
                    val reader = BufferedReader(InputStreamReader(conn.inputStream))
                    val response = reader.readText()
                    reader.close()

                    // Parseamos el JSON que devuelve Flask
                    val arr = JSONArray(response)
                    val list = mutableListOf<Product>()

                    for (i in 0 until arr.length()) {
                        val obj = arr.getJSONObject(i)

                        val p = Product(
                            id = obj.optInt("id"),                   // Int? encaja perfectamente
                            nombre = obj.optString("nombre"),
                            descripcion = obj.optString("descripcion"),
                            precio = obj.optDouble("precio"),
                            cantidad = obj.optInt("cantidad"),
                            categoria = obj.optString("categoria"),
                            imagen_base64 = obj.optString("imagen_base64")
                        )

                        list.add(p)
                    }

                    withContext(Dispatchers.Main) {
                        onResult(list)
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        onResult(null)
                    }
                }

                conn.disconnect()
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    onResult(null)
                }
            }
        }
    }
    fun postProduct(product: Product, onResult: (Boolean) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val url = URL("$BASE_URL/productos")
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "POST"
                conn.connectTimeout = 5000
                conn.readTimeout = 5000
                conn.doOutput = true
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8")

                // Armamos el JSON tal como lo envías desde Postman
                val json = JSONObject().apply {
                    put("nombre", product.nombre)
                    put("descripcion", product.descripcion)
                    put("precio", product.precio)
                    put("cantidad", product.cantidad)
                    put("categoria", product.categoria)
                    put("imagen_base64", product.imagen_base64)
                }

                val out = conn.outputStream
                out.write(json.toString().toByteArray(Charsets.UTF_8))
                out.flush()
                out.close()

                val code = conn.responseCode
                val ok = code in 200..299

                withContext(Dispatchers.Main) {
                    onResult(ok)
                }

                conn.disconnect()
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    onResult(false)
                }
            }
        }
    }
    fun enviarVenta(venta: Sale, onResult: (Boolean) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val url = URL("$BASE_URL/ventas")
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "POST"
                conn.connectTimeout = 5000
                conn.readTimeout = 5000
                conn.doOutput = true
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8")

                // Armamos el JSON tal y como lo espera la API Flask
                val json = JSONObject().apply {
                    put("product_id", venta.productId)
                    put("quantity", venta.quantity)
                    put("unit_price", venta.unitPrice)
                    // si algún día quieres enviar fecha u otro dato, aquí se agrega
                }

                conn.outputStream.use { os ->
                    os.write(json.toString().toByteArray(Charsets.UTF_8))
                }

                val code = conn.responseCode
                val ok = code in 200..299

                withContext(Dispatchers.Main) {
                    onResult(ok)
                }

                conn.disconnect()
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    onResult(false)
                }
            }
        }
    }
    fun getSales(onResult: (List<Sale>?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val url = URL("$BASE_URL/ventas")
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "GET"
                conn.connectTimeout = 5000
                conn.readTimeout = 5000

                val code = conn.responseCode
                if (code in 200..299) {
                    val reader = BufferedReader(InputStreamReader(conn.inputStream))
                    val response = reader.readText()
                    reader.close()

                    val arr = JSONArray(response)
                    val list = mutableListOf<Sale>()

                    for (i in 0 until arr.length()) {
                        val obj = arr.getJSONObject(i)

                        val s = Sale(
                            // id del backend es Int, pero tu Sale tiene id String → no lo usamos
                            productId = obj.optInt("product_id"),
                            productName = obj.optString("product_name"),
                            quantity = obj.optInt("quantity"),
                            unitPrice = obj.optDouble("unit_price"),
                            date = System.currentTimeMillis() // por ahora
                        )

                        list.add(s)
                    }

                    withContext(Dispatchers.Main) { onResult(list) }
                } else {
                    withContext(Dispatchers.Main) { onResult(null) }
                }

                conn.disconnect()
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) { onResult(null) }
            }
        }
    }



}