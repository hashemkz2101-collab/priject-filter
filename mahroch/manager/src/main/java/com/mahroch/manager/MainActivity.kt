package com.mahroch.manager

import android.app.Activity
import android.os.Bundle
import android.widget.*
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

class MainActivity : Activity() {
    override fun onCreate(b: Bundle?) {
        super.onCreate(b)
        setContentView(R.layout.activity_main)
        val ip = findViewById<EditText>(R.id.clientIp)
        val token = findViewById<EditText>(R.id.token)
        val domains = findViewById<EditText>(R.id.domains)
        val result = findViewById<TextView>(R.id.result)
        domains.setText("mahroch.com\nmahroch-ir.ir\nscript.google.com")
        findViewById<Button>(R.id.push).setOnClickListener {
            val host = ip.text.toString().trim()
            val t = token.text.toString()
            val d = domains.text.toString()
            Thread {
                try {
                    val c = URL("http://$host:8765/policy").openConnection() as HttpURLConnection
                    c.requestMethod = "POST"
                    c.doOutput = true
                    c.connectTimeout = 5000
                    c.readTimeout = 5000
                    c.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
                    val body = "token=${URLEncoder.encode(t, "UTF-8")}&domains=${URLEncoder.encode(d, "UTF-8")}"
                    c.outputStream.use { it.write(body.toByteArray()) }
                    val code = c.responseCode
                    runOnUiThread { result.text = "پاسخ Client: HTTP $code" }
                } catch (e: Exception) {
                    runOnUiThread { result.text = "خطا: ${e.message}" }
                }
            }.start()
        }
    }
}
