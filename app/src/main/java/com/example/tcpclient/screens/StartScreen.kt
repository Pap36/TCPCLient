package com.example.tcpclient.screens

import android.Manifest
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tcpclient.viewmodels.HomeViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun StartScreen() {
    /*Scaffold(
        topBar = { TopBar() }
    ) {*/
    val homeViewModel = hiltViewModel<HomeViewModel>()

    val permissionState =
        rememberPermissionState(permission = Manifest.permission.ACCESS_WIFI_STATE)

    when (permissionState.status) {
        PermissionStatus.Granted -> {
            homeViewModel.grantPermissions()
            NavigationScreen()
        }
        is PermissionStatus.Denied -> {
            PermissionScreen(permissionState = permissionState)
        }
    }

    //}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar() {
    TopAppBar(
        title = {
            Text(text = "TCP Client", color = MaterialTheme.colorScheme.onBackground)
        }
    )
}

@Preview
@Composable
fun TopBarPreview() {
    TopBar()
}

