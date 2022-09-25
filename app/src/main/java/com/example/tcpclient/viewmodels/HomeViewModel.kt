package com.example.tcpclient.viewmodels

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.provider.Settings
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.tcpclient.data.DataFlow
import com.example.tcpclient.data.MyWifiManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val _dataFlow: DataFlow,
    @ApplicationContext context: Context,
    private val myWifiManager: MyWifiManager
): ViewModel() {

    private val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    private var firstTime: Boolean = true
    val stateFlow = _dataFlow.getFlow().asStateFlow()

    /**
     * Function changes the state of the permission state flow to let composable know that
     * permissions have been granted. If this is the first time opening the app, the WIFI Manager is
     * also initialised.
     */
    fun grantPermissions() {
        if(firstTime){
            _dataFlow.getFlow().value = _dataFlow.getFlow().value.copy(permissionsGranted = true)
            myWifiManager.initialiseWifiManager()
        }
        firstTime = false
    }

    fun enableWifi(context: Context) {
        (context as Activity).startActivityForResult(
            Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY),
            0
        )
    }

    init {
        _dataFlow.getFlow().value = _dataFlow.getFlow().value.copy(
            wifiEnabled = wifiManager.isWifiEnabled
        )
        Log.d("ViewModel", "Wifi enabled: ${wifiManager.isWifiEnabled}")
    }

}