package com.example.tcpclient.screens

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.google.accompanist.permissions.*

/**
 * Composable function which manages application WIFI permissions
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionScreen(
    permissionState: PermissionState
) {
    RequestPermissionView(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background),
        permissionState
    )
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun RequestPermissionView(
    modifier: Modifier = Modifier,
    permissionState: PermissionState
) {

    val context = LocalContext.current

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Please grant WIFI permissions",
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (permissionState.status.shouldShowRationale) {
            Button(onClick = {
                permissionState.launchPermissionRequest()
            }) {
                Text(text = "Grant WIFI permissions")
            }
        } else {
            Button(onClick = {
                ContextCompat.startActivity(
                    context,
                    Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.fromParts("package", context.packageName, null)
                    ),
                    null
                )
            }) {
                Text(text = "Grant WIFI permissions in settings")
            }
        }
    }
}