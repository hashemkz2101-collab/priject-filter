package com.mahroch.client

import android.app.Activity
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.VpnService
import android.os.Build
import android.os.Bundle
import android.widget.*
import android.provider.Settings

class MainActivity : Activity() {
    private val admin by lazy {
        ComponentName(this, MahrochDeviceAdminReceiver::class.java)
    }

    override fun onCreate(b: Bundle?) {
        super.onCreate(b)
        setContentView(R.layout.activity_main)

        findViewById<TextView>(R.id.status).text =
            if (Policy.enabled(this)) "وضعیت: فعال" else "وضعیت: خاموش"

        findViewById<TextView>(R.id.network).text =
            "Wi-Fi: ${Policy.ssid(this)}\nGateway: ${Policy.gateway(this)}\nDNS: 8.8.8.8"

        findViewById<TextView>(R.id.domains).text =
            "دامنه‌های مجاز:\n" + Policy.domains(this).joinToString("\n")

        findViewById<Button>(R.id.vpnButton).setOnClickListener {
            enableVpn()
        }

        // This button is intentionally visible only for managed setup.
        // It does not silently acquire Device Owner; Android requires provisioning.
        findViewById<Button>(R.id.manageButton).setOnClickListener {
            enableAlwaysOnIfDeviceOwner()
        }
    }

    private fun enableVpn() {
        Policy.setEnabled(this, true)
        val p = VpnService.prepare(this)
        if (p != null) {
            startActivityForResult(p, 101)
        } else {
            startClient()
        }
    }

    private fun startClient() {
        val i = Intent(this, FirewallVpnService::class.java)
        if (Build.VERSION.SDK_INT >= 26) startForegroundService(i) else startService(i)
    }

    private fun enableAlwaysOnIfDeviceOwner() {
        if (Build.VERSION.SDK_INT < 24) {
            toast("Always-on VPN به Android 7 یا بالاتر نیاز دارد.")
            return
        }

        val dpm = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        if (!dpm.isDeviceOwnerApp(packageName)) {
            toast("این گوشی هنوز تحت مدیریت Mahroch نیست. ابتدا Client را به Device Owner تبدیل کنید.")
            return
        }

        try {
            dpm.setAlwaysOnVpnPackage(admin, packageName, null)
            if (Build.VERSION.SDK_INT >= 29) {
                dpm.setLockTaskPackages(admin, arrayOf(packageName))
            }
            // If Android exposes lockdown for this managed VPN, enable it.
            if (Build.VERSION.SDK_INT >= 29) {
                dpm.setAlwaysOnVpnPackage(admin, packageName, null)
            }
            Policy.setEnabled(this, true)
            toast("Always-on VPN فعال شد.")
            startClient()
        } catch (e: Exception) {
            toast("خطا در فعال‌سازی Always-on VPN: ${e.message}")
        }
    }

    private fun toast(s: String) = Toast.makeText(this, s, Toast.LENGTH_LONG).show()

    override fun onActivityResult(r: Int, c: Int, d: Intent?) {
        super.onActivityResult(r, c, d)
        if (r == 101 && c == RESULT_OK) startClient()
    }
}
