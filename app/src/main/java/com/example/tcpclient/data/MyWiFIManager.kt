package com.example.tcpclient.data

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiManager
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MyWifiManager @Inject constructor(
    private val dataFlow: DataFlow,
    @ApplicationContext private val context: Context
) {

    fun initialiseWifiManager() {
        setUpWifiHandler()
    }

    private fun setUpWifiHandler(){
        val wifiStateReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val action = intent?.action
                if (action == WifiManager.WIFI_STATE_CHANGED_ACTION) {
                    when (intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN)) {
                        WifiManager.WIFI_STATE_ENABLED -> {
                            Log.d("WifiState", "Wifi is enabled")
                            dataFlow.getFlow().value = dataFlow.getFlow().value.copy(wifiEnabled = true)
                        }
                        WifiManager.WIFI_STATE_DISABLED -> {
                            Log.d("WifiState", "Wifi is disabled")
                            dataFlow.getFlow().value = dataFlow.getFlow().value.copy(wifiEnabled = false)
                        }
                    }
                }
            }
        }

        val filter = IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION)
        context.registerReceiver(wifiStateReceiver, filter)
    }
}