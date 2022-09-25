package com.example.tcpclient.screens

import android.content.Context
import android.net.wifi.WifiManager
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tcpclient.viewmodels.HomeViewModel
import com.example.tcpclient.R
import dagger.hilt.android.qualifiers.ApplicationContext

@Composable
fun EnableWifiScreen(
    homeViewModel: HomeViewModel
) {

    val context = LocalContext.current

    Column(
    modifier = Modifier
    .fillMaxSize()
    .padding(16.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Box(modifier = Modifier.fillMaxWidth())
        {
            Column(modifier = Modifier
                .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_wifi_off_24),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "WI-FI is disabled",
                    fontSize = 24.sp,
                    color = androidx.compose.material3.MaterialTheme.colorScheme.primary
                )
            }
        }
        Button(onClick = {
            homeViewModel.enableWifi(context)
        }) {
            Text(text = "Enable Wi-Fi")
        }
    }
}